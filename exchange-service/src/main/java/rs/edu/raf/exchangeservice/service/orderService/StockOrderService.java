package rs.edu.raf.exchangeservice.service.orderService;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import rs.edu.raf.exchangeservice.client.BankServiceClient;
import rs.edu.raf.exchangeservice.domain.dto.StockOrderDto;
import rs.edu.raf.exchangeservice.domain.dto.bank.BankTransactionDto;
import rs.edu.raf.exchangeservice.domain.dto.buySell.BuySellStockDto;
import rs.edu.raf.exchangeservice.domain.mappers.StockMapper;
import rs.edu.raf.exchangeservice.domain.model.Actuary;
import rs.edu.raf.exchangeservice.domain.model.enums.OrderStatus;
import rs.edu.raf.exchangeservice.domain.model.enums.OrderType;
import rs.edu.raf.exchangeservice.domain.model.listing.Stock;
import rs.edu.raf.exchangeservice.domain.model.order.StockOrder;
import rs.edu.raf.exchangeservice.jacoco.ExcludeFromJacocoGeneratedReport;
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

    public List<StockOrder> findAll() {
        return stockOrderRepository.findAll();
    }

    public List<StockOrder> findAllByEmployee(Long id) {
        return stockOrderRepository.findByEmployeeId(id);
    }

    //vracamo sve ordere koje treba approve-ovati
    public List<StockOrder> getAllOrdersToApprove() {
        return stockOrderRepository.findStockOrderByStatus(OrderStatus.WAITING);
    }

    //odobravamo ili ne odobravamo StockOrder Agentu
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public StockOrder approveStockOrder(Long id, boolean approved) {
        StockOrder stockOrder = stockOrderRepository.findByStockOrderId(id);
        if (approved) {
            stockOrder.setStatus(OrderStatus.PROCESSING);
            this.ordersToBuy.add(stockOrder);
        } else {
            stockOrder.setStatus(OrderStatus.REJECTED);
        }
        return stockOrderRepository.save(stockOrder);
    }

    //od buyOrderDto pravimo StockOrder i
    //dodajemo order u listu
    public StockOrderDto buyStock(BuySellStockDto buySellStockDto) {
        StockOrder stockOrder = new StockOrder();
        stockOrder.setEmployeeId(buySellStockDto.getEmployeeId());
        stockOrder.setTicker(buySellStockDto.getTicker());
        stockOrder.setAmount(buySellStockDto.getAmount());
        stockOrder.setAmountLeft(buySellStockDto.getAmount());
        stockOrder.setAon(buySellStockDto.isAon());
        stockOrder.setMargin(buySellStockDto.isMargin());

        if (actuaryRepository.findByEmployeeId(buySellStockDto.getEmployeeId()).isOrderRequest()) {
            stockOrder.setStatus(OrderStatus.WAITING);
        } else {
            stockOrder.setStatus(OrderStatus.PROCESSING);
        }

        if (buySellStockDto.getStopValue() == null)
            stockOrder.setStopValue(0.0);
        else
            stockOrder.setStopValue(buySellStockDto.getStopValue());

        if (buySellStockDto.getLimitValue() == null)
            stockOrder.setLimitValue(0.0);
        else
            stockOrder.setLimitValue(buySellStockDto.getLimitValue());

        //market order
        if (stockOrder.getStopValue() == 0.0 && stockOrder.getLimitValue() == 0.0)
            stockOrder.setType(OrderType.MARKET);

        //stop order
        if (stockOrder.getStopValue() != 0.0 && stockOrder.getLimitValue() == 0.0)
            stockOrder.setType(OrderType.STOP);

        //limit order
        if (stockOrder.getStopValue() == 0.0 && stockOrder.getLimitValue() != 0.0)
            stockOrder.setType(OrderType.LIMIT);

        //stop-limit order
        if (stockOrder.getStopValue() != 0.0 && stockOrder.getLimitValue() != 0.0)
            stockOrder.setType(OrderType.STOP_LIMIT);

        if (stockOrder.getStatus().equals(OrderStatus.PROCESSING)) {
            this.ordersToBuy.add(stockOrderRepository.save(stockOrder));
        }else {
            stockOrderRepository.save(stockOrder);
        }

        return StockMapper.INSTANCE.stockOrderToStockOrderDto(stockOrder);
    }

    @Scheduled(fixedRate = 10000)
    @ExcludeFromJacocoGeneratedReport
    public void executeTask() {
        if (ordersToBuy.isEmpty()) {
            System.out.println("Executing buy-task every 10 seconds");
        } else {
            Random rand = new Random();
            int stockNumber = rand.nextInt(ordersToBuy.size());
            StockOrder stockOrder = ordersToBuy.get(stockNumber);   //StockOrder koji obradjujemo

            Stock stock = null;
            Optional<Stock> optionalStock = stockRepository.findByTicker(stockOrder.getTicker());
            if (optionalStock.isPresent())
                stock = optionalStock.get();  //uzimao stock iz baze koji kupujemo
            else {
//                System.out.println("No stocks available with ticker "+stockOrder.getTicker()+", restarting in 15 seconds...");
                return;
            }

            double currentPrice = stock.getAsk();   //trenutna cena po kojoj kupujemo
            int amountToBuy = rand.nextInt(stockOrder.getAmountLeft()) + 1;

            if (stockOrder.isAon()) {
                amountToBuy = stockOrder.getAmount();
            }

            //dodavanje za limit zaposlenog
            Actuary actuary = actuaryRepository.findByEmployeeId(stockOrder.getEmployeeId());
            if (actuary.getLimitValue() != 0.0){
                if(actuary.getLimitUsed() + (currentPrice * amountToBuy) > actuary.getLimitValue()){
                    stockOrder.setStatus(OrderStatus.FAILED);
                    ordersToBuy.remove(stockOrder);
                    stockOrderRepository.save(stockOrder);
                    actuaryRepository.save(actuary);
                    return;
                }else {
                    actuary.setLimitUsed(actuary.getLimitUsed() + (currentPrice * amountToBuy));
                    actuaryRepository.save(actuary);
                }
            }

            //za bank service da skine pare
            BankTransactionDto bankTransactionDto = new BankTransactionDto();
            bankTransactionDto.setCurrencyMark(stock.getCurrencyMark());

            if (stockOrder.getType().equals(OrderType.MARKET)) {
                bankTransactionDto.setAmount(currentPrice * amountToBuy);
                bankServiceClient.stockBuyTransaction(bankTransactionDto);

                myStockService.addAmountToMyStock(stockOrder.getTicker(), amountToBuy);    //dodajemo kolicinu kupljenih deonica u vlasnistvo banke
                stockOrder.setAmountLeft(stockOrder.getAmountLeft() - amountToBuy);
                if (stockOrder.getAmountLeft() <= 0) {
                    stockOrder.setStatus(OrderStatus.FINISHED);
                    ordersToBuy.remove(stockNumber);    //uklanjamo ga iz liste jer je zavrsio
                } else {
                    ordersToBuy.remove(stockNumber);
                    ordersToBuy.add(stockNumber, stockOrder);    //update Objekta u listi
                }
            }

            if (stockOrder.getType().equals(OrderType.LIMIT)) {
                if (currentPrice < stockOrder.getLimitValue()) {
                    bankTransactionDto.setAmount(currentPrice * amountToBuy);
                    bankServiceClient.stockBuyTransaction(bankTransactionDto);

                    myStockService.addAmountToMyStock(stockOrder.getTicker(), amountToBuy);    //dodajemo kolicinu kupljenih deonica u vlasnistvo banke
                    stockOrder.setAmountLeft(stockOrder.getAmountLeft() - amountToBuy);
                    if (stockOrder.getAmountLeft() <= 0) {
                        stockOrder.setStatus(OrderStatus.FINISHED);
                        ordersToBuy.remove(stockNumber);    //uklanjamo ga iz liste jer je zavrsio
                    } else {
                        ordersToBuy.remove(stockNumber);
                        ordersToBuy.add(stockNumber, stockOrder);    //update Objekta u listi
                    }
                } else {
                    stockOrder.setStatus(OrderStatus.FAILED);
                    ordersToBuy.remove(stockNumber);
                }
            }

            if (stockOrder.getType().equals(OrderType.STOP)) {
                if (currentPrice > stockOrder.getStopValue()) {
                    stockOrder.setType(OrderType.MARKET);
                }
            }

            if (stockOrder.getType().equals(OrderType.STOP_LIMIT)) {
                if (currentPrice > stockOrder.getStopValue()) {
                    stockOrder.setType(OrderType.LIMIT);
                }
            }

            this.stockOrderRepository.save(stockOrder); //cuvamo promenjene vrednosti u bazi
        }
    }
}
