package rs.edu.raf.exchangeservice.service.orderService;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import rs.edu.raf.exchangeservice.domain.dto.BuyStockDto;
import rs.edu.raf.exchangeservice.domain.model.listing.Stock;
import rs.edu.raf.exchangeservice.domain.model.order.StockOrder;
import rs.edu.raf.exchangeservice.repository.ActuaryRepository;
import rs.edu.raf.exchangeservice.repository.listing.StockRepository;
import rs.edu.raf.exchangeservice.repository.orderRepository.StockOrderRepository;
import rs.edu.raf.exchangeservice.service.myListingsService.MyStockService;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@RequiredArgsConstructor
public class StockOrderService {
    private final StockOrderRepository stockOrderRepository;
    private final StockRepository stockRepository;
    private final ActuaryRepository actuaryRepository;
    private final MyStockService myStockService;

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
            stockOrder.setStatus("PROCESSING");
            this.ordersToApprove.remove(stockOrder);
            this.ordersToBuy.add(stockOrder);
        }else {
            stockOrder.setStatus("REJECTED");
            this.ordersToApprove.remove(stockOrder);
        }
        return this.stockOrderRepository.save(stockOrder);
    }

    //od buyOrderDto pravimo StockOrder i
    //dodajemo order u listu
    public void buyStock(BuyStockDto buyStockDto){
        StockOrder stockOrder = new StockOrder();
        stockOrder.setEmployeeId(buyStockDto.getEmployeeId());
        stockOrder.setTicker(buyStockDto.getTicker());
        stockOrder.setAmount(buyStockDto.getAmount());

        if (this.actuaryRepository.findByEmployeeId(buyStockDto.getEmployeeId()).isOrderRequest()){
            stockOrder.setStatus("WAITING");
        }else {
            stockOrder.setStatus("PROCESSING");
        }

        stockOrder.setAmount(buyStockDto.getAmount());
        stockOrder.setAmountLeft(buyStockDto.getAmount());
        stockOrder.setAon(buyStockDto.isAon());
        stockOrder.setMargine(buyStockDto.isMargine());

        //market order
        if (buyStockDto.getStopValue() == 0.0 && buyStockDto.getLimitValue() == 0.0){
            stockOrder.setLimitValue(0.0);
            stockOrder.setStopValue(0.0);
            stockOrder.setType("MARKET");
        }

        //stop order
        if(buyStockDto.getStopValue() != 0.0 && buyStockDto.getLimitValue() == 0.0){
            stockOrder.setLimitValue(0.0);
            stockOrder.setStopValue(buyStockDto.getStopValue());
            stockOrder.setType("STOP");
        }

        //limit order
        if(buyStockDto.getStopValue() == 0.0 && buyStockDto.getLimitValue() != 0.0){
            stockOrder.setLimitValue(buyStockDto.getLimitValue());
            stockOrder.setStopValue(0.0);
            stockOrder.setType("LIMIT");
        }

        //stop-limit order
        if(buyStockDto.getStopValue() != 0.0 && buyStockDto.getLimitValue() != 0.0){
            stockOrder.setLimitValue(buyStockDto.getLimitValue());
            stockOrder.setStopValue(buyStockDto.getStopValue());
            stockOrder.setType("STOP-LIMIT");
        }

        if (stockOrder.getStatus().equalsIgnoreCase("PROCESSING")){
            this.ordersToBuy.add(this.stockOrderRepository.save(stockOrder));
        }else {
            this.ordersToApprove.add(this.stockOrderRepository.save(stockOrder));
        }
    }

    //zovemo funkciju svakih 15 sekundi
    //proveravamo da li je lista prazna, ako jeste ona nista
    //ako ima nesto, uzimamo random StockOrder u listi
    //proveravamo uslove za cenu i limit i stop
    //ako su dobri, kupujemo akciju i pravimo MyStock objekat i dodajemo u bazu
    @Scheduled(fixedRate = 5000)
    public void executeTask() {
        if (ordersToBuy.isEmpty()){
            System.out.println("Executing task every 15 seconds, but list is empty :-(");
        }else {
            Random rand = new Random();
            int stockNumber = rand.nextInt(ordersToBuy.size());
            StockOrder stockOrder = ordersToBuy.get(stockNumber);   //StockOrder koji obradjujemo

            Stock stock = this.stockRepository.findByTicker(stockOrder.getTicker()).get();  //uzimao stock iz baze koji kupujemo
            Double currentPrice = stock.getAsk();   //trenutna cena po kojoj kupujemo

            int amountToBuy = rand.nextInt(stockOrder.getAmountLeft()) + 1;

            //provera ako je allOrNon true
            if (stockOrder.isAon()){
                if (amountToBuy != stockOrder.getAmountLeft()){
                    System.out.println("Couldn't buy all");
                    return;
                }
            }

            if (stockOrder.getType().equalsIgnoreCase("MARKET")){
                printer(amountToBuy,currentPrice);    //TODO: poslati currentPrice*amountToBuy ka Transaction-service
                myStockService.addAmountToMyStock(stockOrder.getTicker(), amountToBuy);    //dodajemo kolicinu kupljenih deonica u vlasnistvo banke
                stockOrder.setAmountLeft(stockOrder.getAmountLeft() - amountToBuy);
                if (stockOrder.getAmountLeft() <= 0){
                    stockOrder.setStatus("FINISHED");
                    ordersToBuy.remove(stockNumber);    //uklanjamo ga iz liste jer je zavrsio
                }else {
                    ordersToBuy.remove(stockNumber);
                    ordersToBuy.add(stockNumber,stockOrder);    //update Objekta u listi
                }
            }

            if (stockOrder.getType().equalsIgnoreCase("LIMIT")){
                if (currentPrice < stockOrder.getLimitValue()){
                    printer(amountToBuy,currentPrice); //TODO: poslati currentPrice*amountToBuy ka Transaction-service
                    myStockService.addAmountToMyStock(stockOrder.getTicker(), amountToBuy);    //dodajemo kolicinu kupljenih deonica u vlasnistvo banke
                    stockOrder.setAmountLeft(stockOrder.getAmountLeft() - amountToBuy);
                    if (stockOrder.getAmountLeft() <= 0){
                        stockOrder.setStatus("FINISHED");
                        ordersToBuy.remove(stockNumber);    //uklanjamo ga iz liste jer je zavrsio
                    }else {
                        ordersToBuy.remove(stockNumber);
                        ordersToBuy.add(stockNumber,stockOrder);    //update Objekta u listi
                    }
                }else {
                    stockOrder.setStatus("FAILED");
                    ordersToBuy.remove(stockNumber);
                }
            }

            if (stockOrder.getType().equalsIgnoreCase("STOP")){
                if (currentPrice > stockOrder.getStopValue()){
                    stockOrder.setType("MARKET");
                } else {
                    System.out.println("Stop value hasn't been approved ");
                }
            }

            if (stockOrder.getType().equalsIgnoreCase("STOP-LIMIT")){
                if (currentPrice > stockOrder.getStopValue()){
                    stockOrder.setType("LIMIT");
                } else {
                    System.out.println("Stop value hasn't been approved ");
                }
            }

            this.stockOrderRepository.save(stockOrder); //cuvamo promenjene vrednosti u bazi
        }
    }

    private void printer(int amountToBuy, double currentPrice){
        System.out.println("Kupljeno: " + amountToBuy + " STOCK, po ceni: " + currentPrice*amountToBuy);
    }
}
