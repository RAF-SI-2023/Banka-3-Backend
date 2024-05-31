package rs.edu.raf.exchangeservice.service.myListingService;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import rs.edu.raf.exchangeservice.domain.dto.bank.BankTransactionDto;
import rs.edu.raf.exchangeservice.domain.dto.buySell.BuyForexDto;
import rs.edu.raf.exchangeservice.domain.model.enums.OrderStatus;
import rs.edu.raf.exchangeservice.domain.model.listing.Forex;
import rs.edu.raf.exchangeservice.domain.model.myListing.MyForex;
import rs.edu.raf.exchangeservice.domain.model.order.ForexOrder;
import rs.edu.raf.exchangeservice.domain.model.order.FutureOrder;
import rs.edu.raf.exchangeservice.repository.listingRepository.ForexRepository;
import rs.edu.raf.exchangeservice.repository.myListingRepository.MyForexRepositroy;
import rs.edu.raf.exchangeservice.repository.orderRepository.ForexOrderRepository;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@RequiredArgsConstructor
public class MyForexService {
    private final MyForexRepositroy myForexRepository;
    private final ForexRepository forexRepository;
    private final ForexOrderRepository forexOrderRepository;

    public CopyOnWriteArrayList<ForexOrder> ordersToBuy = new CopyOnWriteArrayList<>();

    public List<ForexOrder> findAll(){
        return forexOrderRepository.findAll();
    }

    public List<ForexOrder> findAllByCompanyId(Long companyId){
        return forexOrderRepository.findAllByCompanyId(companyId);
    }

    public List<MyForex> findAllMyForexByCompanyId(Long companyId){
        return myForexRepository.findAllByCompanyId(companyId);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void addAmount(Long companyId, String quoteCurrency, Double amount){
        MyForex myForex = myForexRepository.findByCompanyIdAndQuoteCurrency(companyId, quoteCurrency);
        if(myForex == null) {
            myForex = new MyForex();
            myForex.setCompanyId(companyId);
            myForex.setQuoteCurrency(quoteCurrency);
            myForex.setAmount(amount);
        } else {
            myForex.setAmount(myForex.getAmount() + amount);
        }
        myForexRepository.save(myForex);
    }

    public ForexOrder buyForex(BuyForexDto buyForexDto){
        ForexOrder forexOrder = new ForexOrder();
        forexOrder.setCompanyId(buyForexDto.getCompanyId());

        Forex forex = forexRepository.findByForexId(buyForexDto.getForexId());
        forexOrder.setQuoteCurrency(forex.getQuoteCurrency());
        forexOrder.setStatus(OrderStatus.PROCESSING);
        forexOrder.setAmount(buyForexDto.getAmount());

        ordersToBuy.add(forexOrder);
        return forexOrder;
    }

    @Scheduled(fixedRate = 10000)
    public void executeOrders(){
        if(!ordersToBuy.isEmpty()) {
            Random random = new Random();
            int forexNumber = random.nextInt(ordersToBuy.size());

            ForexOrder forexOrder = ordersToBuy.get(forexNumber);

            Forex forex = forexRepository.findByQuoteCurrency(forexOrder.getQuoteCurrency());

            this.addAmount(forexOrder.getCompanyId(), forexOrder.getQuoteCurrency(), forexOrder.getAmount() * forex.getConversionRate());

            BankTransactionDto bankTransactionDto = new BankTransactionDto();
            bankTransactionDto.setAmount(forexOrder.getAmount());
            bankTransactionDto.setCurrencyMark("RSD");
            bankTransactionDto.setUserId(null);
            bankTransactionDto.setCompanyId(forexOrder.getCompanyId());
            bankTransactionDto.setEmployeeId(null);
            //todo dodati poziv ka bank servisu

            forexOrder.setStatus(OrderStatus.FINISHED);
            forexOrderRepository.save(forexOrder);
            this.ordersToBuy.remove(forexOrder);
        }
    }
}
