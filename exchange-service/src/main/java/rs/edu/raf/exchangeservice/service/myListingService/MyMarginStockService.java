package rs.edu.raf.exchangeservice.service.myListingService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import rs.edu.raf.exchangeservice.domain.model.listing.Ticker;
import rs.edu.raf.exchangeservice.domain.model.myListing.MyMarginStock;
import rs.edu.raf.exchangeservice.repository.listingRepository.TickerRepository;
import rs.edu.raf.exchangeservice.repository.myListingRepository.MyMarginStockRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MyMarginStockService {
    private final MyMarginStockRepository myMarginStockRepository;
    private final TickerRepository tickerRepository;

    public List<MyMarginStock> findAllByUserId(Long userId) {
        return myMarginStockRepository.findAllByUserId(userId);
    }

    public List<MyMarginStock> findAllByCompanyId(Long companyId) {
        return myMarginStockRepository.findAllByCompanyId(companyId);
    }

    public void addAmountToMyMarginStock(String ticker, Integer amount, Long userId, Long companyId, Double minimumPrice) {
        if(userId != null){
            MyMarginStock myMarginStock = myMarginStockRepository.findByTickerAndUserId(ticker, userId);
            if(myMarginStock == null){
                Ticker ticker1 = tickerRepository.findByTicker(ticker);
                myMarginStock = new MyMarginStock();
                myMarginStock.setTicker(ticker);
                myMarginStock.setUserId(userId);
                myMarginStock.setCompanyId(null);
                myMarginStock.setAmount(amount);
                myMarginStock.setCurrencyMark(ticker1.getCurrencyName());
                myMarginStock.setMinimumPrice(minimumPrice);
                myMarginStockRepository.save(myMarginStock);
            } else {
                if(myMarginStock.getMinimumPrice() > minimumPrice){
                    myMarginStock.setMinimumPrice(minimumPrice);
                }
                myMarginStock.setAmount(myMarginStock.getAmount() + amount);
                myMarginStockRepository.save(myMarginStock);
            }
        } else if(companyId != null){
            MyMarginStock myMarginStock = myMarginStockRepository.findByTickerAndUserId(ticker, userId);
            if(myMarginStock == null){
                Ticker ticker1 = tickerRepository.findByTicker(ticker);
                myMarginStock = new MyMarginStock();
                myMarginStock.setTicker(ticker);
                myMarginStock.setUserId(null);
                myMarginStock.setCompanyId(companyId);
                myMarginStock.setAmount(amount);
                myMarginStock.setCurrencyMark(ticker1.getCurrencyName());
                myMarginStock.setMinimumPrice(minimumPrice);
                myMarginStockRepository.save(myMarginStock);
            } else {
                if(myMarginStock.getMinimumPrice() > minimumPrice){
                    myMarginStock.setMinimumPrice(minimumPrice);
                }
                myMarginStock.setAmount(myMarginStock.getAmount() + amount);
                myMarginStockRepository.save(myMarginStock);
            }
        }
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void removeAmountFromMyMarginStock(String ticker, Integer amount, Long userId, Long companyId) {
        if(userId != null){
            MyMarginStock myMarginStock = myMarginStockRepository.findByTickerAndUserId(ticker, userId);
            if(myMarginStock != null){
                myMarginStock.setAmount(myMarginStock.getAmount() - amount);

                if(myMarginStock.getAmount() == 0)
                    myMarginStockRepository.delete(myMarginStock);
                else
                    myMarginStockRepository.save(myMarginStock);
            }
        } else if(companyId != null){
            MyMarginStock myMarginStock = myMarginStockRepository.findByTickerAndCompanyId(ticker, companyId);
            if(myMarginStock != null){
                myMarginStock.setAmount(myMarginStock.getAmount() - amount);

                if(myMarginStock.getAmount() == 0)
                    myMarginStockRepository.delete(myMarginStock);
                else
                    myMarginStockRepository.save(myMarginStock);
            }
        }
    }

}
