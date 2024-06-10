package rs.edu.raf.exchangeservice.service.myListingService;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import rs.edu.raf.exchangeservice.client.BankServiceClient;
import rs.edu.raf.exchangeservice.configuration.option.OptionUpdateEvent;
import rs.edu.raf.exchangeservice.domain.dto.bank.BankTransactionDto;
import rs.edu.raf.exchangeservice.domain.dto.buySell.SellOptionDto;
import rs.edu.raf.exchangeservice.domain.model.listing.Option;
import rs.edu.raf.exchangeservice.domain.model.myListing.MyOption;
import rs.edu.raf.exchangeservice.repository.listingRepository.OptionRepository;
import rs.edu.raf.exchangeservice.repository.myListingRepository.MyOptionRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MyOptionService {

    private final MyOptionRepository myOptionRepository;
    private final BankServiceClient bankServiceClient;
    private final OptionRepository optionRepository;
    private final ApplicationEventPublisher eventPublisher;

    public MyOption findByContractSymbol(String contractSymbol) {
        return this.myOptionRepository.findByContractSymbol(contractSymbol);
    }

    public List<MyOption> findAllByCompanyId(Long companyId) {
        return this.myOptionRepository.findAllByCompanyId(companyId);
    }

    public void sellOptionsToExchange(SellOptionDto sellOptionDto) {
        MyOption myOption = this.myOptionRepository.findByContractSymbol(sellOptionDto.getContractSymbol());
        if(myOption == null)
            throw new RuntimeException("MyOption not found");

        Option option = this.optionRepository.findByContractSymbol(sellOptionDto.getContractSymbol());
        option.setOpenInterest(option.getOpenInterest() + sellOptionDto.getQuantity());
        int quantity = sellOptionDto.getQuantity();
        double ask = option.getAsk();

        //provera da li imamo dovoljno
        if(quantity > myOption.getQuantity())
            throw new RuntimeException("Insufficient options");

        BankTransactionDto bankTransactionDto = new BankTransactionDto();
        bankTransactionDto.setCompanyId(sellOptionDto.getCompanyId());
        bankTransactionDto.setUserId(null);
        bankTransactionDto.setAmount(quantity * ask);
        bankTransactionDto.setCurrencyMark(myOption.getCurrencyMark());

        bankServiceClient.stockSellTransaction(bankTransactionDto);

        //oduzimanje prodate kolicine (eventualno brisanje)
        myOption.setQuantity(myOption.getQuantity() - quantity);
        if(myOption.getQuantity() == 0)
            myOptionRepository.delete(myOption);
        else
            myOptionRepository.save(myOption);

        optionRepository.save(option);
        eventPublisher.publishEvent(new OptionUpdateEvent(this, myOption));
    }
}
