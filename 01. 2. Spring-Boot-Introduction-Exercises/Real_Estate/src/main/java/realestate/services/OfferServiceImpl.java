package realestate.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import realestate.domain.entities.Offer;
import realestate.domain.models.binding.OfferFindBindingModel;
import realestate.domain.models.service.OfferServiceModel;
import realestate.repositories.OfferRepository;
import realestate.util.HtmlReader;

import javax.validation.Validator;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OfferServiceImpl implements OfferService {
    private final String INDEX_PAGE_FILE_PATH = "C:\\Users\\valch\\Desktop\\Java-Web-Projects\\01. Java Web - January 2019\\02. Java MVC Frameworks - Spring-February 2019\\01. 2. Spring-Boot-Introduction-Exercises\\Real_Estate\\src\\main\\resources\\static\\index.html";

    private final OfferRepository offerRepository;
    private final ModelMapper modelMapper;
    private final Validator validator;
    private final HtmlReader htmlReader;

    @Autowired
    public OfferServiceImpl(OfferRepository offerRepository, ModelMapper modelMapper, Validator validator, HtmlReader htmlReader) {
        this.offerRepository = offerRepository;
        this.modelMapper = modelMapper;
        this.validator = validator;
        this.htmlReader = htmlReader;
    }

    @Override
    public void registerOffer(OfferServiceModel offerServiceModel) {
        if (this.validator.validate(offerServiceModel).size() != 0) {
            throw new IllegalArgumentException("Something went wrong by the creating of the offer!");
        }

        Offer offer = this.modelMapper.map(offerServiceModel, Offer.class);
        this.offerRepository.saveAndFlush(offer);
    }

    @Override
    public String findAllOffers() throws IOException {
        List<Offer> offers = this.offerRepository.findAll();

        String indexPage = htmlReader.readHtml(INDEX_PAGE_FILE_PATH);

        List<OfferServiceModel> offerServiceModels = offers
                .stream()
                .map(offer -> this.modelMapper
                        .map(offer, OfferServiceModel.class))
                .collect(Collectors.toList());

        if (offerServiceModels.size() > 0) {
            System.out.println();
            StringBuilder apartments = new StringBuilder();

            offerServiceModels.forEach(offerServiceModel -> {
                String currentApartment = String.format(" <div class=\"apartment\">\n" +
                                "\t\t<p>Rent: %s</p>\n" +
                                "\t\t<p>Type: %s</p>\n" +
                                "\t\t<p>Commission: %s</p>\n" +
                                "\t</div>",
                        offerServiceModel.getApartmentRent(),
                        offerServiceModel.getApartmentType(),
                        offerServiceModel.getAgencyCommission());

                apartments.append(currentApartment);
            });

            indexPage = indexPage.replace(
                    "{{CHANGE_INDEX_HTML_PATH}}",
                    apartments.toString());
        } else {
            indexPage = indexPage.replace(
                    "{{CHANGE_INDEX_HTML_PATH}}",
                    "<div class=\"apartment no-offers\"> There aren't any offers!</div>");
        }

        return indexPage;
    }

    @Override
    public void findOffer(OfferFindBindingModel offerFindBindingModel) {
        if(this.validator.validate(offerFindBindingModel).size() > 0){
            throw new IllegalArgumentException("Something went wrong by validating the offer!");
        }

        String apartmentType = offerFindBindingModel.getApartmentType();
        Offer offerByApartmentType = this.offerRepository.findFirstByApartmentType(apartmentType);

        if(offerByApartmentType == null ){
            throw new IllegalArgumentException("Something went wrong by searching the offer!");
        }

        BigDecimal sumOfCommissionAndRent = offerByApartmentType.getApartmentRent()
                .add(offerByApartmentType.getApartmentRent()
                        .multiply((
                                offerByApartmentType.getAgencyCommission()
                                        .divide(new BigDecimal("100")))));

        if(offerFindBindingModel.getFamilyBudget().compareTo(sumOfCommissionAndRent) < 0){
            throw new IllegalArgumentException("Budget is not enough!");
        }

        try{
            this.offerRepository.delete(offerByApartmentType);
        }catch (Exception e){
            throw new IllegalArgumentException("Something went wrong by deleting the offer!");
        }
    }
}
