package kl.web.controllers;

import kl.domain.models.service.DocumentServiceModel;
import kl.domain.models.view.DocumentViewAllModel;
import kl.services.DocumentService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController {
    private final ModelMapper modelMapper;
    private final DocumentService documentService;

    public HomeController(ModelMapper modelMapper, DocumentService documentService) {
        this.modelMapper = modelMapper;
        this.documentService = documentService;
    }

    @GetMapping("/")
    public ModelAndView modelAndView(ModelAndView modelAndView, HttpSession session){
        if(session.getAttribute("username") != null){
            modelAndView.setViewName("redirect:/home");
        }else{
            modelAndView.setViewName("index");
        }
        return modelAndView;
    }

    @GetMapping("/home")
    public ModelAndView home(ModelAndView modelAndView, HttpSession session){
        if(session.getAttribute("username") == null){
            modelAndView.setViewName("redirect:/login");
        }else{
            modelAndView.setViewName("home");
        }

        List<DocumentServiceModel> documentServiceModels = this.documentService
                .allDocuments();

        if(documentServiceModels.size() > 0){
            List<DocumentViewAllModel> documentViewAllModelList = documentServiceModels.stream()
                    .map(documentServiceModel -> this.modelMapper
                            .map(documentServiceModel, DocumentViewAllModel.class))
                    .collect(Collectors.toList());

            modelAndView.addObject("documents", documentViewAllModelList);

        }else{
            modelAndView.addObject("documents", new ArrayList<>());

        }

        return modelAndView;
    }
}
