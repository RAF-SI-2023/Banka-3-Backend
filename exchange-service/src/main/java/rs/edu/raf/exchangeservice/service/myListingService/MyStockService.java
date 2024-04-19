package rs.edu.raf.exchangeservice.service.myListingService;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import rs.edu.raf.exchangeservice.client.BankServiceClient;
import rs.edu.raf.exchangeservice.domain.dto.SellStockDto;
import rs.edu.raf.exchangeservice.domain.dto.StockTransactionDto;
import rs.edu.raf.exchangeservice.domain.model.enums.StockOrderStatus;
import rs.edu.raf.exchangeservice.domain.model.enums.StockOrderType;
import rs.edu.raf.exchangeservice.domain.model.listing.Stock;
import rs.edu.raf.exchangeservice.domain.model.listing.Ticker;
import rs.edu.raf.exchangeservice.domain.model.myListing.MyStock;
import rs.edu.raf.exchangeservice.domain.model.order.StockOrderSell;
import rs.edu.raf.exchangeservice.repository.listingRepository.StockRepository;
import rs.edu.raf.exchangeservice.repository.listingRepository.TickerRepository;
import rs.edu.raf.exchangeservice.repository.myListingRepository.MyStockRepository;
import rs.edu.raf.exchangeservice.repository.orderRepository.StockOrderSellRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@RequiredArgsConstructor
public class MyStockService {
    private final MyStockRepository myStockRepository;
    private final TickerRepository tickerRepository;
    private final StockRepository stockRepository;
    private final StockOrderSellRepository stockOrderSellRepository;
    private final BankServiceClient bankServiceClient;

    public CopyOnWriteArrayList<StockOrderSell> ordersToSell = new CopyOnWriteArrayList<>();

    public void loadData() {
        List<Ticker> tickersList = tickerRepository.findAll();

        for (Ticker ticker : tickersList){
            MyStock myStock = new MyStock();
            myStock.setTicker(ticker.getTicker());
            myStock.setAmount(0);
            myStockRepository.save(myStock);
        }
    }

    //na osnovu tickera pronalazi MyStock objekat u bazi
    //i povecava mu kolicinu za prosledjeni amount
    @Transactional
    public void addAmountToMyStock(String ticker, Integer amount) {
        MyStock myStock = myStockRepository.findByTicker(ticker);
        myStock.setAmount(myStock.getAmount() + amount);
        this.myStockRepository.save(myStock);
    }

    //vracamo sve deonice koje su u vlasnistvu banke
    public List<MyStock> getAll(){
        return this.myStockRepository.findAll();
    }

    //funkcija kada prodajemo Stock i dodajemo
    //ga u listu za prodaju
    public String sellStock(SellStockDto sellStockDto){
        MyStock myStock = myStockRepository.findByTicker(sellStockDto.getTicker());

        StockOrderSell stockOrderSell = new StockOrderSell();
        stockOrderSell.setEmployeeId(sellStockDto.getEmployeeId());
        stockOrderSell.setTicker(sellStockDto.getTicker());
        stockOrderSell.setAmount(sellStockDto.getAmount());
        stockOrderSell.setAmountLeft(sellStockDto.getAmount());
        stockOrderSell.setAon(sellStockDto.isAon());
        stockOrderSell.setMargin(sellStockDto.isMargin());

        //market order
        if (sellStockDto.getStopValue() == 0.0 && sellStockDto.getLimitValue() == 0.0){
            stockOrderSell.setLimitValue(0.0);
            stockOrderSell.setStopValue(0.0);
            stockOrderSell.setType(StockOrderType.MARKET);
        }

        //stop order
        if(sellStockDto.getStopValue() != 0.0 && sellStockDto.getLimitValue() == 0.0){
            stockOrderSell.setLimitValue(0.0);
            stockOrderSell.setStopValue(sellStockDto.getStopValue());
            stockOrderSell.setType(StockOrderType.STOP);
        }

        //limit order
        if(sellStockDto.getStopValue() == 0.0 && sellStockDto.getLimitValue() != 0.0){
            stockOrderSell.setLimitValue(sellStockDto.getLimitValue());
            stockOrderSell.setStopValue(0.0);
            stockOrderSell.setType(StockOrderType.LIMIT);
        }

        //stop-limit order
        if(sellStockDto.getStopValue() != 0.0 && sellStockDto.getLimitValue() != 0.0){
            stockOrderSell.setLimitValue(sellStockDto.getLimitValue());
            stockOrderSell.setStopValue(sellStockDto.getStopValue());
            stockOrderSell.setType(StockOrderType.STOP_LIMIT);
        }

        if (stockOrderSell.getAmount() > myStock.getAmount()){
            stockOrderSell.setStatus(StockOrderStatus.FAILED);
            stockOrderSellRepository.save(stockOrderSell);
            return "nije dobar amount";
        }

        stockOrderSell.setStatus(StockOrderStatus.PROCESSING);
        this.ordersToSell.add(stockOrderSellRepository.save(stockOrderSell));
        return "UBACENO U ORDER";
    }

