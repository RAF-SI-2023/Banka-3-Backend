package rs.edu.raf.exchangeservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rs.edu.raf.exchangeservice.domain.dto.contract.ContractAnswerDto;
import rs.edu.raf.exchangeservice.domain.model.enums.BankCertificate;
import rs.edu.raf.exchangeservice.domain.model.enums.SellerCertificate;
import rs.edu.raf.exchangeservice.domain.model.listing.Future;
import rs.edu.raf.exchangeservice.domain.model.myListing.FutureContract;
import rs.edu.raf.exchangeservice.domain.model.myListing.MyFuture;
import rs.edu.raf.exchangeservice.repository.FutureContractRepository;
import rs.edu.raf.exchangeservice.repository.listingRepository.FutureRepository;
import rs.edu.raf.exchangeservice.repository.myListingRepository.MyFutureRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FutureContractService {
    private final FutureContractRepository futureContractRepository;
    private final MyFutureRepository myFutureRepository;

    public FutureContract findByFutureContractName(String contractName){
        return futureContractRepository.findByContractName(contractName);
    }

    //ugovori koje supervizor nije obradio
    public List<FutureContract> getAllUnresolvedContracts(){
        return this.futureContractRepository.findBySellerCertificateAndBankCertificate(SellerCertificate.ACCEPTED, BankCertificate.PROCESSING);
    }

    public List<FutureContract> getAllSentFutureContractsByCompanyId(Long companyId){
        return this.futureContractRepository.findByCompanyBuyerId(companyId);
    }
    public List<FutureContract> getAllReceivedFutureContractsByCompanyId(Long companyId){
        return this.futureContractRepository.findByCompanySellerId(companyId);
    }
    public List<FutureContract> getAllByCompanyId(Long companyId){
        return this.futureContractRepository.findByCompanySellerIdOrCompanyBuyerId(companyId, companyId);
    }

    public boolean companyAccept(ContractAnswerDto dto){
        FutureContract contract = futureContractRepository.findByFutureContractId(dto.getContractId());
        contract.setSellerCertificate(SellerCertificate.ACCEPTED);
        futureContractRepository.save(contract);
        return true;
    }

    public boolean companyDecline(ContractAnswerDto dto){
        FutureContract contract = futureContractRepository.findByFutureContractId(dto.getContractId());
        contract.setSellerCertificate(SellerCertificate.DECLINED);
        futureContractRepository.save(contract);
        return true;
    }
    public synchronized  boolean supervisorAccept(ContractAnswerDto dto){
        FutureContract contract = futureContractRepository.findByFutureContractId(dto.getContractId());

        if(contract.getBankCertificate() == BankCertificate.ACCEPTED){
            return true;
        }


//        MyFuture future = new MyFuture();
//        future.setContractName(contract.getContractName());
//        future.setCompanyId(contract.getCompanyBuyerId());
//        future.setPublicAmount(0);
//        future.setPrivateAmount(1);
//        future.setCurrencyMark("RSD");

        MyFuture future = myFutureRepository.findByContractName(contract.getContractName());
        future.setIsPublic(false);
        future.setCompanyId(contract.getCompanyBuyerId());
        future.setPrice(contract.getPrice());

        contract.setDateFinished(System.currentTimeMillis());
        contract.setBankCertificate(BankCertificate.ACCEPTED);

        myFutureRepository.save(future);
        futureContractRepository.save(contract);

        return true;
    }
    public synchronized boolean supervisorDecline(ContractAnswerDto dto){
        FutureContract contract = futureContractRepository.findByFutureContractId(dto.getContractId());

        if(contract.getBankCertificate() == BankCertificate.DECLINED){
            return true;
        }
        if(contract.getBankCertificate() == BankCertificate.ACCEPTED){
            return false;
        }
        contract.setBankCertificate(BankCertificate.DECLINED);
        contract.setComment(dto.getComment());
        contract.setDateFinished(System.currentTimeMillis());
        futureContractRepository.save(contract);
        return true;
    }
}
