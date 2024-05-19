package rs.edu.raf.exchangeservice.bootstrap;

import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import rs.edu.raf.exchangeservice.domain.model.Actuary;
import rs.edu.raf.exchangeservice.domain.model.Exchange;
import rs.edu.raf.exchangeservice.domain.model.enums.OrderStatus;
import rs.edu.raf.exchangeservice.domain.model.enums.OrderType;
import rs.edu.raf.exchangeservice.domain.model.listing.*;
import rs.edu.raf.exchangeservice.domain.model.myListing.MyFuture;
import rs.edu.raf.exchangeservice.domain.model.order.FutureOrder;
import rs.edu.raf.exchangeservice.domain.model.order.FutureOrderSell;
import rs.edu.raf.exchangeservice.domain.model.order.StockOrder;
import rs.edu.raf.exchangeservice.domain.model.order.StockOrderSell;
import rs.edu.raf.exchangeservice.repository.ActuaryRepository;
import rs.edu.raf.exchangeservice.repository.ExchangeRepository;
import rs.edu.raf.exchangeservice.repository.listingRepository.*;
import rs.edu.raf.exchangeservice.repository.myListingRepository.MyFutureRepository;
import rs.edu.raf.exchangeservice.repository.orderRepository.FutureOrderRepository;
import rs.edu.raf.exchangeservice.repository.orderRepository.FutureOrderSellRepository;
import rs.edu.raf.exchangeservice.repository.orderRepository.StockOrderRepository;
import rs.edu.raf.exchangeservice.repository.orderRepository.StockOrderSellRepository;

import java.util.List;

@AllArgsConstructor
@Component
public class BootstrapData implements CommandLineRunner {
    private final ForexRepository forexRepository;
    private final FutureRepository futureRepository;
    private final OptionRepository optionRepository;
    private final StockRepository stockRepository;
    private final TickerRepository tickerRepository;
    private final MyFutureRepository myFutureRepository;
    private final FutureOrderRepository futureOrderRepository;
    private final FutureOrderSellRepository futureOrderSellRepository;
    private final StockOrderRepository stockOrderRepository;
    private final StockOrderSellRepository stockOrderSellRepository;
    private final ActuaryRepository actuaryRepository;
    private final ExchangeRepository exchangeRepository;

