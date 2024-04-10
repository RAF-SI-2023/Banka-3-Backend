package rs.edu.raf.exchangeservice.service.orderService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rs.edu.raf.exchangeservice.domain.dto.BuyFutureDto;
import rs.edu.raf.exchangeservice.domain.model.order.FutureOrder;
import rs.edu.raf.exchangeservice.repository.ActuaryRepository;
import rs.edu.raf.exchangeservice.repository.orderRepository.FutureOrderRepository;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@RequiredArgsConstructor
public class FutureOrderService {
    private final FutureOrderRepository futureOrderRepository;

    private final ActuaryRepository actuaryRepository;


    public CopyOnWriteArrayList<FutureOrder> ordersToBuy = new CopyOnWriteArrayList<>();
    public CopyOnWriteArrayList<FutureOrder> ordersToApprove = new CopyOnWriteArrayList<>();

    public List<FutureOrder> findAll(){
        return futureOrderRepository.findAll();
    }

    public List<FutureOrder> findAllByEmployee(Long id){
        return futureOrderRepository.findByEmployeeId(id);
    }

    public List<FutureOrder> getAllOrdersToApprove(){
        return this.ordersToApprove;
    }

    public FutureOrder approveFutureOrder(Long id, boolean approved){
        FutureOrder futureOrder = futureOrderRepository.findByFutureOrderId(id);
        if (approved){
            futureOrder.setStatus("PROCESSING");
            this.ordersToApprove.remove(futureOrder);
            this.ordersToBuy.add(futureOrder);
        }else {
            futureOrder.setStatus("REJECTED");
            this.ordersToApprove.remove(futureOrder);
        }
        return this.futureOrderRepository.save(futureOrder);
    }

    public String buyFuture(BuyFutureDto buyFutureDto){
        FutureOrder futureOrder = new FutureOrder();
        futureOrder.setEmployeeId(buyFutureDto.getEmployeeId());
        futureOrder.setContractName(buyFutureDto.getContractName());
        futureOrder.setAmount(buyFutureDto.getAmount());

        if (actuaryRepository.findByEmployeeId(buyFutureDto.getEmployeeId()).isOrderRequest()){
            futureOrder.setStatus("WAITING");
        }else {
            futureOrder.setStatus("PROCESSING");
        }

        futureOrder.setAmount(buyFutureDto.getAmount());
        futureOrder.setAmountLeft(buyFutureDto.getAmount());
        futureOrder.setAon(buyFutureDto.isAon());
        futureOrder.setMargine(buyFutureDto.isMargine());

        if (futureOrder.getStatus().equalsIgnoreCase("PROCESSING")){
            this.ordersToBuy.add(this.futureOrderRepository.save(futureOrder));
        }else {
            this.ordersToApprove.add(this.futureOrderRepository.save(futureOrder));
        }

        return "UBACENO U ORDER";
    }

}
