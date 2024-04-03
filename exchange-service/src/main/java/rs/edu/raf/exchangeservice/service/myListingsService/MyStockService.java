package rs.edu.raf.exchangeservice.service.myListingsService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rs.edu.raf.exchangeservice.domain.dto.BuyStockDto;
import rs.edu.raf.exchangeservice.domain.model.Actuary;
import rs.edu.raf.exchangeservice.domain.model.Stock;
import rs.edu.raf.exchangeservice.domain.model.Ticker;
import rs.edu.raf.exchangeservice.domain.model.myListings.MyStock;
import rs.edu.raf.exchangeservice.repository.ActuaryRepository;
import rs.edu.raf.exchangeservice.repository.StockRepository;
import rs.edu.raf.exchangeservice.repository.TickerRepository;
import rs.edu.raf.exchangeservice.repository.myListingsRepository.MyStockRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MyStockService {
    private final MyStockRepository myStockRepository;
    private final TickerRepository tickerRepository;
    private final StockRepository stockRepository;
    private final ActuaryRepository actuaryRepository;

    public void loadData() {
        List<Ticker> tickersList = tickerRepository.findAll();

        for (Ticker ticker : tickersList){
            MyStock myStock = new MyStock();
            myStock.setTicker(ticker.getTicker());
            myStock.setAmount(0);

            myStockRepository.save(myStock);
        }
    }

    private boolean checkActuary(Actuary actuary, Double value){
        if(actuary.getRole().contains("SUPERVISOR")){
            return true;
        }
        if (actuary.getLimitUsed() + value > actuary.getLimitValue()){
            return false;
        }else {
            if (actuary.isOrderRequest()){
                //TODO: ide na listu cekanja
            }else {
                actuary.setLimitUsed(actuary.getLimitUsed() + value);
                this.actuaryRepository.save(actuary);
                return true;
            }
        }
        return false;
    }

    public String buyStock(BuyStockDto buyStockDto){
        Stock stock = this.stockRepository.findByTicker(buyStockDto.getTicker());
        Double value = stock.getAsk() * buyStockDto.getAmount();
        Actuary actuary = this.actuaryRepository.findByEmployeeId(buyStockDto.getEmployeeId()); //aktuar kog treba proveriti

        if (!checkActuary(actuary,value)){
            return "NEUSPESNO";
        }
        //klasican Market Order
        if (buyStockDto.getStopValue() == 0 && buyStockDto.getLimitValue() == 0){
            MyStock myStock = myStockRepository.findByTicker(buyStockDto.getTicker());
            myStock.setAmount(myStock.getAmount() + buyStockDto.getAmount());
            this.myStockRepository.save(myStock);
            //TODO: transakcija ka transactionService
        }

        //stop order
        if(buyStockDto.getStopValue() != 0 && buyStockDto.getLimitValue() == 0){

        }

        //limit order
        if(buyStockDto.getStopValue() == 0 && buyStockDto.getLimitValue() != 0){

        }

        //stop-limit order
        if(buyStockDto.getStopValue() != 0 && buyStockDto.getLimitValue() != 0){

        }

        return "USEPSNO";
    }
}
