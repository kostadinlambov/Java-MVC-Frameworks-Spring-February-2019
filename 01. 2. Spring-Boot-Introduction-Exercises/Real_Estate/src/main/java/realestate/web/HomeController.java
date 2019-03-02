package realestate.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import realestate.services.OfferService;

import java.io.IOException;

@Controller
public class HomeController {
    private final OfferService offerService;

    @Autowired
    public HomeController(OfferService offerService) {
        this.offerService = offerService;
    }

    @GetMapping("/")
    @ResponseBody
    public String index() throws IOException {
        return this.offerService.findAllOffers();
    }
}
