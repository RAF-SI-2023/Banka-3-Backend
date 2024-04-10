package rs.edu.raf.exchangeservice.repository.myListingRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.edu.raf.exchangeservice.domain.model.myListing.MyFuture;

public interface MyFutureRepository extends JpaRepository<MyFuture, Long> {
    MyFuture findByContractName(String contractName);
}
