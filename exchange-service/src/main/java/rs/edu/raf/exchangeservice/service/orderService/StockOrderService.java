package rs.edu.raf.exchangeservice.service.orderService;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import rs.edu.raf.exchangeservice.client.BankServiceClient;
import rs.edu.raf.exchangeservice.domain.dto.BuyStockDto;
import rs.edu.raf.exchangeservice.domain.dto.StockOrderDto;
import rs.edu.raf.exchangeservice.domain.dto.StockTransactionDto;
import rs.edu.raf.exchangeservice.domain.mappers.StockMapper;
import rs.edu.raf.exchangeservice.domain.model.enums.OrderStatus;
import rs.edu.raf.exchangeservice.domain.model.enums.OrderType;
import rs.edu.raf.exchangeservice.domain.model.listing.Stock;
import rs.edu.raf.exchangeservice.domain.model.order.StockOrder;
import rs.edu.raf.exchangeservice.repository.ActuaryRepository;
import rs.edu.raf.exchangeservice.repository.listingRepository.StockRepository;
import rs.edu.raf.exchangeservice.repository.orderRepository.StockOrderRepository;
import rs.edu.raf.exchangeservice.service.myListingService.MyStockService;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@RequiredArgsConstructor
public class StockOrderService {
    private final StockOrderRepository stockOrderRepository;
    private final StockRepository stockRepository;
    private final ActuaryRepository actuaryRepository;
    private final MyStockService myStockService;
    private final BankServiceClient bankServiceClient;

    public CopyOnWriteArrayList<StockOrder> ordersToBuy = new CopyOnWriteArrayList<>();
    public CopyOnWriteArrayList<StockOrder> ordersToApprove = new CopyOnWriteArrayList<>();

    //vraca sve stockOrder
    public List<StockOrder> findAll(){
        return stockOrderRepository.findAll();
    }

    //vraca stockOrder od odredjeno zaposlenog
    public List<StockOrder> findAllByEmployee(Long id){
        return stockOrderRepository.findByEmployeeId(id);
    }

    //vracamo sve ordere koje treba approve-ovati
    public List<StockOrder> getAllOrdersToApprove(){
        return this.ordersToApprove;
    }

    //odobravamo ili ne odobravamo StockOrder Agentu
    public StockOrder approveStockOrder(Long id, boolean approved){
        StockOrder stockOrder = stockOrderRepository.findByStockOrderId(id);
        if (approved){
            stockOrder.setStatus(OrderStatus.PROCESSING);
            this.ordersToApprove.remove(stockOrder);
            this.ordersToBuy.add(stockOrder);
        }else {
            stockOrder.setStatus(OrderStatus.REJECTED);
            this.ordersToApprove.remove(stockOrder);
        }
        return this.stockOrderRepository.save(stockOrder);
    }

    //od buyOrderDto pravimo StockOrder i
    //dodajemo order u listu
    public StockOrderDto buyStock(BuyStockDto buyStockDto) {

        StockOrder stockOrder = new StockOrder();
        stockOrder.setEmployeeId(buyStockDto.getEmployeeId());
        stockOrder.setTicker(buyStockDto.getTicker());
        stockOrder.setAmount(buyStockDto.getAmount());
        stockOrder.setAmountLeft(buyStockDto.getAmount());
        stockOrder.setAon(buyStockDto.isAon());
        stockOrder.setMargin(buyStockDto.isMargin());

        if (actuaryRepository.findByEmployeeId(buyStockDto.getEmployeeId()).isOrderRequest()){
            stockOrder.setStatus(OrderStatus.WAITING);
        }else {
            stockOrder.setStatus(OrderStatus.PROCESSING);
        }

        if(buyStockDto.getStopValue() == null)
            stockOrder.setStopValue(0.0);
        else
            stockOrder.setStopValue(buyStockDto.getStopValue());

        if(buyStockDto.getLimitValue() == null)
            stockOrder.setLimitValue(0.0);
        else
            stockOrder.setLimitValue(buyStockDto.getLimitValue());

        //market order
        if (stockOrder.getStopValue() == 0.0 && stockOrder.getLimitValue() == 0.0)
            stockOrder.setType(OrderType.MARKET);

        //stop order
        if(stockOrder.getStopValue() != 0.0 && stockOrder.getLimitValue() == 0.0)
            stockOrder.setType(OrderType.STOP);

        //limit order
        if(stockOrder.getStopValue() == 0.0 && stockOrder.getLimitValue() != 0.0)
            stockOrder.setType(OrderType.LIMIT);

        //stop-limit order
        if(stockOrder.getStopValue() != 0.0 && stockOrder.getLimitValue() != 0.0)
            stockOrder.setType(OrderType.STOP_LIMIT);


        if (stockOrder.getStatus().equals(OrderStatus.PROCESSING)) {
            this.ordersToBuy.add(this.stockOrderRepository.save(stockOrder));
        } else {
            this.ordersToApprove.add(this.stockOrderRepository.save(stockOrder));
        }

        return StockMapper.INSTANCE.stockOrderToStockOrderDto(stockOrder);
    }

