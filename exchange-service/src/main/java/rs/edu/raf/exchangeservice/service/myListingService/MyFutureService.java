package rs.edu.raf.exchangeservice.service.myListingService;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import rs.edu.raf.exchangeservice.domain.dto.SellFutureDto;
import rs.edu.raf.exchangeservice.domain.dto.StockTransactionDto;
import rs.edu.raf.exchangeservice.domain.model.myListing.MyFuture;
import rs.edu.raf.exchangeservice.domain.model.order.FutureOrderSell;
import rs.edu.raf.exchangeservice.repository.listingRepository.FutureRepository;
import rs.edu.raf.exchangeservice.repository.myListingRepository.MyFutureRepository;
import rs.edu.raf.exchangeservice.repository.orderRepository.FutureOrderSellRepository;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@RequiredArgsConstructor
public class MyFutureService {

    private final MyFutureRepository myFutureRepository;
    private final FutureOrderSellRepository futureOrderSellRepository;
    private final FutureRepository futureRepository;

    public CopyOnWriteArrayList<FutureOrderSell> ordersToSell = new CopyOnWriteArrayList<>();

    public List<MyFuture> getAll(){
        return this.myFutureRepository.findAll();
    }

    public String sellFuture(SellFutureDto sellFutureDto){
        FutureOrderSell futureOrderSell = new FutureOrderSell();
        futureOrderSell.setFutureId(sellFutureDto.getFutureId());
        futureOrderSell.setEmployeeId(sellFutureDto.getEmployeeId());
        futureOrderSell.setPrice(sellFutureDto.getPrice());

        this.ordersToSell.add(futureOrderSellRepository.save(futureOrderSell));
        return "UBACENO U ORDER";
    }

    @Scheduled(fixedRate = 40000)
    public void executeTask() {
        if (ordersToSell.isEmpty()){

        }else {
            Random rand = new Random();
            int futureNumber = rand.nextInt(ordersToSell.size());
            FutureOrderSell futureOrderSell = ordersToSell.get(futureNumber);   //FutureOrder koji obradjujemo

            //kreirati stockTransactionDto koji sadrzi sracunatu kolicinu novca u zavisnosti od tipa stock-a,
            //broj racuna banke, broj racuna berze.
            StockTransactionDto stockTransactionDto = new StockTransactionDto();

            //TODO naci broj racuna banke i berze preko valute

            this.futureOrderSellRepository.save(futureOrderSell); //cuvamo promenjene vrednosti u bazi
        }
    }

}
