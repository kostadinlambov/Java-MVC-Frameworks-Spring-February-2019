package org.kl.residentevil.web.controllers;

import org.kl.residentevil.domain.models.view.CapitalListViewModel;
import org.kl.residentevil.services.CapitalService;
import org.kl.residentevil.services.VirusService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@Controller
@RequestMapping("/capitals")
public class CapitalController extends BaseController {
    private final CapitalService capitalService;
    private final VirusService virusService;
    private final ModelMapper modelMapper;

    public CapitalController(CapitalService capitalService, VirusService virusService, ModelMapper modelMapper) {
        this.capitalService = capitalService;
        this.virusService = virusService;
        this.modelMapper = modelMapper;
    }




    @GetMapping("/all")
    public @ResponseBody Object fetchViruses() {
        return this.capitalService
                .findAllCapitals()
                .stream()
                .map(capital -> this.modelMapper.map(capital, CapitalListViewModel.class))
                .collect(Collectors.toList());
    }

}