    //zovemo funkciju svakih 15 sekundi
    //proveravamo da li je lista prazna, ako jeste ona nista
    //ako ima nesto, uzimamo random StockOrder u listi
    //proveravamo uslove za cenu i limit i stop
    //ako su dobri, kupujemo akciju i azuriramo MyStock u DB
    @Scheduled(fixedRate = 15000)
    public void executeTask() {

        if (ordersToBuy.isEmpty()) {
            System.out.println("Executing task every 15 seconds, but list to buy is empty :-(");
        } else {
            Random rand = new Random();
            int stockNumber = rand.nextInt(ordersToBuy.size());
            StockOrder stockOrder = ordersToBuy.get(stockNumber);   //StockOrder koji obradjujemo

            Stock stock = null;
            Optional<Stock> optionalStock = this.stockRepository.findByTicker(stockOrder.getTicker());
            if(optionalStock.isPresent())
                stock = optionalStock.get();  //uzimao stock iz baze koji kupujemo
            else {
                System.out.println("No stocks available with ticker "+stockOrder.getTicker()+", restarting in 15 seconds...");
                return;
            }

            double currentPrice = stock.getAsk();   //trenutna cena po kojoj kupujemo

            int amountToBuy = rand.nextInt(stockOrder.getAmountLeft()) + 1;

            //provera ako je allOrNon true
            if (stockOrder.isAon()) {
                if (amountToBuy != stockOrder.getAmountLeft()) {
                    System.out.println("Couldn't buy all " + stockOrder.getAmountLeft());
                    return;
                }
            }

            //kreirati stockTransactionDto koji sadrzi sracunatu kolicinu novca u zavisnosti od tipa stock-a,
            //broj racuna banke, broj racuna berze.
            StockTransactionDto stockTransactionDto = new StockTransactionDto();

            //TODO naci broj racuna banke i berze preko valute

            stockTransactionDto.setCurrencyMark("USD");

            if (stockOrder.getType().equals(OrderType.MARKET)) {
                stockTransactionDto.setAmount(currentPrice * amountToBuy);
                bankServiceClient.stockBuyTransaction(stockTransactionDto);

                myStockService.addAmountToMyStock(stockOrder.getTicker(), amountToBuy);    //dodajemo kolicinu kupljenih deonica u vlasnistvo banke
                stockOrder.setAmountLeft(stockOrder.getAmountLeft() - amountToBuy);
                if (stockOrder.getAmountLeft() <= 0) {
                    stockOrder.setStatus(OrderStatus.FINISHED);
                    ordersToBuy.remove(stockNumber);    //uklanjamo ga iz liste jer je zavrsio
                } else {
                    ordersToBuy.remove(stockNumber);
                    ordersToBuy.add(stockNumber,stockOrder);    //update Objekta u listi
                }
            }

            if (stockOrder.getType().equals(OrderType.LIMIT)){
                if (currentPrice < stockOrder.getLimitValue()){
                    stockTransactionDto.setAmount(currentPrice * amountToBuy);
                    bankServiceClient.stockBuyTransaction(stockTransactionDto);
                    myStockService.addAmountToMyStock(stockOrder.getTicker(), amountToBuy);    //dodajemo kolicinu kupljenih deonica u vlasnistvo banke
                    stockOrder.setAmountLeft(stockOrder.getAmountLeft() - amountToBuy);
                    if (stockOrder.getAmountLeft() <= 0) {
                        stockOrder.setStatus(OrderStatus.FINISHED);
                        ordersToBuy.remove(stockNumber);    //uklanjamo ga iz liste jer je zavrsio
                    }else {
                        ordersToBuy.remove(stockNumber);
                        ordersToBuy.add(stockNumber,stockOrder);    //update Objekta u listi
                    }
                } else {
                    stockOrder.setStatus(OrderStatus.FAILED);
                    ordersToBuy.remove(stockNumber);
                }
            }

            if (stockOrder.getType().equals(OrderType.STOP)){
                if (currentPrice > stockOrder.getStopValue()){
                    stockOrder.setType(OrderType.MARKET);
                } else {
                    System.out.println("Stop value hasn't been approved ");
                }
            }

            if (stockOrder.getType().equals(OrderType.STOP_LIMIT)){
                if (currentPrice > stockOrder.getStopValue()){
                    stockOrder.setType(OrderType.LIMIT);
                } else {
                    System.out.println("Stop value hasn't been approved ");
                }
            }

            this.stockOrderRepository.save(stockOrder); //cuvamo promenjene vrednosti u bazi
        }
    }

    private void printer(int amountToBuy, double currentPrice) {
        System.out.println("Kupljeno: " + amountToBuy + " STOCK, po ceni: " + currentPrice*amountToBuy);
    }
}
