package rs.edu.raf.exchangeservice.service.myListingService;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import rs.edu.raf.exchangeservice.client.BankServiceClient;
import rs.edu.raf.exchangeservice.configuration.StockUpdateEvent;
import rs.edu.raf.exchangeservice.domain.dto.MakePublicStockDto;
import rs.edu.raf.exchangeservice.domain.dto.bank.BankMarginTransactionDto;
import rs.edu.raf.exchangeservice.domain.dto.buySell.BuySellStockDto;
import rs.edu.raf.exchangeservice.domain.dto.bank.BankTransactionDto;
import rs.edu.raf.exchangeservice.domain.model.ProfitStock;
import rs.edu.raf.exchangeservice.domain.model.TaxStock;
import rs.edu.raf.exchangeservice.domain.model.enums.OrderStatus;
import rs.edu.raf.exchangeservice.domain.model.enums.OrderType;
import rs.edu.raf.exchangeservice.domain.model.listing.Stock;
import rs.edu.raf.exchangeservice.domain.model.listing.Ticker;
import rs.edu.raf.exchangeservice.domain.model.myListing.MyMarginStock;
import rs.edu.raf.exchangeservice.domain.model.myListing.MyStock;
import rs.edu.raf.exchangeservice.domain.model.order.StockOrderSell;
import rs.edu.raf.exchangeservice.jacoco.ExcludeFromJacocoGeneratedReport;
import rs.edu.raf.exchangeservice.repository.ProfitStockRepositorty;
import rs.edu.raf.exchangeservice.repository.TaxStockRepository;
import rs.edu.raf.exchangeservice.repository.listingRepository.StockRepository;
import rs.edu.raf.exchangeservice.repository.listingRepository.TickerRepository;
import rs.edu.raf.exchangeservice.repository.myListingRepository.MyMarginStockRepository;
import rs.edu.raf.exchangeservice.repository.myListingRepository.MyStockRepository;
import rs.edu.raf.exchangeservice.repository.orderRepository.StockOrderSellRepository;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@RequiredArgsConstructor
public class MyStockService {
    private final MyStockRepository myStockRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final TickerRepository tickerRepository;
    private final StockRepository stockRepository;
    private final StockOrderSellRepository stockOrderSellRepository;
    private final BankServiceClient bankServiceClient;
    private final TaxStockRepository taxStockRepository;
    private final ProfitStockRepositorty profitStockRepositorty;
    private final MyMarginStockService myMarginStockService;
    private final MyMarginStockRepository myMarginStockRepository;

    public CopyOnWriteArrayList<StockOrderSell> ordersToSell = new CopyOnWriteArrayList<>();

