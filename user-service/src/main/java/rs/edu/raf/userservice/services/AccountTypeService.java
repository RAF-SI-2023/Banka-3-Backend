package rs.edu.raf.userservice.services;

import org.springframework.stereotype.Service;
import rs.edu.raf.userservice.domains.model.AccountType;
import rs.edu.raf.userservice.repositories.AccountTypeRepository;

import java.util.List;

@Service
public class AccountTypeService {

    private final AccountTypeRepository accountTypeRepository;

    public AccountTypeService(AccountTypeRepository accountTypeRepository) {
        this.accountTypeRepository = accountTypeRepository;
    }

    public List<AccountType> findAll() {
        return accountTypeRepository.findAll();
    }
}
