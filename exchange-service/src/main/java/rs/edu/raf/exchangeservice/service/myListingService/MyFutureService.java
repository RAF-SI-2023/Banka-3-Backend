package rs.edu.raf.exchangeservice.service.myListingService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rs.edu.raf.exchangeservice.domain.dto.SellFutureDto;
import rs.edu.raf.exchangeservice.domain.dto.SellStockDto;
import rs.edu.raf.exchangeservice.domain.model.myListing.MyFuture;
import rs.edu.raf.exchangeservice.domain.model.myListing.MyStock;
import rs.edu.raf.exchangeservice.domain.model.order.FutureOrderSell;
import rs.edu.raf.exchangeservice.domain.model.order.StockOrderSell;
import rs.edu.raf.exchangeservice.repository.myListingRepository.MyFutureRepository;
import rs.edu.raf.exchangeservice.repository.orderRepository.FutureOrderSellRepository;
import rs.edu.raf.exchangeservice.repository.orderRepository.StockOrderSellRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@RequiredArgsConstructor
public class MyFutureService {

    private final MyFutureRepository myFutureRepository;
    private final FutureOrderSellRepository futureOrderSellRepository;

    public CopyOnWriteArrayList<FutureOrderSell> ordersToSell = new CopyOnWriteArrayList<>();

    public List<MyFuture> getAll(){
        return this.myFutureRepository.findAll();
    }

    @Transactional
    public void addAmountToMyFuture(String contractName, Integer amount){
        MyFuture myFuture = myFutureRepository.findByContractName(contractName);
        myFuture.setAmount(myFuture.getAmount() + amount);
        this.myFutureRepository.save(myFuture);
    }

    public String sellFuture(SellFutureDto sellFutureDto){
        MyFuture myFuture = myFutureRepository.findByContractName(sellFutureDto.getContractName());

        FutureOrderSell futureOrderSell = new FutureOrderSell();
        futureOrderSell.setEmployeeId(sellFutureDto.getEmployeeId());
        futureOrderSell.setContractName(sellFutureDto.getContractName());
        futureOrderSell.setAmount(sellFutureDto.getAmount());
        futureOrderSell.setAmountLeft(sellFutureDto.getAmount());
        futureOrderSell.setAon(sellFutureDto.isAon());
        futureOrderSell.setMargine(sellFutureDto.isMargine());

        if (futureOrderSell.getAmount() > myFuture.getAmount()){
            futureOrderSell.setStatus("FAILED");
            futureOrderSellRepository.save(futureOrderSell);
            return "nije dobar amount";
        }

        futureOrderSell.setStatus("PROCESSING");
        this.ordersToSell.add(futureOrderSellRepository.save(futureOrderSell));
        return "UBACENO U ORDER";
    }

}
