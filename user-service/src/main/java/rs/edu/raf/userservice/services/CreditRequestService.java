package rs.edu.raf.userservice.services;

import org.springframework.stereotype.Service;
import rs.edu.raf.userservice.domains.dto.credit.CreateCreditDto;
import rs.edu.raf.userservice.domains.dto.creditrequest.CreditRequestCreateDto;
import rs.edu.raf.userservice.domains.dto.creditrequest.CreditRequestDto;
import rs.edu.raf.userservice.domains.dto.creditrequest.ProcessCreditRequestDto;
import rs.edu.raf.userservice.domains.mappers.CreditRequestMapper;
import rs.edu.raf.userservice.domains.model.CreditRequest;
import rs.edu.raf.userservice.domains.model.User;
import rs.edu.raf.userservice.domains.model.enums.CreditRequestStatus;
import rs.edu.raf.userservice.repositories.CreditRequestRepository;
import rs.edu.raf.userservice.repositories.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CreditRequestService {

    private final CreditRequestRepository creditRequestRepository;
    private final CreditService creditService;
    private final UserRepository userRepository;

    public CreditRequestService(CreditRequestRepository creditRequestRepository, UserRepository userRepository, CreditService creditService) {
        this.creditRequestRepository = creditRequestRepository;
        this.userRepository = userRepository;
        this.creditService = creditService;
    }

    public List<CreditRequestDto> findAll() {
        return creditRequestRepository.findAll().stream().map(CreditRequestMapper.INSTANCE::creditRequestToCreditRequestDto)
                .collect(Collectors.toList());
    }

    public CreditRequestDto findById(Long id) {
        return creditRequestRepository.findById(id).map(CreditRequestMapper.INSTANCE::creditRequestToCreditRequestDto).orElseThrow();
    }

    public CreditRequestDto createCreditRequest(CreditRequestCreateDto creditRequestCreateDto) {
        CreditRequest creditRequest =
                CreditRequestMapper.INSTANCE.creditRequestCreateDtoToCreditRequest(creditRequestCreateDto);
        User user = userRepository.findById(creditRequestCreateDto.getUserId()).orElseThrow();
        creditRequest.setUser(user);
        creditRequest.setStatus(CreditRequestStatus.PROCESSING);

        creditRequestRepository.save(creditRequest);

        return CreditRequestMapper.INSTANCE.creditRequestToCreditRequestDto(creditRequest);
    }

    public CreditRequestDto processCreditRequest(ProcessCreditRequestDto processCreditRequestDto) {
        CreditRequest creditRequest =
                creditRequestRepository.findById(processCreditRequestDto.getCreditRequestId()).orElseThrow();
        if (creditRequest.getStatus().equals(CreditRequestStatus.PROCESSING)) {
            if (processCreditRequestDto.getAccepted())
                creditRequest.setStatus(CreditRequestStatus.ACCEPTED);
            //napraviti kredit u bazi
            CreditRequest cr = creditRequestRepository.findById(processCreditRequestDto.getCreditRequestId()).get();
            CreateCreditDto createCreditDto = new CreateCreditDto();
            createCreditDto.setUserId(cr.getUser().getUserId());
            createCreditDto.setName(cr.getName());
            createCreditDto.setAccountNumber(cr.getAccountNumber());
            createCreditDto.setCurrencyMark(cr.getCurrencyMark());
            createCreditDto.setAmount(cr.getAmount());
            createCreditDto.setPaymentPeriod(cr.getPaymentPeriod());
            creditService.createCredit(createCreditDto);
        }else{
            creditRequest.setStatus(CreditRequestStatus.DECLINED);
        }

        creditRequestRepository.save(creditRequest);

        return CreditRequestMapper.INSTANCE.creditRequestToCreditRequestDto(creditRequest);
    }

}
