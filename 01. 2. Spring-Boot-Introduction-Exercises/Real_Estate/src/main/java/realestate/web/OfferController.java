package realestate.web;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import realestate.domain.models.binding.OfferFindBindingModel;
import realestate.domain.models.binding.OfferRegisterBindingModel;
import realestate.domain.models.service.OfferServiceModel;
import realestate.services.OfferService;

@Controller
public class OfferController {
    private final OfferService offerService;
    private final ModelMapper modelMapper;

    @Autowired
    public OfferController(OfferService offerService, ModelMapper modelMapper) {
        this.offerService = offerService;
        this.modelMapper = modelMapper;
    }

    @GetMapping(value = "/reg")
    public String registerGet() {
        return "register.html";
    }

    @PostMapping(value = "/reg")
    public String registerPost(@ModelAttribute(name = "model") OfferRegisterBindingModel offerRegisterBindingModel) {
        try {
            OfferServiceModel offerServiceModel = this.modelMapper.map(offerRegisterBindingModel, OfferServiceModel.class);
             this.offerService.registerOffer(offerServiceModel);
        } catch (IllegalArgumentException iae) {
            iae.printStackTrace();
            return "redirect:/reg";
        }

        return "redirect:/";
    }

    @GetMapping(value = "/find")
    public String findApartmentGet(){
        return "find.html";
    }

    @PostMapping(value = "/find")
    public String findApartmentPost(@ModelAttribute(name = "model") OfferFindBindingModel offerFindBindingModel){
        try{
            this.offerService.findOffer(offerFindBindingModel);
        }catch(IllegalArgumentException iae){
            iae.printStackTrace();
            return "redirect:/find";
        }

        return "redirect:/";
    }
}
