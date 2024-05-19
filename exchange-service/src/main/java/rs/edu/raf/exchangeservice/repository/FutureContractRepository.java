package rs.edu.raf.exchangeservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.edu.raf.exchangeservice.domain.model.enums.BankCertificate;
import rs.edu.raf.exchangeservice.domain.model.listing.Future;
import rs.edu.raf.exchangeservice.domain.model.myListing.FutureContract;

import java.util.List;

public interface FutureContractRepository extends JpaRepository<FutureContract, Long> {
    FutureContract findByFutureContractId(Long id);
    FutureContract findByContractName(String contractName);
    List<FutureContract> findByCompanySellerId(Long id);
    List<FutureContract> findByCompanyBuyerId(Long id);
    List<FutureContract> findByCompanySellerIdOrCompanyBuyerId(Long id);
    List<FutureContract> findByBankCertificate(BankCertificate bankCertificate);

}
