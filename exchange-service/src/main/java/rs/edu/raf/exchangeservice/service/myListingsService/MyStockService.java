package rs.edu.raf.exchangeservice.service.myListingsService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rs.edu.raf.exchangeservice.domain.dto.SellStockDto;
import rs.edu.raf.exchangeservice.domain.model.listing.Stock;
import rs.edu.raf.exchangeservice.domain.model.listing.Ticker;
import rs.edu.raf.exchangeservice.domain.model.myListing.MyStock;
import rs.edu.raf.exchangeservice.repository.ActuaryRepository;
import rs.edu.raf.exchangeservice.repository.listing.StockRepository;
import rs.edu.raf.exchangeservice.repository.listing.TickerRepository;
import rs.edu.raf.exchangeservice.repository.myListingRepository.MyStockRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MyStockService {
    private final MyStockRepository myStockRepository;
    private final TickerRepository tickerRepository;
    private final StockRepository stockRepository;

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
    public void addAmountToMyStock(String ticker, Integer amount){
        MyStock myStock = myStockRepository.findByTicker(ticker);
        myStock.setAmount(myStock.getAmount() + amount);
        this.myStockRepository.save(myStock);
    }

    //vracamo sve deonice koje su u vlasnistvu banke
    public List<MyStock> getAll(){
        return this.myStockRepository.findAll();
    }

    //funkcija kada prodajemo Stock
    public String sellStock(SellStockDto sellStockDto){
        Stock stock = stockRepository.findByTicker(sellStockDto.getTicker()).get();
        MyStock myStock = myStockRepository.findByTicker(sellStockDto.getTicker());
        if (myStock.getAmount() < sellStockDto.getAmount()){
            return "NEUSPESNO";
        }

        Double value = stock.getBid() * sellStockDto.getAmount(); //TODO: ovo saljemo transaction servicu
        myStock.setAmount(myStock.getAmount() - sellStockDto.getAmount());
        myStockRepository.save(myStock);
        return "USPESNO " + value;
    }
}
