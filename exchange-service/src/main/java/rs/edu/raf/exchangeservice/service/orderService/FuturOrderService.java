package rs.edu.raf.exchangeservice.service.orderService;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import rs.edu.raf.exchangeservice.client.BankServiceClient;
import rs.edu.raf.exchangeservice.domain.dto.bank.BankTransactionDto;
import rs.edu.raf.exchangeservice.domain.dto.buySell.BuyFutureDto;
import rs.edu.raf.exchangeservice.domain.model.enums.OrderStatus;
import rs.edu.raf.exchangeservice.domain.model.listing.Future;
import rs.edu.raf.exchangeservice.domain.model.myListing.MyFuture;
import rs.edu.raf.exchangeservice.domain.model.order.FutureOrder;
import rs.edu.raf.exchangeservice.domain.model.order.StockOrder;
import rs.edu.raf.exchangeservice.jacoco.ExcludeFromJacocoGeneratedReport;
import rs.edu.raf.exchangeservice.repository.listingRepository.FutureRepository;
import rs.edu.raf.exchangeservice.repository.orderRepository.FutureOrderRepository;
import rs.edu.raf.exchangeservice.service.myListingService.MyFutureSerivce;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@RequiredArgsConstructor
@Getter
public class FuturOrderService {

    private final FutureRepository futureRepository;
    private final FutureOrderRepository futureOrderRepository;
    private final MyFutureSerivce myFutureSerivce;
    private final BankServiceClient bankServiceClient;

    private static final double BUSHEL=6.5;
    private static final double POUND=1.2;
    private static final double BOARD_FEET=0.5;
    private static final double BARREL=50.0;
    private static final double GALLON=3.5;
    private static final double TROY_OUNCE=500.0;
    private static final double METRIC_TON=100.0;

    public CopyOnWriteArrayList<FutureOrder> ordersToBuy = new CopyOnWriteArrayList<>();

    public List<FutureOrder> findAll(){
        return futureOrderRepository.findAll();
    }


    public FutureOrder buyFuture(BuyFutureDto dto) {
        FutureOrder futureOrder = new FutureOrder();
        futureOrder.setCompanyId(dto.getCompanyId());

        Future future = futureRepository.findByFutureId(dto.getFutureId());
        futureOrder.setContractName(future.getContractName());
        futureOrder.setStatus(OrderStatus.PROCESSING);

        //todo dodati price
        if(future.getContractUnit().equalsIgnoreCase("BUSHEL")){
            futureOrder.setPrice(BUSHEL*future.getContractSize());
        }else if(future.getContractUnit().equalsIgnoreCase("POUND")){
            futureOrder.setPrice(POUND*future.getContractSize());
        }else if(future.getContractUnit().equalsIgnoreCase("BOARD FEET")){
            futureOrder.setPrice(BOARD_FEET*future.getContractSize());
        }else if(future.getContractUnit().equalsIgnoreCase("BARREL")){
            futureOrder.setPrice(BARREL*future.getContractSize());
        } else if(future.getContractUnit().equalsIgnoreCase("GALLON")) {
            futureOrder.setPrice(GALLON * future.getContractSize());
        } else if(future.getContractUnit().equalsIgnoreCase("TROY OUNCE")) {
            futureOrder.setPrice(TROY_OUNCE * future.getContractSize());
        } else if(future.getContractUnit().equalsIgnoreCase("METRIC TON")) {
            futureOrder.setPrice(METRIC_TON * future.getContractSize());
        }

        futureOrderRepository.save(futureOrder);

        ordersToBuy.add(futureOrder);
        return futureOrder;
    }

    @Scheduled(fixedRate = 10000)
    @ExcludeFromJacocoGeneratedReport
    public void executeOrders(){
        if(!ordersToBuy.isEmpty()){
            Random random = new Random();
            int futureNumber = random.nextInt(ordersToBuy.size());
            FutureOrder futureOrder = ordersToBuy.get(futureNumber);

            //todo dodati naknadno za bank transakciju izvuci price iz futureOrder-a

            Future future = futureRepository.findByContractName(futureOrder.getContractName()).orElseThrow(() -> new RuntimeException("Future not found"));

            MyFuture myFuture = new MyFuture();
            myFuture.setCompanyId(futureOrder.getCompanyId());
            myFuture.setContractName(futureOrder.getContractName());
            myFuture.setContractSize(future.getContractSize());
            myFuture.setContractUnit(future.getContractUnit());
            myFuture.setMaintenanceMargin(future.getMaintenanceMargin());
            myFuture.setType(future.getType());
            myFuture.setCurrencyMark(future.getCurrencyMark());
            myFuture.setPrice(futureOrder.getPrice());
            myFuture.setIsPublic(false);

            myFutureSerivce.addMyFuture(myFuture);

            BankTransactionDto bankTransactionDto = new BankTransactionDto();
            bankTransactionDto.setAmount(futureOrder.getPrice());
            bankTransactionDto.setCurrencyMark(future.getCurrencyMark());
            bankTransactionDto.setCompanyId(futureOrder.getCompanyId());
            bankTransactionDto.setUserId(null);
            bankTransactionDto.setEmployeeId(null);

            bankServiceClient.stockBuyTransaction(bankTransactionDto);

            futureOrder.setStatus(OrderStatus.FINISHED);
            futureOrderRepository.save(futureOrder);
            ordersToBuy.remove(futureOrder);

            futureRepository.delete(future);
        }
    }

}
