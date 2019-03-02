package realestate.services;

import realestate.domain.models.binding.OfferFindBindingModel;
import realestate.domain.models.service.OfferServiceModel;

import java.io.IOException;

public interface OfferService {
    void registerOffer(OfferServiceModel offerServiceModel);

    String findAllOffers() throws IOException;

    void findOffer(OfferFindBindingModel offerFindBindingModel);
}
