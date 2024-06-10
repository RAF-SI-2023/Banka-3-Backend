package rs.edu.raf.exchangeservice;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import rs.edu.raf.exchangeservice.client.BankServiceClient;
import rs.edu.raf.exchangeservice.domain.dto.bank.CompanyOtcDto;
import rs.edu.raf.exchangeservice.domain.dto.offer.FrontendOfferDto;
import rs.edu.raf.exchangeservice.domain.dto.offer.MyStockDto;
import rs.edu.raf.exchangeservice.domain.dto.offer.OfferDto;
import rs.edu.raf.exchangeservice.domain.model.listing.Bank4Stock;
import rs.edu.raf.exchangeservice.domain.model.myListing.MyStock;
import rs.edu.raf.exchangeservice.domain.model.offer.MyOffer;
import rs.edu.raf.exchangeservice.domain.model.offer.Offer;
import rs.edu.raf.exchangeservice.domain.model.offer.OfferStatus;
import rs.edu.raf.exchangeservice.repository.listingRepository.Bank4StockRepository;
import rs.edu.raf.exchangeservice.repository.myListingRepository.MyStockRepository;
import rs.edu.raf.exchangeservice.repository.offerRepository.MyOfferRepository;
import rs.edu.raf.exchangeservice.repository.offerRepository.OfferRepository;
import rs.edu.raf.exchangeservice.service.Banka4OtcService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class Banka4OtcServiceTest {
    @Mock
    private OfferRepository offerRepository;

    @Mock
    private MyStockRepository myStockRepository;

    @Mock
    private Bank4StockRepository bank4StockRepository;

    @Mock
    private MyOfferRepository myOfferRepository;

    @Mock
    private BankServiceClient bankServiceClient;

    @Mock
    private ResponseEntity<List<MyStockDto>> responseEntity;

    @InjectMocks
    private Banka4OtcService banka4OtcService;

    @Test
    public void testFindAllOffers() {
        // Priprema
        Offer offer1 = new Offer();
        offer1.setIdBank4(1L);
        offer1.setAmount(100);
        offer1.setTicker("TICKER1");
        offer1.setPrice(50.0);

        Offer offer2 = new Offer();
        offer2.setIdBank4(2L);
        offer2.setAmount(200);
        offer2.setTicker("TICKER2");
        offer2.setPrice(75.0);

        List<Offer> offers = new ArrayList<>();
        offers.add(offer1);
        offers.add(offer2);

        when(offerRepository.findAll()).thenReturn(offers);

        // Izvršenje
        List<Offer> result = banka4OtcService.findAllOffers();

        // Provera
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(offer1, result.get(0));
        assertEquals(offer2, result.get(1));
    }
    @Test
    void findAllStocks_ReturnsCorrectDtoList() {
        // Mock podaci
        MyStock myStock1 = new MyStock();
        myStock1.setTicker("AAPL");
        myStock1.setPublicAmount(10);

        MyStock myStock2 = new MyStock();
        myStock2.setTicker("GOOGL");
        myStock2.setPublicAmount(5);

        List<MyStock> myStocks = new ArrayList<>();
        myStocks.add(myStock1);
        myStocks.add(myStock2);
        // Postavljamo mock ponašanje za myStockRepository
        when(myStockRepository.findAllByCompanyIdAndPublicAmountGreaterThan(1L, 0)).thenReturn(myStocks);

        // Pozivamo metodu koju testiramo
        List<MyStockDto> result = banka4OtcService.findAllStocks();

        // Provera rezultata
        assertEquals(2, result.size()); // Očekujemo da će vratiti samo jedan element
        assertEquals("AAPL", result.get(0).getTicker()); // Provera da li je ticker ispravan
        assertEquals(10, result.get(0).getAmount()); // Provera da li je amount ispravan
    }

    @Test
    void findAllStocks_NoStocksFound() {
        // Postavljamo mock ponašanje za myStockRepository kada nema pronađenih stock-ova
        when(myStockRepository.findAllByCompanyIdAndPublicAmountGreaterThan(1L, 0)).thenReturn(new ArrayList<>());

        // Pozivamo metodu koju testiramo
        List<MyStockDto> result = banka4OtcService.findAllStocks();

        // Provera rezultata
        assertEquals(0, result.size()); // Očekujemo da neće vratiti nijedan element
    }
    @Test
    void receiveValidOfferTest() {
        // Priprema
        OfferDto offerDto = new OfferDto();
        offerDto.setIdBank4(1l);
        offerDto.setAmount(50);  // Postavljamo iznos manji od publicAmount
        offerDto.setTicker("AAPL");
        offerDto.setPrice(100.0);

        MyStock myStock = new MyStock();
        myStock.setPublicAmount(100); // Postavljamo dovoljan publicAmount

        when(myStockRepository.findByTickerAndCompanyId("AAPL", 1L)).thenReturn(myStock);

        // Izvršenje
        Offer result = banka4OtcService.receiveOffer(offerDto);

        // Provera
        assertEquals(OfferStatus.PROCESSING, result.getOfferStatus());
    }
    @Test
    void receiveInvalidOfferTest() {
        // Priprema
        OfferDto offerDto = new OfferDto();
        offerDto.setIdBank4(1l);
        offerDto.setAmount(150);  // Postavljamo iznos veći od publicAmount
        offerDto.setTicker("AAPL");
        offerDto.setPrice(100.0);

        MyStock myStock = new MyStock();
        myStock.setPublicAmount(100); // Postavljamo nedovoljan publicAmount

        when(myStockRepository.findByTickerAndCompanyId("AAPL", 1L)).thenReturn(myStock);

        // Izvršenje
        Offer result = banka4OtcService.receiveOffer(offerDto);

        // Provera
        assertEquals(OfferStatus.DECLINED, result.getOfferStatus());
    }
    @Test
    void acceptOfferTest() {
        // Priprema
        Offer offer = new Offer();
        offer.setOfferId(1L);
        offer.setTicker("AAPL");
        offer.setAmount(50);
        offer.setPrice(100.0);
        offer.setOfferStatus(OfferStatus.PROCESSING);

        MyStock myStock = new MyStock();
        myStock.setAmount(100);
        myStock.setPublicAmount(100);

        when(offerRepository.findById(1L)).thenReturn(Optional.of(offer));
        when(myStockRepository.findByTickerAndCompanyId("AAPL", 1L)).thenReturn(myStock);

        // Izvršenje
        Offer result = banka4OtcService.acceptOffer(1L);

        // Provera
        assertNotNull(result);
        assertEquals(OfferStatus.ACCEPTED, result.getOfferStatus());
        assertEquals(50, myStock.getAmount()); // Proveravamo smanjenje količine
        assertEquals(50, myStock.getPublicAmount()); // Proveravamo smanjenje publicAmount
        verify(offerRepository, times(2)).save(offer);
        verify(myStockRepository, times(1)).save(myStock);
        verify(bankServiceClient, times(1)).otcBank4transaction(any(CompanyOtcDto.class));
    }

    @Test
    void acceptOfferNotFoundTest() {
        // Priprema
        when(offerRepository.findById(1L)).thenReturn(Optional.empty());

        // Izvršenje
        Offer result = banka4OtcService.acceptOffer(1L);

        // Provera
        assertEquals(null, result);
        verify(myStockRepository, never()).findByTickerAndCompanyId(anyString(), anyLong());
        verify(offerRepository, never()).save(any());
        verify(bankServiceClient, never()).otcBank4transaction(any());
    }

    @Test
    public void testDeclineOffer() {
        // Priprema
        Offer offer = new Offer();
        offer.setOfferId(123L);
        offer.setOfferStatus(OfferStatus.PROCESSING);

        when(offerRepository.findById(anyLong())).thenReturn(Optional.of(offer));
        when(offerRepository.save(any(Offer.class))).thenReturn(new Offer());

        // Izvršenje
        Offer declinedOffer = banka4OtcService.declineOffer(123L);

        // Provera
        assertNotNull(declinedOffer);
        assertEquals(OfferStatus.DECLINED, declinedOffer.getOfferStatus());
    }
    @Test
    public void testGetMyOffers() {
        // Priprema
        List<MyOffer> myOffers = new ArrayList<>();
        MyOffer offer1 = new MyOffer();
        offer1.setAmount(10);
        offer1.setTicker("ABC");
        offer1.setPrice(100.0);
        myOffers.add(offer1);

        MyOffer offer2 = new MyOffer();
        offer2.setAmount(20);
        offer2.setTicker("XYZ");
        offer2.setPrice(200.0);
        myOffers.add(offer2);

        when(myOfferRepository.findAll()).thenReturn(myOffers);

        // Izvršenje
        List<MyOffer> result = banka4OtcService.getMyOffers();

        // Provera
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(offer1.getAmount(), result.get(0).getAmount());
        assertEquals(offer1.getTicker(), result.get(0).getTicker());
        assertEquals(offer1.getPrice(), result.get(0).getPrice());
        assertEquals(offer2.getAmount(), result.get(1).getAmount());
        assertEquals(offer2.getTicker(), result.get(1).getTicker());
        assertEquals(offer2.getPrice(), result.get(1).getPrice());
        verify(myOfferRepository, times(1)).findAll();
    }
    @Test
    public void testOfferDeclined() {
        // Priprema
        Long offerId = 1L;
        MyOffer myOffer = new MyOffer();
        myOffer.setMyOfferId(offerId);
        myOffer.setOfferStatus(OfferStatus.PROCESSING);

        when(myOfferRepository.findById(offerId)).thenReturn(Optional.of(myOffer));

        // Izvršenje
        MyOffer result = banka4OtcService.offerDeclined(offerId);

        // Provera
        assertNotNull(result);
        assertEquals(OfferStatus.DECLINED, result.getOfferStatus());
        verify(myOfferRepository, times(1)).findById(offerId);
        verify(myOfferRepository, times(1)).save(myOffer);
    }

    @Test
    public void testOfferDeclined_NotFound() {
        // Priprema
        Long offerId = 1L;
        when(myOfferRepository.findById(offerId)).thenReturn(Optional.empty());

        // Izvršenje
        MyOffer result = banka4OtcService.offerDeclined(offerId);

        // Provera
        assertNull(result);
        verify(myOfferRepository, times(1)).findById(offerId);
        verify(myOfferRepository, never()).save(any(MyOffer.class));
    }

    @Test
    public void testOfferAccepted() {
        // Mocking data
        Long offerId = 1L;
        MyOffer myOffer = new MyOffer();
        myOffer.setMyOfferId(offerId);
        myOffer.setTicker("TICKER");
        myOffer.setAmount(100);
        myOffer.setPrice(500.0);
        myOffer.setOfferStatus(OfferStatus.PROCESSING);

        MyStock myStock = new MyStock();
        myStock.setAmount(100);

        // Mocking repository methods
        when(myOfferRepository.findById(offerId)).thenReturn(Optional.of(myOffer));
        when(myStockRepository.findByTicker(myOffer.getTicker())).thenReturn(myStock);
        when(myStockRepository.findByTickerAndCompanyId(myOffer.getTicker(), 1L)).thenReturn(myStock);

        // Call the method
        MyOffer result = banka4OtcService.offerAccepted(offerId);

        // Verify that repository methods are called
        verify(myOfferRepository, times(1)).findById(offerId);
        verify(myStockRepository, times(1)).findByTickerAndCompanyId(myOffer.getTicker(), 1L);
        verify(myStockRepository, times(1)).save(any(MyStock.class));
        verify(bankServiceClient, times(1)).otcBank4transaction(any(CompanyOtcDto.class));
        verify(myOfferRepository, times(1)).save(myOffer);

        // Assert the result
        assertEquals(OfferStatus.ACCEPTED, result.getOfferStatus());
        // Add more assertions if needed
    }
    @Test
    public void testOfferAccepted_WhenMyStockIsNull() {
        // Mocking data
        Long offerId = 1L;
        String ticker = "TICKER";
        MyOffer myOffer = new MyOffer();
        myOffer.setMyOfferId(1L);
        myOffer.setTicker(ticker);
        myOffer.setAmount(100);
        myOffer.setPrice(500.0);
        myOffer.setOfferStatus(OfferStatus.PROCESSING);

        // Mocking repository methods
        when(myOfferRepository.findById(offerId)).thenReturn(Optional.of(myOffer));
        when(myStockRepository.findByTicker(ticker)).thenReturn(null);

        // Call the method
        MyOffer result = banka4OtcService.offerAccepted(offerId);

        // Verify that repository methods are called
        verify(myOfferRepository, times(1)).findById(offerId);
        verify(myStockRepository, times(1)).findByTicker(ticker);
        verify(myStockRepository, times(1)).save(any(MyStock.class)); // Verifying save method is called

        // Assert the result
        assertEquals(OfferStatus.ACCEPTED, result.getOfferStatus());

        // Additional assertions for MyStock creation
        ArgumentCaptor<MyStock> myStockCaptor = ArgumentCaptor.forClass(MyStock.class);
        verify(myStockRepository).save(myStockCaptor.capture());
        MyStock savedStock = myStockCaptor.getValue();
        assertEquals(ticker, savedStock.getTicker());
        assertEquals(1L, savedStock.getCompanyId());
        assertEquals(100, savedStock.getAmount());
        assertEquals(0, savedStock.getPrivateAmount());
        assertEquals(100, savedStock.getPublicAmount());
        assertEquals("RSD", savedStock.getCurrencyMark());
        assertEquals(5.0, savedStock.getMinimumPrice()); // Assuming price is 500.0, and amount is 100
    }
    @Test
    void offerAcceptedInvalidOfferIdTest() {
        // Priprema
        when(myOfferRepository.findById(1L)).thenReturn(Optional.empty());

        // Izvršenje
        MyOffer result = banka4OtcService.offerAccepted(1L);

        // Provera
        assertEquals(null, result);

        verify(myOfferRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(myOfferRepository);
        verifyNoInteractions(myStockRepository);
        verifyNoInteractions(bankServiceClient);
    }
    @Test
    public void testOfferAccepted_NotFound() {
        // Priprema
        Long offerId = 1L;

        when(myOfferRepository.findById(offerId)).thenReturn(Optional.empty());

        // Izvršenje
        MyOffer result = banka4OtcService.offerAccepted(offerId);

        // Provera
        assertNull(result);
        verify(myOfferRepository, times(1)).findById(offerId);
        verify(myOfferRepository, never()).save(any(MyOffer.class));
        verify(myStockRepository, never()).save(any(MyStock.class));
    }
    @Test
    void getAllStocksForBank4Test() {
        // Priprema
        List<Bank4Stock> bank4Stocks = new ArrayList<>();
        bank4Stocks.add(new Bank4Stock(1L, "AAPL", 100));
        bank4Stocks.add(new Bank4Stock(2L, "GOOGL", 200));

        when(bank4StockRepository.findAll()).thenReturn(bank4Stocks);

        // Izvršenje
        List<MyStockDto> result = banka4OtcService.getAllStocksForBank4();

        // Provera
        assertEquals(2, result.size());
        assertEquals("AAPL", result.get(0).getTicker());
        assertEquals(100, result.get(0).getAmount());
        assertEquals("GOOGL", result.get(1).getTicker());
        assertEquals(200, result.get(1).getAmount());

        verify(bank4StockRepository, times(1)).findAll();
    }

    @Test
    void getAllStocksForBank4EmptyTest() {
        // Priprema
        when(bank4StockRepository.findAll()).thenReturn(new ArrayList<>());

        // Izvršenje
        List<MyStockDto> result = banka4OtcService.getAllStocksForBank4();

        // Provera
        assertEquals(0, result.size());

        verify(bank4StockRepository, times(1)).findAll();
    }
}