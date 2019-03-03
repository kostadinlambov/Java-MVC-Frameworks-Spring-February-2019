package kl.web.controllers;

import kl.domain.models.binding.UserLoginBindingModel;
import kl.domain.models.binding.UserRegisterBindingModel;
import kl.domain.models.service.UserServiceModel;
import kl.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller
public class UserController {
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }


    @GetMapping("/register")
    public ModelAndView register(ModelAndView modelAndView, HttpSession session ){
        if(session.getAttribute("username") != null){
            modelAndView.setViewName("redirect:/home");
        }else{
            modelAndView.setViewName("register");
        }

        return modelAndView;
    }

    @PostMapping("/register")
    public ModelAndView registerPost(@ModelAttribute UserRegisterBindingModel userRegisterBindingModel,  ModelAndView modelAndView){
        if(!userRegisterBindingModel.getPassword().equals(userRegisterBindingModel.getConfirmPassword())){
            throw new IllegalArgumentException("Passwords don't match");
        }

        UserServiceModel userServiceModel = this.modelMapper.map(userRegisterBindingModel, UserServiceModel.class);
        UserServiceModel savedUserServiceModel = this.userService.userRegister(userServiceModel);


        if(savedUserServiceModel == null){
            throw new IllegalArgumentException("User registration failed!");
        }

        modelAndView.setViewName("redirect:/login");

        return modelAndView;
    }


    @GetMapping("/login")
    public ModelAndView login(ModelAndView modelAndView, HttpSession session){
        if(session.getAttribute("username") != null){
            modelAndView.setViewName("redirect:/home");
        }else{
            modelAndView.setViewName("login");
        }

        return modelAndView;
    }

    @PostMapping("/login")
    public ModelAndView loginPost(@ModelAttribute UserLoginBindingModel userLoginBindingModel, ModelAndView modelAndView, HttpSession session){
        UserServiceModel userServiceModel = this.modelMapper.map(userLoginBindingModel, UserServiceModel.class);
        UserServiceModel user = this.userService.userLogin(userServiceModel);

        if(user == null){
            throw new IllegalArgumentException("User registration failed!");
        }

        session.setAttribute("username", user.getUsername());
        session.setAttribute("id", user.getId());

        modelAndView.setViewName("redirect:/home");

        return modelAndView;
    }

    @GetMapping("/logout")
    public ModelAndView logout(ModelAndView modelAndView, HttpSession session){
        if(session.getAttribute("username") == null){
            modelAndView.setViewName("redirect:/login");
        }else{
            session.invalidate();
            modelAndView.setViewName("redirect:/");
        }
        return modelAndView;
    }
}
