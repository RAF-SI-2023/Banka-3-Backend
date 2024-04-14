package rs.edu.raf.exchangeservice.repository.listingRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.edu.raf.exchangeservice.domain.model.listing.Future;
import rs.edu.raf.exchangeservice.domain.model.myListing.MyFuture;

import java.util.Optional;


public interface FutureRepository extends JpaRepository<Future, Long> {
    Optional<Future> findByContractName(String contractName);
}
