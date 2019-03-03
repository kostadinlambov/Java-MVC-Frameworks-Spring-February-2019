package kl.web.controllers;

import kl.domain.models.binding.DocumentCreateBindingModel;
import kl.domain.models.service.DocumentServiceModel;
import kl.domain.models.view.DocumentViewModel;
import kl.services.DocumentService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller
public class DocumentController {
    private final ModelMapper modelMapper;
    private final DocumentService documentService;

    public DocumentController(ModelMapper modelMapper, DocumentService documentService) {
        this.modelMapper = modelMapper;
        this.documentService = documentService;
    }

    @GetMapping("/schedule")
    public ModelAndView schedule(ModelAndView modelAndView, HttpSession session){
        if(session.getAttribute("username") == null){
            modelAndView.setViewName("redirect:/login");
        }else{
            modelAndView.setViewName("schedule");
        }

        return modelAndView;
    }

    @PostMapping("/schedule")
    public ModelAndView schedulePost(@ModelAttribute DocumentCreateBindingModel documentCreateBindingModel, ModelAndView modelAndView){

        DocumentServiceModel documentServiceModel = this.modelMapper.map(documentCreateBindingModel, DocumentServiceModel.class);

        try{
            DocumentServiceModel savedDocument = this.documentService.save(documentServiceModel);
            modelAndView.setViewName("redirect:/details/" + savedDocument.getId());
        }catch (Exception e){
            throw new IllegalArgumentException("Document creation failed!");
        }

        return modelAndView;
    }


    @GetMapping("/details/{id}")
    public ModelAndView details(@PathVariable("id") String id, ModelAndView modelAndView, HttpSession session){
        if(session.getAttribute("username") == null){
            modelAndView.setViewName("redirect:/login");
        }else{
            DocumentViewModel documentById = findDocumentById(id);
            modelAndView.addObject("model", documentById);
            modelAndView.setViewName("details");
        }

        return modelAndView;
    }

    @GetMapping("/print/{id}")
    public ModelAndView printGet(@PathVariable String id, ModelAndView modelAndView, HttpSession session){
        if(session.getAttribute("username") == null){
            modelAndView.setViewName("redirect:/login");
        }else{
            DocumentViewModel documentById = findDocumentById(id);
            modelAndView.addObject("documentToPrint", documentById);
            modelAndView.setViewName("print");
        }

        return modelAndView;
    }

    @PostMapping("/print/{id}")
    public ModelAndView printPost(@PathVariable String id, ModelAndView modelAndView){
        boolean result = this.documentService.removeDocument(id);

        if(!result){
            throw new IllegalArgumentException("Something went wrong");
        }

        modelAndView.setViewName("redirect:/home");

        return modelAndView;
    }

    private DocumentViewModel findDocumentById(String id){
        DocumentServiceModel documentServiceById = this.documentService.findById(id);

        DocumentViewModel documentViewModel = this.modelMapper.map(documentServiceById, DocumentViewModel.class);

        if(documentViewModel == null){
            throw new IllegalArgumentException("Document not found!");
        }

        return documentViewModel;
    }
}
