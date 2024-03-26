package rs.edu.raf.userservice.services;

import org.springframework.stereotype.Service;
import rs.edu.raf.userservice.domains.dto.credit.CreateCreditDto;
import rs.edu.raf.userservice.domains.dto.credit.CreditDto;
import rs.edu.raf.userservice.domains.mappers.CreditMapper;
import rs.edu.raf.userservice.domains.model.Credit;
import rs.edu.raf.userservice.domains.model.User;
import rs.edu.raf.userservice.repositories.CreditRepository;
import rs.edu.raf.userservice.repositories.UserRepository;

import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CreditService {

    private final CreditRepository creditRepository;

    private final UserRepository userRepository;

    public CreditService(CreditRepository creditRepository, UserRepository userRepository) {
        this.creditRepository = creditRepository;
        this.userRepository = userRepository;
    }

    public List<CreditDto> findAll() {
        return creditRepository.findAll().stream().map(CreditMapper.INSTANCE::creditToCreditDto)
                .collect(Collectors.toList());
    }

    public CreditDto findById(Long id) {
        return creditRepository.findById(id).map(CreditMapper.INSTANCE::creditToCreditDto).orElseThrow();
    }

    //TODO - increase account balance by credit amount
    public CreditDto createCredit(CreateCreditDto createCreditDto) {
        Credit credit = CreditMapper.INSTANCE.createCreditDtoToCredit(createCreditDto);
        User user = userRepository.findById(createCreditDto.getUserId()).orElseThrow();
        credit.setUser(user);
        credit.setFee(0.05);
        credit.setStartDate(System.currentTimeMillis());
        credit.setEndDate(calculateEndDate(credit.getStartDate(), credit.getPaymentPeriod()));
        credit.setMonthlyFee((credit.getAmount() * (1 + credit.getFee())) / credit.getPaymentPeriod());
        credit.setRemainingAmount(credit.getAmount() * (1 + credit.getFee()));

        creditRepository.save(credit);

        return CreditMapper.INSTANCE.creditToCreditDto(credit);
    }

    private long calculateEndDate(long startDate, int paymentPeriod) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(startDate);

        calendar.add(Calendar.MONTH, paymentPeriod);

        return calendar.getTimeInMillis();
    }


}
