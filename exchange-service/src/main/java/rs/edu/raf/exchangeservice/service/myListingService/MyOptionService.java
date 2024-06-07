package rs.edu.raf.exchangeservice.service.myListingService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rs.edu.raf.exchangeservice.client.BankServiceClient;
import rs.edu.raf.exchangeservice.domain.dto.bank.BankTransactionDto;
import rs.edu.raf.exchangeservice.domain.dto.buySell.SellOptionDto;
import rs.edu.raf.exchangeservice.domain.model.myListing.MyOption;
import rs.edu.raf.exchangeservice.repository.myListingRepository.MyOptionRepository;

@Service
@RequiredArgsConstructor
public class MyOptionService {

    private final MyOptionRepository myOptionRepository;
    private final BankServiceClient bankServiceClient;

    public MyOption findByContractSymbol(String contractSymbol) {
        return this.myOptionRepository.findByContractSymbol(contractSymbol);
    }

    public void sellOptionsToExchange(SellOptionDto sellOptionDto) {
        MyOption myOption = this.myOptionRepository.findByContractSymbol(sellOptionDto.getContractSymbol());
        if(myOption == null)
            throw new RuntimeException("MyOption not found");

        int quantity = sellOptionDto.getQuantity();
        double ask = myOption.getAsk();

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

    }
}
