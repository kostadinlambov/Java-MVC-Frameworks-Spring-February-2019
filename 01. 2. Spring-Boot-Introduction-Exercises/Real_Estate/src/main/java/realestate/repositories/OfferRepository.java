package realestate.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import realestate.domain.entities.Offer;

@Repository
public interface OfferRepository extends JpaRepository<Offer, String> {
    Offer findFirstByApartmentType(String apartmentType);
}