    @Override
    public void run(String... args) {

        Forex forex1 = new Forex();
        forex1.setBaseCurrency("USD");
        forex1.setQuoteCurrency("EUR");
        forex1.setConversionRate(0.85);
        forex1.setLastRefresh(1L);

        Forex forex2 = new Forex();
        forex2.setBaseCurrency("EUR");
        forex2.setQuoteCurrency("RSD");
        forex2.setConversionRate(0.75);
        forex2.setLastRefresh(1L);

        Forex forex3 = new Forex();
        forex3.setBaseCurrency("GBP");
        forex3.setQuoteCurrency("JPY");
        forex3.setConversionRate(150.25);
        forex3.setLastRefresh(1L);

        if (forexRepository.count() == 0) {
            loadForexData(List.of(forex1, forex2, forex3));
        }

        Future futureCrudeOil = new Future();
        futureCrudeOil.setContractName("CLN4");
        futureCrudeOil.setContractSize(1000);
        futureCrudeOil.setContractUnit("barrels");
        futureCrudeOil.setMaintenanceMargin(5000);
        futureCrudeOil.setType("energy");
        futureCrudeOil.setCurrencyMark("USD");

        Future futureOats = new Future();
        futureOats.setContractName("CLN4");
        futureOats.setContractSize(5000);
        futureOats.setContractUnit("bushels");
        futureOats.setMaintenanceMargin(460000);
        futureOats.setType("agricultural");
        futureOats.setCurrencyMark("RSD");

        if (futureRepository.count() == 0) {
            loadFutureData(List.of(futureOats, futureCrudeOil));
        }
        Option option1 = new Option();
        option1.setOptionId(1L);
        option1.setLastRefresh(System.currentTimeMillis());
        option1.setContractSymbol("AAPL210618C00125000");
        option1.setStockListing("AAPL");
        option1.setOptionType("CALL");
        option1.setStrikePrice(125.0);
        option1.setImpliedVolatility(0.25);
        option1.setOpenInterest(1000);
        option1.setSettlementDate(1623993600000L); // June 18, 2021
        option1.setPrice(5.25);
        option1.setAsk(5.30);
        option1.setBid(5.20);
        option1.setChange(0.05);
        option1.setVolume(1500);
        option1.setCurrencyMark("GBP");

        Option option2 = new Option();
        option2.setOptionId(2L);
        option2.setLastRefresh(System.currentTimeMillis());
        option2.setContractSymbol("GOOGL210618P00275000");
        option2.setStockListing("GOOGL");
        option2.setOptionType("PUT");
        option2.setStrikePrice(2750.0);
        option2.setImpliedVolatility(0.30);
        option2.setOpenInterest(800);
        option2.setSettlementDate(1623993600000L); // June 18, 2021
        option2.setPrice(12.50);
        option2.setAsk(12.55);
        option2.setBid(12.45);
        option2.setChange(-0.10);
        option2.setVolume(1000);
        option2.setCurrencyMark("USD");

        if (optionRepository.count() == 0) {
            loadOptionData(List.of(option2, option1));
        }

        Stock stockApple = new Stock();
        stockApple.setStockId(1L);
        stockApple.setName("Apple Inc.");
        stockApple.setExchange("NASDAQ");
        stockApple.setLastRefresh(System.currentTimeMillis());
        stockApple.setTicker("AAPL");
        stockApple.setPrice(150.0);
        stockApple.setAsk(151.0);
        stockApple.setBid(149.0);
        stockApple.setChange(1.5);
        stockApple.setVolume(5000000);
        stockApple.setCurrencyMark("USD");

        Stock stockAlpha = new Stock();
        stockAlpha.setStockId(2L);
        stockAlpha.setName("Alphabet Inc.");
        stockAlpha.setExchange("NASDAQ");
        stockAlpha.setLastRefresh(System.currentTimeMillis());
        stockAlpha.setTicker("GOOGL");
        stockAlpha.setPrice(2500.0);
        stockAlpha.setAsk(2505.0);
        stockAlpha.setBid(2495.0);
        stockAlpha.setChange(-15.0);
        stockAlpha.setVolume(2000000);
        stockAlpha.setCurrencyMark("RSD");

        if (stockRepository.count() == 0) {
            loadStockData(List.of(stockAlpha, stockApple));
        }
        Ticker ticker1 = new Ticker();
        ticker1.setTickerId(1L);
        ticker1.setTicker("AAPL");
        ticker1.setName("Apple Inc.");
        ticker1.setCurrencyName("USD");
        ticker1.setPrimaryExchange("NASDAQ");

        Ticker ticker2 = new Ticker();
        ticker2.setTickerId(2L);
        ticker2.setTicker("GOOGL");
        ticker2.setName("Alphabet Inc.");
        ticker2.setCurrencyName("USD");
        ticker2.setPrimaryExchange("NASDAQ");

        if (tickerRepository.count() == 0) {
            loadTickerData(List.of(ticker2, ticker1));
        }

        MyFuture mf = new MyFuture();
        mf.setContractName("CLN4");
        mf.setCompanyId(1L);
        mf.setAmount(100000);
        mf.setCurrencyMark("RSD");
        mf.setPrivateAmount(50000);
        mf.setPublicAmount(40000);

        if (myFutureRepository.count() == 0) {
            loadMyFutureData(List.of(mf));
        }

        FutureOrder futureOrder1 = new FutureOrder();
        futureOrder1.setFutureId(1L);
        futureOrder1.setEmployeeId(1L);
        futureOrder1.setPrice(23000.0);

        if (futureOrderRepository.count() == 0) {
            loadFutureOrderData(List.of(futureOrder1));
        }
        FutureOrderSell futureOrderSell = new FutureOrderSell();
        futureOrderSell.setFutureId(1L);
        futureOrderSell.setEmployeeId(1L);
        futureOrderSell.setPrice(2000.50);

        if (futureOrderSellRepository.count() == 0) {
            loadFutureOrderSellData(List.of(futureOrderSell));
        }

        StockOrder stockOrder1 = new StockOrder();
        stockOrder1.setEmployeeId(1001L);
        stockOrder1.setTicker("AAPL");
        stockOrder1.setStatus(OrderStatus.PROCESSING);
        stockOrder1.setType(OrderType.MARKET);
        stockOrder1.setLimitValue(150.0);
        stockOrder1.setStopValue(145.0);
        stockOrder1.setAmount(100);
        stockOrder1.setAmountLeft(50);
        stockOrder1.setAon(true);
        stockOrder1.setMargin(false);
        stockOrder1.setCurrencyMark("USD");

        if (stockOrderRepository.count() == 0) {
            loadStockOrderData(List.of(stockOrder1));
        }

        StockOrderSell orderSell1 = new StockOrderSell();
        orderSell1.setEmployeeId(1L);
        orderSell1.setTicker("AAPL");
        orderSell1.setStatus(OrderStatus.PROCESSING);
        orderSell1.setType(OrderType.MARKET);
        orderSell1.setLimitValue(150.0);
        orderSell1.setStopValue(145.0);
        orderSell1.setAmount(100);
        orderSell1.setAmountLeft(50);
        orderSell1.setAon(true);
        orderSell1.setMargin(false);

        StockOrderSell orderSell2 = new StockOrderSell();
        orderSell2.setEmployeeId(2L);
        orderSell2.setTicker("GOOGL");
        orderSell2.setStatus(OrderStatus.FAILED);
        orderSell2.setType(OrderType.STOP);
        orderSell2.setLimitValue(2500.0);
        orderSell2.setStopValue(2450.0);
        orderSell2.setAmount(50);
        orderSell2.setAmountLeft(50);
        orderSell2.setAon(true);
        orderSell2.setMargin(true);

        if (stockOrderSellRepository.count() == 0) {
            loadStockOrderSellData(List.of(orderSell1,orderSell2));
        }
        Actuary actuary1 = new Actuary();
        actuary1.setEmployeeId(1L);
        actuary1.setEmail("jana@gmail.com");
        actuary1.setRole("Senior Actuary");
        actuary1.setLimitValue(50000.0);
        actuary1.setLimitUsed(10000.0);
        actuary1.setOrderRequest(true);

        Actuary actuary2 = new Actuary();
        actuary2.setEmployeeId(2L);
        actuary2.setEmail("luka@yahoo.com");
        actuary2.setRole("Junior Actuary");
        actuary2.setLimitValue(30000.0);
        actuary2.setLimitUsed(5000.0);
        actuary2.setOrderRequest(false);

        if (actuaryRepository.count() == 0) {
            loadActuaryData(List.of(actuary1, actuary2));
        }

        Exchange exchange1 = new Exchange();
        exchange1.setExchangeName("NASDAQ Stock Market");
        exchange1.setExchangeAcronym("NASDAQ");
        exchange1.setExchange("NQ");
        exchange1.setCountry("USA");
        exchange1.setCurrency("USD");
        exchange1.setTimeZone("EST");
        exchange1.setOpenTime(930L);
        exchange1.setCloseTime(1600L);

        Exchange exchange2 = new Exchange();
        exchange2.setExchangeName("London Stock Exchange");
        exchange2.setExchangeAcronym("LSE");
        exchange2.setExchange("LN");
        exchange2.setCountry("UK");
        exchange2.setCurrency("GBP");
        exchange2.setTimeZone("GMT");
        exchange2.setOpenTime(800L);
        exchange2.setCloseTime(1630L);

        if (exchangeRepository.count() == 0) {
            loadExchangelData(List.of(exchange1, exchange2));
        }
    }
    private void loadForexData(List<Forex> forexes) {
        forexRepository.saveAll(forexes);
    }
    private void loadFutureData(List<Future> futures) {
        futureRepository.saveAll(futures);
    }
    private void loadOptionData(List<Option> options) {
        optionRepository.saveAll(options);
    }
    private void loadStockData(List<Stock> stocks) {
        stockRepository.saveAll(stocks);
    }
    private void loadTickerData(List<Ticker> tickers) {
        tickerRepository.saveAll(tickers);
    }
    private void loadMyFutureData(List<MyFuture> myFutures) {
        myFutureRepository.saveAll(myFutures);
    }
    private void loadFutureOrderData(List<FutureOrder> futureOrders) {
        futureOrderRepository.saveAll(futureOrders);
    }
    private void loadFutureOrderSellData(List<FutureOrderSell> futureOrderSells) {
        futureOrderSellRepository.saveAll(futureOrderSells);
    }
    private void loadStockOrderData(List<StockOrder> stockOrders) {
        stockOrderRepository.saveAll(stockOrders);
    }
    private void loadStockOrderSellData(List<StockOrderSell> stockOrderSells) {
        stockOrderSellRepository.saveAll(stockOrderSells);
    }
    private void loadActuaryData(List<Actuary> actuaries) {
        actuaryRepository.saveAll(actuaries);
    }
    private void loadExchangelData(List<Exchange> exchanges) {
        exchangeRepository.saveAll(exchanges);
    }
}
