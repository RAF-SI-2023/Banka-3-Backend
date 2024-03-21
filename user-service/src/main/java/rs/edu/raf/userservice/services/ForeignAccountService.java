package rs.edu.raf.userservice.services;

import org.springframework.stereotype.Service;
import rs.edu.raf.userservice.domains.dto.foreignaccount.ForeignAccountCreateDto;
import rs.edu.raf.userservice.domains.dto.foreignaccount.ForeignAccountDto;
import rs.edu.raf.userservice.domains.mappers.ForeignAccountMapper;
import rs.edu.raf.userservice.domains.model.ForeignAccount;
import rs.edu.raf.userservice.repositories.AccountTypeRepository;
import rs.edu.raf.userservice.repositories.CurrencyRepository;
import rs.edu.raf.userservice.repositories.ForeignAccountRepository;
import rs.edu.raf.userservice.repositories.UserRepository;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class ForeignAccountService {

    private final ForeignAccountRepository foreignAccountRepository;

    private final AccountTypeRepository accountTypeRepository;

    private final UserRepository userRepository;

    private final CurrencyRepository currencyRepository;

    public ForeignAccountService(ForeignAccountRepository foreignAccountRepository, AccountTypeRepository accountTypeRepository,
                                 UserRepository userRepository, CurrencyRepository currencyRepository) {
        this.foreignAccountRepository = foreignAccountRepository;
        this.accountTypeRepository = accountTypeRepository;
        this.userRepository = userRepository;
        this.currencyRepository = currencyRepository;
    }

    private String randAccNumber(){ //generise broj racuna
        String fixedPart = "5054791";
        StringBuilder builder = new StringBuilder(fixedPart);
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            int randomNumber = random.nextInt(10); // Generate a random number between 0-9
            builder.append(randomNumber);
        }
        return builder.toString();
    }

    public ForeignAccountDto create(ForeignAccountCreateDto facd, Long userId){
        ForeignAccount foreignAccount = new ForeignAccount();
        foreignAccount.setAccountNumber(randAccNumber());
        foreignAccount.setBalance(facd.getBalance());
        foreignAccount.setCurrency(currencyRepository.findByName(facd.getCurrency()).orElseThrow());
        foreignAccount.setAccountType(accountTypeRepository.findByName(facd.getAccountType()).orElseThrow());
        foreignAccount.setUser(userRepository.getReferenceById(userId));
        foreignAccount.setActive(true);
        foreignAccount.setCreationDate(System.currentTimeMillis());
        foreignAccount.setExpireDate(System.currentTimeMillis() + 31556952000L);
        return ForeignAccountMapper.INSTANCE.foreignAccountToForeignAccountDto(foreignAccountRepository.save(foreignAccount));
    }

    public void deactivate(Long id){
        ForeignAccount foreignAccount = foreignAccountRepository.findById(id).orElseThrow();
        foreignAccount.setActive(false);
        foreignAccountRepository.save(foreignAccount);
    }

    public ForeignAccountDto findByAccountNumber(String accountNumber){
        return ForeignAccountMapper.INSTANCE.foreignAccountToForeignAccountDto(foreignAccountRepository.findByAccountNumber(accountNumber).orElseThrow());
    }

    public List<ForeignAccountDto> findByUser(Long userId){
        List<ForeignAccount> accs = foreignAccountRepository.findByUserId(userId).orElseThrow();
        return accs.stream().map(ForeignAccountMapper.INSTANCE::foreignAccountToForeignAccountDto).collect(Collectors.toList());
    }

    public List<ForeignAccountDto> findAll(){
        return foreignAccountRepository.findAll().stream().map(ForeignAccountMapper.INSTANCE::foreignAccountToForeignAccountDto).collect(Collectors.toList());
    }
}
