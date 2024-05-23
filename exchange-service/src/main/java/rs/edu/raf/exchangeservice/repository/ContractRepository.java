package rs.edu.raf.exchangeservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.edu.raf.exchangeservice.domain.model.enums.BankCertificate;
import rs.edu.raf.exchangeservice.domain.model.enums.SellerCertificate;
import rs.edu.raf.exchangeservice.domain.model.myListing.Contract;

import java.util.List;

public interface ContractRepository extends JpaRepository<Contract, Long> {

    Contract findByContractId(Long id);

    //Firma koja prodaje treba da vidi sve ugovore koje su joj poslale druge firme
    List<Contract>findByCompanySellerId(Long id);

    List<Contract>findByCompanyBuyerId(Long id);

    List<Contract> findByUserSellerId(Long id);

    List<Contract> findByUserBuyerId(Long id);

    List<Contract> findByCompanySellerIdOrCompanyBuyerId(Long id, Long id2);
    List<Contract> findBySellerCertificateAndBankCertificate(SellerCertificate sellerCertificate, BankCertificate bankCertificate);
}
