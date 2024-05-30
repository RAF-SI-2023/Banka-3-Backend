package rs.edu.raf.exchangeservice.bootstrap;

import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import rs.edu.raf.exchangeservice.domain.model.myListing.MyStock;
import rs.edu.raf.exchangeservice.repository.myListingRepository.MyStockRepository;
import rs.edu.raf.exchangeservice.service.ExchangeService;
import rs.edu.raf.exchangeservice.service.listingService.ForexService;
import rs.edu.raf.exchangeservice.service.listingService.FutureService;

@AllArgsConstructor
@Component
public class BootstrapData implements CommandLineRunner {

    MyStockRepository myStockRepository;
    ExchangeService exchangeService;
    FutureService futureService;
    ForexService forexService;
    @Override
    public void run(String... args) throws Exception {

        if(exchangeService.findAll().size() == 0) {
            exchangeService.loadData();
        }
        if(futureService.findAll().size() == 0){
            futureService.loadData();
        }
        if(forexService.findAll().size() == 0){
            forexService.loadData();
        }

        if(myStockRepository.count() == 0) {
            //banka 3 stokovi
            MyStock stok1 = new MyStock();
            stok1.setTicker("AAPL");
            stok1.setAmount(100);
            stok1.setCurrencyMark("RSD");
            stok1.setPrivateAmount(50);
            stok1.setPublicAmount(50);
            stok1.setCompanyId(1L);
            stok1.setUserId(null);
            MyStock stok2 = new MyStock();
            stok2.setTicker("NVDA");
            stok2.setAmount(100);
            stok2.setCurrencyMark("RSD");
            stok2.setPrivateAmount(50);
            stok2.setPublicAmount(50);
            stok2.setCompanyId(1L);
            stok2.setUserId(null);
            MyStock stok3 = new MyStock();
            stok3.setTicker("AMZN");
            stok3.setAmount(100);
            stok3.setCurrencyMark("RSD");
            stok3.setPrivateAmount(50);
            stok3.setPublicAmount(50);
            stok3.setCompanyId(1L);
            stok3.setUserId(null);

            MyStock stok5 = new MyStock();
            stok5.setTicker("NVDA");
            stok5.setAmount(20);
            stok5.setCurrencyMark("RSD");
            stok5.setPrivateAmount(10);
            stok5.setPublicAmount(10);
            stok5.setCompanyId(3L);
            stok5.setUserId(null);

            MyStock stok4 = new MyStock();
            stok4.setTicker("TSLA");
            stok4.setAmount(20);
            stok4.setCurrencyMark("RSD");
            stok4.setPrivateAmount(10);
            stok4.setPublicAmount(10);
            stok4.setCompanyId(4L);
            stok4.setUserId(null);

            myStockRepository.save(stok1);
            myStockRepository.save(stok2);
            myStockRepository.save(stok3);
            myStockRepository.save(stok4);
            myStockRepository.save(stok5);
        }
    }
}
