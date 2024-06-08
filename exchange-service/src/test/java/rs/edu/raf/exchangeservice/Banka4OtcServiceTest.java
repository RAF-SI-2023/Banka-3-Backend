package rs.edu.raf.exchangeservice;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import rs.edu.raf.exchangeservice.domain.dto.offer.FrontendOfferDto;
import rs.edu.raf.exchangeservice.domain.dto.offer.MyStockDto;
import rs.edu.raf.exchangeservice.domain.dto.offer.OfferDto;
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
    private RestTemplate restTemplate;

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
        offer1.setPrice(50);

        Offer offer2 = new Offer();
        offer2.setIdBank4(2L);
        offer2.setAmount(200);
        offer2.setTicker("TICKER2");
        offer2.setPrice(75);

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
    public void testFindAllStocks() {
        // Priprema
        List<MyStock> myStocks = new ArrayList<>();
        MyStock myStock1 = new MyStock();
        myStock1.setAmount(100);
        myStock1.setTicker("TICKER1");
        myStocks.add(myStock1);

        MyStock myStock2 = new MyStock();
        myStock2.setAmount(200);
        myStock2.setTicker("TICKER2");
        myStocks.add(myStock2);

        when(myStockRepository.findAllByCompanyId(1L)).thenReturn(myStocks);

        // Izvršenje
        List<MyStockDto> result = banka4OtcService.findAllStocks();

        // Provera
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(100, result.get(0).getAmount());
        assertEquals("TICKER1", result.get(0).getTicker());
        assertEquals(200, result.get(1).getAmount());
        assertEquals("TICKER2", result.get(1).getTicker());
    }
    @Test
    public void testReceiveOffer() {
        // Priprema
        OfferDto offerDto = new OfferDto();
        offerDto.setIdBank4(123L);
        offerDto.setAmount(100);
        offerDto.setTicker("TICKER");
        offerDto.setPrice(10);

        when(offerRepository.save(any(Offer.class))).thenReturn(new Offer());

        // Izvršenje
        Offer result = banka4OtcService.receiveOffer(offerDto);

        // Provera
        assertNotNull(result);
        assertEquals(OfferStatus.PROCESSING, result.getOfferStatus());
    }

    @Test
    public void testAcceptOffer() {
        // Priprema
        Offer offer = new Offer();
        offer.setOfferId(123L);
        offer.setAmount(50);
        offer.setTicker("TICKER");
        offer.setOfferStatus(OfferStatus.PROCESSING);

        MyStock myStock = new MyStock();
        myStock.setAmount(100);
        myStock.setTicker("TICKER");

        when(offerRepository.findById(anyLong())).thenReturn(Optional.of(offer));
        when(myStockRepository.findByTickerAndCompanyId(eq("TICKER"), anyLong())).thenReturn(myStock);

        // Izvršenje
        Offer acceptedOffer = banka4OtcService.acceptOffer(123L);

        // Provera
        assertNotNull(acceptedOffer);
        assertEquals(OfferStatus.ACCEPTED, acceptedOffer.getOfferStatus());
        assertEquals(50, myStock.getAmount());
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
        offer1.setPrice(100);
        myOffers.add(offer1);

        MyOffer offer2 = new MyOffer();
        offer2.setAmount(20);
        offer2.setTicker("XYZ");
        offer2.setPrice(200);
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
        // Priprema
        Long offerId = 1L;
        String ticker = "ABC";
        Long companyId = 1L;
        Integer amount = 10;

        MyOffer myOffer = new MyOffer();
        myOffer.setMyOfferId(offerId);
        myOffer.setOfferStatus(OfferStatus.PROCESSING);
        myOffer.setTicker(ticker);
        myOffer.setAmount(amount);

        when(myOfferRepository.findById(offerId)).thenReturn(Optional.of(myOffer));
        when(myStockRepository.findByTicker(ticker)).thenReturn(null);

        // Izvršenje
        MyOffer result = banka4OtcService.offerAccepted(offerId);

        // Provera
        assertNotNull(result);
        assertEquals(OfferStatus.ACCEPTED, result.getOfferStatus());
        verify(myOfferRepository, times(1)).findById(offerId);
        verify(myOfferRepository, times(1)).save(myOffer);

        MyStock expectedStock = new MyStock();
        expectedStock.setTicker(ticker);
        expectedStock.setCompanyId(companyId);
        expectedStock.setAmount(amount);
        expectedStock.setPrivateAmount(0);
        expectedStock.setPublicAmount(amount);
        expectedStock.setCurrencyMark("RSD");

        verify(myStockRepository, times(1)).save(expectedStock);
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
}