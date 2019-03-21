package org.kl.residentevil.web.controllers;

import org.kl.residentevil.domain.models.binding.VirusAddBindingModel;
import org.kl.residentevil.domain.models.binding.VirusEditBindingModel;
import org.kl.residentevil.domain.models.service.VirusServiceModel;
import org.kl.residentevil.domain.models.view.CapitalListViewModel;
import org.kl.residentevil.domain.models.view.VirusAllViewModel;
import org.kl.residentevil.domain.models.view.VirusDeleteViewModel;
import org.kl.residentevil.domain.models.view.VirusEditViewModel;
import org.kl.residentevil.services.CapitalService;
import org.kl.residentevil.services.VirusService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/viruses")
public class VirusController extends BaseController {
    private final CapitalService capitalService;
    private final VirusService virusService;
    private final ModelMapper modelMapper;

    public VirusController(CapitalService capitalService, VirusService virusService, ModelMapper modelMapper) {
        this.capitalService = capitalService;
        this.virusService = virusService;
        this.modelMapper = modelMapper;
    }


    @ModelAttribute("bindingModel")
    public VirusAddBindingModel createBindingModel() {
        return new VirusAddBindingModel();
    }


    @GetMapping("/add")
    public ModelAndView add(ModelAndView modelAndView, @ModelAttribute(name = "bindingModel") VirusAddBindingModel bindingModel) {

        modelAndView.addObject("bindingModel", bindingModel);
        modelAndView.addObject("capitals", getCapitals());

        return super.view("add-virus", modelAndView);
    }

    @PostMapping("/add")
    public ModelAndView addConfirm(@Valid @ModelAttribute(name = "bindingModel") VirusAddBindingModel bindingModel, BindingResult bindingResult, ModelAndView modelAndView) {
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("bindingModel", bindingModel);
            modelAndView.addObject("capitals", getCapitals());
            return super.view("add-virus", modelAndView);
        }


        boolean result = this.virusService.createVirus(bindingModel);

        if (result) {
            return super.redirect("/");
        }

        throw new IllegalArgumentException("Something went wrong!");

    }


    @GetMapping("/show")
    public ModelAndView allViruses(ModelAndView modelAndView) {
        List<VirusServiceModel> virusServiceModels = this.virusService.getAllViruses();

        if (virusServiceModels.size() > 0) {
            List<VirusAllViewModel> documentViewAllModelList = virusServiceModels.stream()
                    .map(virusServiceModel -> this.modelMapper
                            .map(virusServiceModel, VirusAllViewModel.class))
                    .collect(Collectors.toList());

            modelAndView.addObject("viruses", documentViewAllModelList);


        } else {
            modelAndView.addObject("viruses", new ArrayList<>());

        }

        return super.view("display-all-viruses", modelAndView);
    }

    @GetMapping("/delete/{id}")
    public ModelAndView deleteGet(@PathVariable String id, ModelAndView modelAndView) {
        VirusServiceModel virusById = this.virusService.findVirusById(id);

        VirusDeleteViewModel virusDeleteViewModel = this.modelMapper.map(virusById, VirusDeleteViewModel.class);

        modelAndView.addObject("virusToDelete", virusDeleteViewModel);
        modelAndView.setViewName("delete-virus");

        return modelAndView;
    }

    @PostMapping("/delete/{id}")
    public ModelAndView deletePost(@PathVariable String id, ModelAndView modelAndView) {
        boolean result = this.virusService.removeVirus(id);

        if (!result) {
            throw new IllegalArgumentException("Something went wrong");
        }

        modelAndView.setViewName("redirect:/");

        return modelAndView;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView editGet(@PathVariable String id,
                                ModelAndView modelAndView,
                                @ModelAttribute(name = "virusToEdit") VirusEditBindingModel virusToEdit) {
//        VirusServiceModel virusById = this.virusService.findVirusById(id);
//
//        VirusEditViewModel virusEditViewModel = this.modelMapper.map(virusById, VirusEditViewModel.class);
//
//        modelAndView.addObject("virusToEdit", virusEditViewModel);
//        modelAndView.setViewName("edit-virus");
        modelAndView.setViewName("/index");
        return modelAndView;
    }

    @PostMapping("/edit/{id}")
    public ModelAndView editPost(@PathVariable String id,
                                 ModelAndView modelAndView,
                                 @Valid @ModelAttribute(name = "virusToEdit") VirusEditBindingModel virusToEdit,
                                 BindingResult bindingResult) {

//        if (bindingResult.hasErrors()) {
//            modelAndView.addObject("virusToEdit", virusToEdit);
//            return super.view("edit-virus", modelAndView);
//        }
//
//        boolean result = this.virusService.editVirus(virusToEdit);
//
//        if (!result) {
//            throw new IllegalArgumentException("Something went wrong");
//        }

        modelAndView.setViewName("redirect:/");

        return modelAndView;
    }


    private List<CapitalListViewModel> getCapitals() {
        return this.capitalService
                .findAllCapitals()
                .stream()
                .map(capital -> this.modelMapper.map(capital, CapitalListViewModel.class))
                .collect(Collectors.toList());
    }
}
