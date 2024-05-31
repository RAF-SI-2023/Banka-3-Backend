package rs.edu.raf.exchangeservice.repository.offerRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import rs.edu.raf.exchangeservice.domain.model.offer.Offer;

@Repository
@Transactional(isolation = Isolation.SERIALIZABLE)
public interface OfferRepository extends JpaRepository<Offer, Long> {

}
