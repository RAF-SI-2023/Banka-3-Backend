package rs.edu.raf.exchangeservice.service.myListingService;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import rs.edu.raf.exchangeservice.client.BankServiceClient;
import rs.edu.raf.exchangeservice.configuration.StockUpdateEvent;
import rs.edu.raf.exchangeservice.domain.dto.buySell.BuySellStockDto;
import rs.edu.raf.exchangeservice.domain.dto.bank.BankTransactionDto;
import rs.edu.raf.exchangeservice.domain.model.enums.OrderStatus;
import rs.edu.raf.exchangeservice.domain.model.enums.OrderType;
import rs.edu.raf.exchangeservice.domain.model.listing.Stock;
import rs.edu.raf.exchangeservice.domain.model.listing.Ticker;
import rs.edu.raf.exchangeservice.domain.model.myListing.MyStock;
import rs.edu.raf.exchangeservice.domain.model.order.StockOrderSell;
import rs.edu.raf.exchangeservice.repository.listingRepository.StockRepository;
import rs.edu.raf.exchangeservice.repository.listingRepository.TickerRepository;
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
    public CopyOnWriteArrayList<StockOrderSell> ordersToSell = new CopyOnWriteArrayList<>();

    public void loadData() {
        List<Ticker> tickersList = tickerRepository.findAll();

        for (Ticker ticker : tickersList) {
            MyStock myStock = new MyStock();
            myStock.setTicker(ticker.getTicker());
            myStock.setAmount(0);
            myStock.setCurrencyMark(ticker.getCurrencyName());
            myStockRepository.save(myStock);

        }
    }

    //na osnovu tickera pronalazi MyStock objekat u bazi
    //i povecava mu kolicinu za prosledjeni amount
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void addAmountToMyStock(String ticker, Integer amount) {
        MyStock myStock = myStockRepository.findByTicker(ticker);
        myStock.setAmount(myStock.getAmount() + amount);
        myStockRepository.save(myStock);
        eventPublisher.publishEvent(new StockUpdateEvent(this, myStock));
    }

    //vracamo sve deonice koje su u vlasnistvu banke
    public List<MyStock> getAll() {
        return myStockRepository.findAll();
    }

    //funkcija kada prodajemo Stock i dodajemo
    //ga u listu za prodaju
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void sellStock(BuySellStockDto sellStockDto) {
        MyStock myStock = myStockRepository.findByTicker(sellStockDto.getTicker());

        StockOrderSell stockOrderSell = new StockOrderSell();
        stockOrderSell.setEmployeeId(sellStockDto.getEmployeeId());
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

        if (stockOrderSell.getAmount() > myStock.getAmount()) {
            stockOrderSell.setStatus(OrderStatus.FAILED);
            stockOrderSellRepository.save(stockOrderSell);
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
    public void executeTask() {
        if (ordersToSell.isEmpty()) {
            System.out.println("Executing sell-task every 15 seconds");
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
                bankTransactionDto.setAmount(currentPrice * amountToSell);
                bankServiceClient.stockSellTransaction(bankTransactionDto);

                this.addAmountToMyStock(stockOrderSell.getTicker(), amountToSell * (-1));    //dodajemo kolicinu kupljenih deonica u vlasnistvo banke
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
                    bankTransactionDto.setAmount(currentPrice * amountToSell);
                    bankServiceClient.stockSellTransaction(bankTransactionDto);

                    this.addAmountToMyStock(stockOrderSell.getTicker(), amountToSell * (-1));    //dodajemo kolicinu kupljenih deonica u vlasnistvo banke
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
