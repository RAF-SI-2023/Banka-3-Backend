package rs.edu.raf.userservice.services;

import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import rs.edu.raf.userservice.domains.model.AccountActivation;
import rs.edu.raf.userservice.repositories.AccountActivationRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AccountActivationService {
    private AccountActivationRepository accountActivationRepository;
    private TaskScheduler taskScheduler;
    private EmailService emailService;

    public AccountActivationService(AccountActivationRepository accountActivationRepository, TaskScheduler taskScheduler, EmailService emailService) {
        this.accountActivationRepository = accountActivationRepository;
        this.taskScheduler = taskScheduler;
        this.emailService = emailService;
    }

    /**
     * Funkcija vraca sve Entitete iz baze tipa AccountActivation.
     * Moze se obrisati kasnije.
     */
    public List<AccountActivation> allAccountActivations(){
        return accountActivationRepository.findAll();
    }

    /**
     * Za prosledjeni kod i email adresu, funckija vraca true ukoliko
     * je kod aktivan, ili false ako nije aktivan ili nepostoji.
     */
    public boolean findByEmailAndCode(String email, int code){
        Optional<AccountActivation> accountActivation = accountActivationRepository.findByEmailAndCode(email, code);
        if (accountActivation.isPresent()){
            return accountActivation.get().isActive();
        }
        return false;
    }

    /**
     * Za prosledjeni email pravi novi entitet tipa Account Activation
     * Generise random sestocifreni broj, i salje ga na mail.
     * Pored toga, postavlja i Schedule operaciju da kod postane neaktivan
     * posle 5 minuta.
     * Vraca kompletan objekat tipa AccountActivation
     */
    public AccountActivation addAccountActivation(AccountActivation accountActivation){
        AccountActivation activation = accountActivationRepository.save(accountActivation);
        scheduleDeactivation(activation.getId());   //zakazivanje deaktivacije koda
        emailService.sendSimpleMessage(activation.getEmail(), "Aktivacija naloga za Banku3","Vas kod za aktivaciju naloga je: " + activation.getCode()); //slanje koda na Mail
        return activation;
    }

    //zakazivanje deaktivacije koda
    private void scheduleDeactivation(Long id){
        taskScheduler.schedule(() -> {
            Optional<AccountActivation> accountActivation = accountActivationRepository.findById(id);
            if (accountActivation.isPresent()){
                AccountActivation activation = accountActivation.get();
                activation.setActive(false);
                accountActivationRepository.save(activation);
            }
        }, new Date(System.currentTimeMillis() + 300000));
    }
}
