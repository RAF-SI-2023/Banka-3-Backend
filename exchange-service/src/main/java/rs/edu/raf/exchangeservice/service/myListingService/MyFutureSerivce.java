package rs.edu.raf.exchangeservice.service.myListingService;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import rs.edu.raf.exchangeservice.client.BankServiceClient;
import rs.edu.raf.exchangeservice.configuration.future.FutureUpdateEvent;
import rs.edu.raf.exchangeservice.domain.dto.bank.BankTransactionDto;
import rs.edu.raf.exchangeservice.domain.dto.buySell.BuyFutureDto;
import rs.edu.raf.exchangeservice.domain.model.enums.OrderStatus;
import rs.edu.raf.exchangeservice.domain.model.listing.Future;
import rs.edu.raf.exchangeservice.domain.model.myListing.MyFuture;
import rs.edu.raf.exchangeservice.domain.model.order.FutureOrder;
import rs.edu.raf.exchangeservice.domain.model.order.FutureOrderSell;
import rs.edu.raf.exchangeservice.jacoco.ExcludeFromJacocoGeneratedReport;
import rs.edu.raf.exchangeservice.repository.listingRepository.FutureRepository;
import rs.edu.raf.exchangeservice.repository.myListingRepository.MyFutureRepository;
import rs.edu.raf.exchangeservice.repository.orderRepository.FutureOrderSellRepository;
import rs.edu.raf.exchangeservice.service.orderService.FuturOrderService;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@RequiredArgsConstructor
public class MyFutureSerivce {

    private final MyFutureRepository myFutureRepository;
    private final FutureRepository futureRepository;
    private final FutureOrderSellRepository futureOrderSellRepository;
    private final BankServiceClient bankServiceClient;
    private final ApplicationEventPublisher eventPublisher;

    private static final double BUSHEL=6.5;
    private static final double POUND=1.2;
    private static final double BOARD_FEET=0.5;
    private static final double BARREL=50.0;
    private static final double GALLON=3.5;
    private static final double TROY_OUNCE=500.0;
    private static final double METRIC_TON=100.0;
    public CopyOnWriteArrayList<FutureOrderSell> orderToSell = new CopyOnWriteArrayList<>();


    public List<MyFuture> findAllForOtcBuy(Long companyId){
        return myFutureRepository.findByIsPublicTrueAndCompanyIdNot(companyId);
    }

    public void addMyFuture(MyFuture myFuture){
        myFutureRepository.save(myFuture);
    }

    public List<MyFuture> findAllByCompanyId(Long companyId){
        return myFutureRepository.findAllByCompanyId(companyId);
    }

    public MyFuture makeFuturePublic(Long myFutureId){
        MyFuture myFuture = myFutureRepository.findByMyFutureId(myFutureId);
        myFuture.setIsPublic(true);
        myFutureRepository.save(myFuture);
        eventPublisher.publishEvent(new FutureUpdateEvent(this, myFuture));
        return myFuture;
    }

    public MyFuture makeFuturePrivate(Long myFutureId){
        MyFuture myFuture = myFutureRepository.findByMyFutureId(myFutureId);
        myFuture.setIsPublic(false);
        myFutureRepository.save(myFuture);
        eventPublisher.publishEvent(new FutureUpdateEvent(this, myFuture));
        return myFuture;
    }


    public FutureOrderSell sellMyFuture(BuyFutureDto buyFutureDto){
        FutureOrderSell futureOrderSell = new FutureOrderSell();
        futureOrderSell.setCompanyId(buyFutureDto.getCompanyId());

        MyFuture myFuture = myFutureRepository.findByMyFutureId(buyFutureDto.getFutureId());
        futureOrderSell.setContractName(myFuture.getContractName());
        futureOrderSell.setStatus(OrderStatus.PROCESSING);

        if(myFuture.getContractUnit().equalsIgnoreCase("BUSHEL")){
            futureOrderSell.setPrice(BUSHEL*myFuture.getContractSize());
        }else if(myFuture.getContractUnit().equalsIgnoreCase("POUND")){
            futureOrderSell.setPrice(POUND*myFuture.getContractSize());
        } else if(myFuture.getContractUnit().equalsIgnoreCase("BOARD FEET")){
            futureOrderSell.setPrice(BOARD_FEET*myFuture.getContractSize());
        } else if(myFuture.getContractUnit().equalsIgnoreCase("BARREL")){
            futureOrderSell.setPrice(BARREL*myFuture.getContractSize());
        } else if(myFuture.getContractUnit().equalsIgnoreCase("GALLON")){
            futureOrderSell.setPrice(GALLON*myFuture.getContractSize());
        } else if(myFuture.getContractUnit().equalsIgnoreCase("TROY OUNCE")){
            futureOrderSell.setPrice(TROY_OUNCE*myFuture.getContractSize());
        } else if(myFuture.getContractUnit().equalsIgnoreCase("METRIC TON")){
            futureOrderSell.setPrice(METRIC_TON*myFuture.getContractSize());
        }

        orderToSell.add(futureOrderSell);
        return futureOrderSell;
    }

    @Scheduled(fixedRate = 10000)
    @ExcludeFromJacocoGeneratedReport
    public void executeOrders(){
        if(!orderToSell.isEmpty()){
            Random random = new Random();
            int index = random.nextInt(orderToSell.size());
            FutureOrderSell futureOrderSell = orderToSell.get(index);

            //todo dodati naknadno za bank transakciju

            MyFuture myFuture = myFutureRepository.findByContractName(futureOrderSell.getContractName());

            Future future = new Future();
            future.setContractName(futureOrderSell.getContractName());
            future.setContractSize(myFuture.getContractSize());
            future.setContractUnit(myFuture.getContractUnit());
            future.setMaintenanceMargin(myFuture.getMaintenanceMargin());
            future.setType(myFuture.getType());
            future.setCurrencyMark(myFuture.getCurrencyMark());
            future.setPrice(futureOrderSell.getPrice());

            BankTransactionDto bankTransactionDto = new BankTransactionDto();
            bankTransactionDto.setAmount(futureOrderSell.getPrice());
            bankTransactionDto.setCompanyId(futureOrderSell.getCompanyId());
            bankTransactionDto.setCurrencyMark(future.getCurrencyMark());
            bankTransactionDto.setUserId(null);
            bankTransactionDto.setEmployeeId(null);

            bankServiceClient.stockSellTransaction(bankTransactionDto);

            futureOrderSell.setStatus(OrderStatus.FINISHED);
            orderToSell.remove(futureOrderSell);

            myFutureRepository.delete(myFuture);
            futureRepository.save(future);
            futureOrderSellRepository.save(futureOrderSell);
            eventPublisher.publishEvent(new FutureUpdateEvent(this, myFuture));
        }
    }

}