    //zovemo funkciju svakih 20 sekundi
    //proveravamo da li je lista prazna, ako jeste ona nista
    //ako ima nesto, uzimamo random StockOrderSell u listi
    //proveravamo uslove za cenu i limit i stop
    //ako su dobri, prodajemo akciju i azuriramo MyStock u DB
    @Scheduled(fixedRate = 20000)
    public void executeTask() {
        if (ordersToSell.isEmpty()){
            System.out.println("Executing task every 20 seconds, but list to sell is empty :-(");
        }else {
            Random rand = new Random();
            int stockNumber = rand.nextInt(ordersToSell.size());
            StockOrderSell stockOrderSell = ordersToSell.get(stockNumber);   //StockOrder koji obradjujemo

            Stock stock = this.stockRepository.findByTicker(stockOrderSell.getTicker()).get();  //uzimao stock iz baze koji kupujemo
            Double currentPrice = stock.getBid();   //trenutna cena po kojoj kupujemo

            int amountToSell = rand.nextInt(stockOrderSell.getAmountLeft()) + 1;

            //provera ako je allOrNon true
            if (stockOrderSell.isAon()){
                if (amountToSell != stockOrderSell.getAmountLeft()){
                    System.out.println("Couldn't sell all " + stockOrderSell.getAmountLeft());
                    return;
                }
            }

            //kreirati stockTransactionDto koji sadrzi sracunatu kolicinu novca u zavisnosti od tipa stock-a,
            //broj racuna banke, broj racuna berze.
            StockTransactionDto stockTransactionDto = new StockTransactionDto();

            //TODO naci broj racuna banke i berze preko valute
            stockTransactionDto.setAccountFrom("3333333333333333");
            stockTransactionDto.setAccountTo("3030303030303030");
            stockTransactionDto.setCurrencyMark("USD");

            if (stockOrderSell.getType().equals(StockOrderType.MARKET)){
                stockTransactionDto.setAmount(currentPrice * amountToSell);
                bankServiceClient.startStockTransaction(stockTransactionDto);

                this.addAmountToMyStock(stockOrderSell.getTicker(), amountToSell*(-1));    //dodajemo kolicinu kupljenih deonica u vlasnistvo banke
                stockOrderSell.setAmountLeft(stockOrderSell.getAmountLeft() - amountToSell);
                if (stockOrderSell.getAmountLeft() <= 0){
                    stockOrderSell.setStatus(StockOrderStatus.FINISHED);
                    ordersToSell.remove(stockNumber);    //uklanjamo ga iz liste jer je zavrsio
                }else {
                    ordersToSell.remove(stockNumber);
                    ordersToSell.add(stockNumber,stockOrderSell);    //update Objekta u listi
                }
            }

            if (stockOrderSell.getType().equals(StockOrderType.LIMIT)){
                if (currentPrice > stockOrderSell.getLimitValue()){
                    stockTransactionDto.setAmount(currentPrice * amountToSell);
                    bankServiceClient.startStockTransaction(stockTransactionDto);

                    this.addAmountToMyStock(stockOrderSell.getTicker(), amountToSell*(-1));    //dodajemo kolicinu kupljenih deonica u vlasnistvo banke
                    stockOrderSell.setAmountLeft(stockOrderSell.getAmountLeft() - amountToSell);
                    if (stockOrderSell.getAmountLeft() <= 0){
                        stockOrderSell.setStatus(StockOrderStatus.FINISHED);
                        ordersToSell.remove(stockNumber);    //uklanjamo ga iz liste jer je zavrsio
                    }else {
                        ordersToSell.remove(stockNumber);
                        ordersToSell.add(stockNumber,stockOrderSell);    //update Objekta u listi
                    }
                }else {
                    stockOrderSell.setStatus(StockOrderStatus.FAILED);
                    ordersToSell.remove(stockNumber);
                }
            }

            if (stockOrderSell.getType().equals(StockOrderType.STOP)){
                if (currentPrice < stockOrderSell.getStopValue()){
                    stockOrderSell.setType(StockOrderType.MARKET);
                } else {
                    System.out.println("Stop value hasn't been approved ");
                }
            }

            if (stockOrderSell.getType().equals(StockOrderType.STOP_LIMIT)){
                if (currentPrice < stockOrderSell.getStopValue()){
                    stockOrderSell.setType(StockOrderType.LIMIT);
                } else {
                    System.out.println("Stop value hasn't been approved ");
                }
            }

            this.stockOrderSellRepository.save(stockOrderSell); //cuvamo promenjene vrednosti u bazi
        }
    }

    private void printer(int amountToBuy, double currentPrice){
        System.out.println("Prodato: " + amountToBuy + " STOCK, po ceni: " + currentPrice*amountToBuy);
    }
}
