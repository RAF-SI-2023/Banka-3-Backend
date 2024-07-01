package rs.edu.raf.exchangeservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.edu.raf.exchangeservice.client.BankServiceClient;
import rs.edu.raf.exchangeservice.domain.dto.bank.CompanyOtcDto;
import rs.edu.raf.exchangeservice.domain.dto.offer.MyStockDto;
import rs.edu.raf.exchangeservice.domain.dto.offer.OfferDto;
import rs.edu.raf.exchangeservice.domain.model.listing.BankOTCStock;
import rs.edu.raf.exchangeservice.domain.model.myListing.MyStock;
import rs.edu.raf.exchangeservice.domain.model.offer.MyOffer;
import rs.edu.raf.exchangeservice.domain.model.offer.Offer;
import rs.edu.raf.exchangeservice.domain.model.offer.OfferStatus;
import rs.edu.raf.exchangeservice.repository.listingRepository.BankOTCStockRepository;
import rs.edu.raf.exchangeservice.repository.listingRepository.TickerRepository;
import rs.edu.raf.exchangeservice.repository.myListingRepository.MyStockRepository;
import rs.edu.raf.exchangeservice.repository.offerRepository.MyOfferRepository;
import rs.edu.raf.exchangeservice.repository.offerRepository.OfferRepository;
import rs.edu.raf.exchangeservice.service.BankOtcService;
import rs.edu.raf.exchangeservice.service.listingService.TickerService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BankOtcServiceTest {
    @Mock
    private MyStockRepository myStockRepository;

    @Mock
    private BankOTCStockRepository bankOTCStockRepository;
    @Mock
    private MyOfferRepository myOfferRepository;

    @Mock
    private TickerRepository tickerRepository;

    @Mock
    private TickerService tickerService;
    @Mock
    private BankServiceClient bankServiceClient;
    @Mock
    private OfferRepository offerRepository;
    @InjectMocks
    private BankOtcService bankOtcService;

    private List<MyStock> myStocks;

    private OfferDto offerDto;
    private MyOffer myOffer;
    private List<Offer> offers;
    private List<MyOffer> myOffers;

    private List<BankOTCStock> bankOTCStocks;


    @BeforeEach
    void setUp() {
        MyStock stock1 = new MyStock();
        stock1.setTicker("AAPL");
        stock1.setPublicAmount(100);

        MyStock stock2 = new MyStock();
        stock2.setTicker("GOOGL");
        stock2.setPublicAmount(200);

        myStocks = Arrays.asList(stock1, stock2);

        offerDto = new OfferDto();
        offerDto.setTicker("AAPL");
        offerDto.setAmount(50);
        offerDto.setPrice(150.0);
        offerDto.setIdBank(1L);

        myOffer = new MyOffer();
        myOffer.setMyOfferId(1L);
        myOffer.setTicker("AAPL");
        myOffer.setAmount(100);
        myOffer.setPrice(15000.0);
        myOffer.setOfferStatus(OfferStatus.PROCESSING);

        BankOTCStock bankOTCStocks1 = new BankOTCStock();
        bankOTCStocks1.setId(1L);
        bankOTCStocks1.setTicker("AAPL");
        bankOTCStocks1.setAmount(100);

        BankOTCStock bankOTCStocks2 = new BankOTCStock();
        bankOTCStocks2.setId(2L);
        bankOTCStocks2.setTicker("GOOGL");
        bankOTCStocks2.setAmount(200);

        bankOTCStocks = Arrays.asList(bankOTCStocks1, bankOTCStocks2);

        Offer offer1 = new Offer();
        offer1.setOfferId(1L);
        offer1.setTicker("AAPL");
        offer1.setAmount(100);
        offer1.setPrice(15000.0);
        offer1.setOfferStatus(OfferStatus.PROCESSING);

        Offer offer2 = new Offer();
        offer2.setOfferId(2L);
        offer2.setTicker("GOOGL");
        offer2.setAmount(200);
        offer2.setPrice(30000.0);
        offer2.setOfferStatus(OfferStatus.PROCESSING);

        offers = Arrays.asList(offer1, offer2);

        MyOffer offer3 = new MyOffer();
        offer3.setMyOfferId(1L);
        offer3.setTicker("AAPL");
        offer3.setAmount(100);
        offer3.setPrice(15000.0);
        offer3.setOfferStatus(OfferStatus.PROCESSING);

        MyOffer offer4 = new MyOffer();
        offer4.setMyOfferId(2L);
        offer4.setTicker("GOOGL");
        offer4.setAmount(200);
        offer4.setPrice(30000.0);
        offer4.setOfferStatus(OfferStatus.PROCESSING);

        myOffers = Arrays.asList(offer3, offer4);
    }

    @Test
    void testFindAllStocks() {
        when(myStockRepository.findAllByCompanyIdAndPublicAmountGreaterThan(1L, 0)).thenReturn(myStocks);

        List<MyStockDto> dtos = bankOtcService.findAllStocks();

        assertEquals(2, dtos.size());

        MyStockDto dto1 = dtos.get(0);
        assertEquals("AAPL", dto1.getTicker());
        assertEquals(100, dto1.getAmount());

        MyStockDto dto2 = dtos.get(1);
        assertEquals("GOOGL", dto2.getTicker());
        assertEquals(200, dto2.getAmount());
    }

    @Test
    void testReceiveOffer_Processing() {
        MyStock myStock = new MyStock();
        myStock.setTicker("AAPL");
        myStock.setCompanyId(1L);
        myStock.setPublicAmount(100);

        when(myStockRepository.findByTickerAndCompanyId("AAPL", 1L)).thenReturn(myStock);

        Offer offer = bankOtcService.receiveOffer(offerDto, 1);

        assertEquals(OfferStatus.PROCESSING, offer.getOfferStatus());
        verify(offerRepository, times(1)).save(any(Offer.class));
    }

    @Test
    void testReceiveOffer_Declined_NotEnoughStock() {
        MyStock myStock = new MyStock();
        myStock.setTicker("AAPL");
        myStock.setCompanyId(1L);
        myStock.setPublicAmount(30);

        when(myStockRepository.findByTickerAndCompanyId("AAPL", 1L)).thenReturn(myStock);

        Offer offer = bankOtcService.receiveOffer(offerDto, 1);

        assertEquals(OfferStatus.DECLINED, offer.getOfferStatus());
        verify(offerRepository, times(1)).save(any(Offer.class));
    }

    @Test
    void testReceiveOffer_Declined_NoStock() {
        when(myStockRepository.findByTickerAndCompanyId("AAPL", 1L)).thenReturn(null);

        Offer offer = bankOtcService.receiveOffer(offerDto, 1);

        assertEquals(OfferStatus.DECLINED, offer.getOfferStatus());
        verify(offerRepository, times(1)).save(any(Offer.class));
    }
    @Test
    void testOfferAccepted_NewStock() {
        when(myOfferRepository.findById(1L)).thenReturn(Optional.of(myOffer));
        when(myStockRepository.findByTicker("AAPL")).thenReturn(null);
        when(tickerRepository.findByTicker("AAPL")).thenReturn(null);

        boolean result = bankOtcService.offerAccepted(1L);

        assertTrue(result);
        verify(myOfferRepository, times(1)).save(myOffer);
        verify(myStockRepository, times(1)).save(any(MyStock.class));
        verify(tickerService, times(1)).addTicker("AAPL");
        verify(bankServiceClient, times(1)).otcBank4transaction(any(CompanyOtcDto.class));
    }


    @Test
    void testOfferAccepted_OfferNotFound() {
        when(myOfferRepository.findById(1L)).thenReturn(Optional.empty());

        boolean result = bankOtcService.offerAccepted(1L);

        assertFalse(result);
        verify(myOfferRepository, never()).save(any(MyOffer.class));
        verify(myStockRepository, never()).save(any(MyStock.class));
        verify(bankServiceClient, never()).otcBank4transaction(any(CompanyOtcDto.class));
    }

    @Test
    void testOfferDeclined_OfferFound() {
        when(myOfferRepository.findById(1L)).thenReturn(Optional.of(myOffer));

        boolean result = bankOtcService.offerDeclined(1L);

        assertTrue(result);
        verify(myOfferRepository, times(1)).save(myOffer);
        assertEquals(OfferStatus.DECLINED, myOffer.getOfferStatus());
    }

    @Test
    void testOfferDeclined_OfferNotFound() {
        when(myOfferRepository.findById(1L)).thenReturn(Optional.empty());

        boolean result = bankOtcService.offerDeclined(1L);

        assertFalse(result);
        verify(myOfferRepository, never()).save(any(MyOffer.class));
    }

    @Test
    void testGetAllStocksForBanks() {
        when(bankOTCStockRepository.findAll()).thenReturn(bankOTCStocks);

        List<BankOTCStock> result = bankOtcService.getAllStocksForBanks();

        assertEquals(2, result.size());

        BankOTCStock stock1 = result.get(0);
        assertEquals("AAPL", stock1.getTicker());
        assertEquals(100, stock1.getAmount());

        BankOTCStock stock2 = result.get(1);
        assertEquals("GOOGL", stock2.getTicker());
        assertEquals(200, stock2.getAmount());
    }

    @Test
    void testFindAllOffers() {
        when(offerRepository.findAll()).thenReturn(offers);

        List<Offer> result = bankOtcService.findAllOffers();

        assertEquals(2, result.size());

        Offer offer1 = result.get(0);
        assertEquals(1L, offer1.getOfferId());
        assertEquals("AAPL", offer1.getTicker());
        assertEquals(100, offer1.getAmount());
        assertEquals(15000.0, offer1.getPrice());
        assertEquals(OfferStatus.PROCESSING, offer1.getOfferStatus());

        Offer offer2 = result.get(1);
        assertEquals(2L, offer2.getOfferId());
        assertEquals("GOOGL", offer2.getTicker());
        assertEquals(200, offer2.getAmount());
        assertEquals(30000.0, offer2.getPrice());
        assertEquals(OfferStatus.PROCESSING, offer2.getOfferStatus());
    }

    @Test
    void testGetMyOffers() {
        when(myOfferRepository.findAll()).thenReturn(myOffers);

        List<MyOffer> result = bankOtcService.getMyOffers();

        assertEquals(2, result.size());

        MyOffer offer1 = result.get(0);
        assertEquals(1L, offer1.getMyOfferId());
        assertEquals("AAPL", offer1.getTicker());
        assertEquals(100, offer1.getAmount());
        assertEquals(15000.0, offer1.getPrice());
        assertEquals(OfferStatus.PROCESSING, offer1.getOfferStatus());

        MyOffer offer2 = result.get(1);
        assertEquals(2L, offer2.getMyOfferId());
        assertEquals("GOOGL", offer2.getTicker());
        assertEquals(200, offer2.getAmount());
        assertEquals(30000.0, offer2.getPrice());
        assertEquals(OfferStatus.PROCESSING, offer2.getOfferStatus());
    }
}