    public void addAmountToMyStock(String ticker, Integer amount, Long userId, Long companyId, Double minimumPrice) {
       if(userId != null){
           MyStock myStock = myStockRepository.findByTickerAndUserId(ticker, userId);
           if(myStock == null){
               Ticker ticker1 = tickerRepository.findByTicker(ticker);
               myStock = new MyStock();
               myStock.setTicker(ticker);
               myStock.setUserId(userId);
               myStock.setAmount(amount);
               myStock.setCurrencyMark(ticker1.getCurrencyName());
               myStock.setPrivateAmount(amount);
               myStock.setPublicAmount(0);
               myStock.setMinimumPrice(minimumPrice);
               myStockRepository.save(myStock);
               eventPublisher.publishEvent(new StockUpdateEvent(this, myStock));
           } else {
               if(myStock.getMinimumPrice() > minimumPrice){
                   myStock.setMinimumPrice(minimumPrice);
               }
               myStock.setAmount(myStock.getAmount() + amount);
               myStock.setPrivateAmount(myStock.getPrivateAmount() + amount);
               myStockRepository.save(myStock);
               eventPublisher.publishEvent(new StockUpdateEvent(this, myStock));
           }
       }else if(companyId != null){
           MyStock myStock = myStockRepository.findByTickerAndCompanyId(ticker, companyId);
           if(myStock == null){
               Ticker ticker1 = tickerRepository.findByTicker(ticker);
               myStock = new MyStock();
               myStock.setTicker(ticker);
               myStock.setCompanyId(companyId);
               myStock.setAmount(amount);
               myStock.setCurrencyMark(ticker1.getCurrencyName());
               myStock.setPrivateAmount(amount);
               myStock.setPublicAmount(0);
               myStock.setMinimumPrice(minimumPrice);
               myStockRepository.save(myStock);
               eventPublisher.publishEvent(new StockUpdateEvent(this, myStock));
           } else {
                if(myStock.getMinimumPrice() > minimumPrice){
                     myStock.setMinimumPrice(minimumPrice);
                }
               myStock.setAmount(myStock.getAmount() + amount);
               myStock.setPrivateAmount(myStock.getPrivateAmount() + amount);
               myStockRepository.save(myStock);
               eventPublisher.publishEvent(new StockUpdateEvent(this, myStock));
           }
       }
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void removeAmountFromMyStock(String ticker, Integer amount, Long userId, Long companyId) {
        if(userId != null){
            MyStock myStock = myStockRepository.findByTickerAndUserId(ticker, userId);
            for(int i = amount; i > 0; i--){
                if(myStock.getPublicAmount() > 0){
                    myStock.setPublicAmount(myStock.getPublicAmount() - 1);
                } else {
                    myStock.setPrivateAmount(myStock.getPrivateAmount() - 1);
                }
                myStock.setAmount(myStock.getAmount() - 1);
            }
            if(myStock.getAmount() == 0){
                myStockRepository.delete(myStock);
            }else {
                myStockRepository.save(myStock);
            }
            eventPublisher.publishEvent(new StockUpdateEvent(this, myStock));
        }else if(companyId != null){
            MyStock myStock = myStockRepository.findByTickerAndCompanyId(ticker, companyId);
            for(int i = amount; i > 0; i--){
                if(myStock.getPublicAmount() > 0){
                    myStock.setPublicAmount(myStock.getPublicAmount() - 1);
                } else {
                    myStock.setPrivateAmount(myStock.getPrivateAmount() - 1);
                }
                myStock.setAmount(myStock.getAmount() - 1);
            }
            if(myStock.getAmount() == 0){
                myStockRepository.delete(myStock);
            }else {
                myStockRepository.save(myStock);
            }
            eventPublisher.publishEvent(new StockUpdateEvent(this, myStock));
        }
    }

    public Double calculateTaxForSellStock(Long companyId, Long userId, String ticker, Integer sellAmount, Double sellPrice){
        MyStock myStock = null;
        if(userId != null){
            myStock = myStockRepository.findByTickerAndUserId(ticker, userId);
        }else if(companyId != null){
            myStock = myStockRepository.findByTickerAndCompanyId(ticker, companyId);
        }
        Double minimumPrice = myStock.getMinimumPrice()*sellAmount;
        Double priceForSell = sellPrice*sellAmount;

        Double tax = 0.0;
        if(priceForSell > minimumPrice){
            tax = (priceForSell - minimumPrice) * 0.15;
            TaxStock taxStock = new TaxStock();
            taxStock.setAmount(tax);
            taxStock.setDate(System.currentTimeMillis());
            taxStockRepository.save(taxStock);
        }
        return tax;
    }

    public void addProfitForEmployee(Long employeeId, Double amount){
        ProfitStock profitStock = new ProfitStock();
        profitStock.setEmployeeId(employeeId);
        profitStock.setAmount(amount);
        profitStockRepositorty.save(profitStock);
    }
    public MyStock makeCompanyStockPublic(MakePublicStockDto makePublicStockDto) {
        MyStock myStock = myStockRepository.findByTickerAndCompanyId(makePublicStockDto.getTicker(), makePublicStockDto.getOwnerId());
        if(myStock.getAmount() >= makePublicStockDto.getAmount() && makePublicStockDto.getAmount() >= 0){
            myStock.setPublicAmount(makePublicStockDto.getAmount());
            myStock.setPrivateAmount(myStock.getAmount() - makePublicStockDto.getAmount());
            myStockRepository.save(myStock);
            eventPublisher.publishEvent(new StockUpdateEvent(this, myStock));
        }
        return myStock;
    }
    public MyStock makeUserStockPublic(MakePublicStockDto makePublicStockDto) {
        MyStock myStock = myStockRepository.findByTickerAndUserId(makePublicStockDto.getTicker(), makePublicStockDto.getOwnerId());
        if(myStock.getAmount() >= makePublicStockDto.getAmount() && makePublicStockDto.getAmount() >= 0){
            myStock.setPublicAmount(makePublicStockDto.getAmount());
            myStock.setPrivateAmount(myStock.getAmount() - makePublicStockDto.getAmount());
            myStockRepository.save(myStock);
            eventPublisher.publishEvent(new StockUpdateEvent(this, myStock));
        }
        return myStock;
    }

    //vracamo sve deonice koje su u vlasnistvu banke
    public List<MyStock> getAll() {
        return myStockRepository.findAll();
    }

    public List<MyStock> getAllForCompany(Long companyId) {
        return myStockRepository.findAllByCompanyId(companyId);
    }

    public List<MyStock> getAllForUser(Long userId) {
        return myStockRepository.findAllByUserId(userId);
    }

    public List<MyStock> getAllForUserOtcBuy(Long userId) {
        return myStockRepository.findByUserIdIsNotNullAndCompanyIdIsNullAndPublicAmountGreaterThanAndUserIdNot(0, userId);
    }

    public List<MyStock> getAllForCompanyOtcBuy(Long companyId) {
        return myStockRepository.findByCompanyIdIsNotNullAndUserIdIsNullAndPublicAmountGreaterThanAndCompanyIdNot(0, companyId);
    }


    //funkcija kada prodajemo Stock i dodajemo
    //ga u listu za prodaju
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void sellStock(BuySellStockDto sellStockDto) {


        //MyStock myStock = myStockRepository.findByTicker(sellStockDto.getTicker());

        MyStock myStock = null;
        MyMarginStock myMarginStock = null;

        if(sellStockDto.isMargin()) {
            if(sellStockDto.getUserId() != null)
                myMarginStock = myMarginStockRepository.findByTickerAndUserId(sellStockDto.getTicker(), sellStockDto.getUserId());
            else
                myMarginStock = myMarginStockRepository.findByTickerAndCompanyId(sellStockDto.getTicker(), sellStockDto.getCompanyId());
        }else {
            if(sellStockDto.getUserId() != null)
                myStock = myStockRepository.findByTickerAndUserId(sellStockDto.getTicker(), sellStockDto.getUserId());
            else
                myStock = myStockRepository.findByTickerAndCompanyId(sellStockDto.getTicker(), sellStockDto.getCompanyId());
        }
        StockOrderSell stockOrderSell = new StockOrderSell();
        stockOrderSell.setEmployeeId(sellStockDto.getEmployeeId());
        stockOrderSell.setUserId(sellStockDto.getUserId());
        stockOrderSell.setCompanyId(sellStockDto.getCompanyId());
        stockOrderSell.setTicker(sellStockDto.getTicker());
        stockOrderSell.setAmount(sellStockDto.getAmount());
        stockOrderSell.setAmountLeft(sellStockDto.getAmount());
        stockOrderSell.setAon(sellStockDto.isAon());
        stockOrderSell.setMargin(sellStockDto.isMargin());

        //market order
        if (sellStockDto.getStopValue() == 0.0 && sellStockDto.getLimitValue() == 0.0) {
            stockOrderSell.setLimitValue(0.0);
            stockOrderSell.setStopValue(0.0);
            stockOrderSell.setType(OrderType.MARKET);
        }

        //stop order
        if (sellStockDto.getStopValue() != 0.0 && sellStockDto.getLimitValue() == 0.0) {
            stockOrderSell.setLimitValue(0.0);
            stockOrderSell.setStopValue(sellStockDto.getStopValue());
            stockOrderSell.setType(OrderType.STOP);
        }

        //limit order
        if (sellStockDto.getStopValue() == 0.0 && sellStockDto.getLimitValue() != 0.0) {
            stockOrderSell.setLimitValue(sellStockDto.getLimitValue());
            stockOrderSell.setStopValue(0.0);
            stockOrderSell.setType(OrderType.LIMIT);
        }

        //stop-limit order
        if (sellStockDto.getStopValue() != 0.0 && sellStockDto.getLimitValue() != 0.0) {
            stockOrderSell.setLimitValue(sellStockDto.getLimitValue());
            stockOrderSell.setStopValue(sellStockDto.getStopValue());
            stockOrderSell.setType(OrderType.STOP_LIMIT);
        }

        if(sellStockDto.isMargin()){
            if (stockOrderSell.getAmount() > myMarginStock.getAmount()) {
                stockOrderSell.setStatus(OrderStatus.FAILED);
                stockOrderSellRepository.save(stockOrderSell);
            }
        }else {
            if (stockOrderSell.getAmount() > myStock.getAmount()) {
                stockOrderSell.setStatus(OrderStatus.FAILED);
                stockOrderSellRepository.save(stockOrderSell);
            }
        }
        stockOrderSell.setStatus(OrderStatus.PROCESSING);
        ordersToSell.add(stockOrderSellRepository.save(stockOrderSell));
    }

    //zovemo funkciju svakih 20 sekundi
    //proveravamo da li je lista prazna, ako jeste ona nista
    //ako ima nesto, uzimamo random StockOrderSell u listi
    //proveravamo uslove za cenu i limit i stop
    //ako su dobri, prodajemo akciju i azuriramo MyStock u DB
    @Scheduled(fixedRate = 15000)
    @ExcludeFromJacocoGeneratedReport
    public void executeTask() {
        if (ordersToSell.isEmpty()) {
        } else {
            Random rand = new Random();
            int stockNumber = rand.nextInt(ordersToSell.size());
            StockOrderSell stockOrderSell = ordersToSell.get(stockNumber);   //StockOrder koji obradjujemo

                Stock stock = stockRepository.findByTicker(stockOrderSell.getTicker()).get();  //uzimao stock iz baze koji kupujemo
            double currentPrice = stock.getBid();   //trenutna cena po kojoj prodajemo
            int amountToSell = rand.nextInt(stockOrderSell.getAmountLeft()) + 1;

            if (stockOrderSell.isAon()) {
                amountToSell = stockOrderSell.getAmount();
            }


            //kreirati stockTransactionDto koji sadrzi sracunatu kolicinu novca u zavisnosti od tipa stock-a,
            //broj racuna banke, broj racuna berze.
            BankTransactionDto bankTransactionDto = new BankTransactionDto();
            bankTransactionDto.setCurrencyMark(stock.getCurrencyMark());

            if (stockOrderSell.getType().equals(OrderType.MARKET)) {
                if(!stockOrderSell.isMargin()) {
                    double tax = this.calculateTaxForSellStock(stockOrderSell.getCompanyId(), stockOrderSell.getUserId(), stockOrderSell.getTicker(), amountToSell, currentPrice);

                    //TODO: izracunaj porez pa setuj amount
                    bankTransactionDto.setAmount(currentPrice * amountToSell);
                    bankTransactionDto.setEmployeeId(stockOrderSell.getEmployeeId());
                    bankTransactionDto.setUserId(stockOrderSell.getUserId());
                    bankTransactionDto.setCompanyId(stockOrderSell.getCompanyId());
                    bankTransactionDto.setTax(tax);
                    bankServiceClient.stockSellTransaction(bankTransactionDto);

                    //dodajemo agentu amount koji je zaradio
                    if (stockOrderSell.getEmployeeId() != null) {
                        this.addProfitForEmployee(stockOrderSell.getEmployeeId(), currentPrice * amountToSell);
                    }

                    this.removeAmountFromMyStock(stockOrderSell.getTicker(), amountToSell, stockOrderSell.getUserId(), stockOrderSell.getCompanyId());    //dodajemo kolicinu kupljenih deonica u vlasnistvo banke
                }else {
                    BankMarginTransactionDto bankMarginTransactionDto = new BankMarginTransactionDto();
                    bankMarginTransactionDto.setAmount(currentPrice * amountToSell);
                    bankMarginTransactionDto.setUserId(stockOrderSell.getUserId());
                    bankMarginTransactionDto.setCompanyId(stockOrderSell.getCompanyId());
                    //todo otkomentarisati kada se implementira
                    //bankServiceClient.marginStockSellTransaction(bankMarginTransactionDto);
                    myMarginStockService.removeAmountFromMyMarginStock(stockOrderSell.getTicker(), amountToSell, stockOrderSell.getUserId(), stockOrderSell.getCompanyId());
                }

                stockOrderSell.setAmountLeft(stockOrderSell.getAmountLeft() - amountToSell);
                if (stockOrderSell.getAmountLeft() <= 0) {
                    stockOrderSell.setStatus(OrderStatus.FINISHED);
                    ordersToSell.remove(stockNumber);    //uklanjamo ga iz liste jer je zavrsio
                } else {
                    ordersToSell.remove(stockNumber);
                    ordersToSell.add(stockNumber, stockOrderSell);    //update Objekta u listi
                }
            }

            if (stockOrderSell.getType().equals(OrderType.LIMIT)) {
                if (currentPrice > stockOrderSell.getLimitValue()) {
                    if(!stockOrderSell.isMargin()) {
                        // racunamo porez na prodati stock
                        double tax = this.calculateTaxForSellStock(stockOrderSell.getCompanyId(), stockOrderSell.getUserId(), stockOrderSell.getTicker(), amountToSell, currentPrice);

                        bankTransactionDto.setAmount(currentPrice * amountToSell);
                        bankTransactionDto.setEmployeeId(stockOrderSell.getEmployeeId());
                        bankTransactionDto.setUserId(stockOrderSell.getUserId());
                        bankTransactionDto.setCompanyId(stockOrderSell.getCompanyId());
                        bankTransactionDto.setTax(tax);
                        bankServiceClient.stockSellTransaction(bankTransactionDto);

                        //dodajemo agentu amount koji je zaradio
                        if (stockOrderSell.getEmployeeId() != null) {
                            this.addProfitForEmployee(stockOrderSell.getEmployeeId(), currentPrice * amountToSell);
                        }

                        this.removeAmountFromMyStock(stockOrderSell.getTicker(), amountToSell, stockOrderSell.getUserId(), stockOrderSell.getCompanyId());    //dodajemo kolicinu kupljenih deonica u vlasnistvo banke
                    }else {
                        BankMarginTransactionDto bankMarginTransactionDto = new BankMarginTransactionDto();
                        bankMarginTransactionDto.setAmount(currentPrice * amountToSell);
                        bankMarginTransactionDto.setUserId(stockOrderSell.getUserId());
                        bankMarginTransactionDto.setCompanyId(stockOrderSell.getCompanyId());
                        //todo otkomentarisati kada se implementira
                        //bankServiceClient.marginStockSellTransaction(bankMarginTransactionDto);
                        myMarginStockService.removeAmountFromMyMarginStock(stockOrderSell.getTicker(), amountToSell, stockOrderSell.getUserId(), stockOrderSell.getCompanyId());
                    }
                    stockOrderSell.setAmountLeft(stockOrderSell.getAmountLeft() - amountToSell);
                    if (stockOrderSell.getAmountLeft() <= 0) {
                        stockOrderSell.setStatus(OrderStatus.FINISHED);
                        ordersToSell.remove(stockNumber);    //uklanjamo ga iz liste jer je zavrsio
                    } else {
                        ordersToSell.remove(stockNumber);
                        ordersToSell.add(stockNumber, stockOrderSell);    //update Objekta u listi
                    }
                } else {
                    stockOrderSell.setStatus(OrderStatus.FAILED);
                    ordersToSell.remove(stockNumber);
                }
            }

            if (stockOrderSell.getType().equals(OrderType.STOP)) {
                if (currentPrice < stockOrderSell.getStopValue()) {
                    stockOrderSell.setType(OrderType.MARKET);
                }

                if (stockOrderSell.getType().equals(OrderType.STOP_LIMIT)) {
                    if (currentPrice < stockOrderSell.getStopValue()) {
                        stockOrderSell.setType(OrderType.LIMIT);
                    }

                }
            }
           this.stockOrderSellRepository.save(stockOrderSell); //cuvamo promenjene vrednosti u bazi
        }
    }
}

