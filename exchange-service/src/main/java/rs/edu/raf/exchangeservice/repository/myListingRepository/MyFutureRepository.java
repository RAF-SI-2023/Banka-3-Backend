package rs.edu.raf.exchangeservice.repository.myListingRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import rs.edu.raf.exchangeservice.domain.model.myListing.MyFuture;

import java.util.List;

@Repository
@Transactional(isolation = Isolation.SERIALIZABLE)
public interface MyFutureRepository extends JpaRepository<MyFuture, Long> {

    MyFuture findByContractName(String contractName);
    MyFuture findByMyFutureId(Long myFutureId);
    List<MyFuture> findAllByCompanyId(Long companyId);
    List<MyFuture> findByIsPublicTrueAndCompanyIdNot(Long companyId);
}
