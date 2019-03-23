package org.kl.residentevil.web.controllers;

import org.kl.residentevil.domain.models.binding.UserRegisterBindingModel;
import org.kl.residentevil.domain.models.service.UserServiceModel;
import org.kl.residentevil.domain.models.view.UserAllViewModel;
import org.kl.residentevil.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class UserController extends BaseController {

    private final UserService userService;
    private final ModelMapper modelMapper;


    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/login")
    public ModelAndView login(ModelAndView modelAndView) {
        modelAndView.setViewName("/login");
        return modelAndView;
//        return super.view("login", modelAndView);
    }

    @GetMapping("/register")
    public ModelAndView registerGet(ModelAndView modelAndView) {
        return super.view("register", modelAndView);
    }

    @PostMapping("/register")
    public ModelAndView registerPost(@ModelAttribute UserRegisterBindingModel userRegisterBindingModel, ModelAndView modelAndView) {

        UserServiceModel userServiceModel = this.modelMapper.map(userRegisterBindingModel, UserServiceModel.class);
        boolean result = this.userService.register(userServiceModel);

        return super.redirect("/login");
    }


    @GetMapping("/users/all")
    public ModelAndView getAllUsers(ModelAndView modelAndView) {

        List<UserAllViewModel> usersList = this.userService
                .getAll()
                .stream()
                .map(userServiceModel -> this.modelMapper.map(userServiceModel, UserAllViewModel.class))
                .collect(Collectors.toList());

        modelAndView.addObject("users", usersList);

        return super.view("users-all", modelAndView);
    }


    @GetMapping("/users/role")
    public ModelAndView getAllUsersWithRole(ModelAndView modelAndView) {
        List<UserAllViewModel> usersList = this.getAllUsers();
        modelAndView.addObject("users", usersList);

        return super.view("users-roles", modelAndView);
    }

    @GetMapping("/users/promote/{id}")
    public ModelAndView promote(@PathVariable(value = "id") String id, ModelAndView modelAndView) {
        try{
            boolean result = this.userService.promote(id);
        }catch (Exception e){
            e.printStackTrace();
        }
        List<UserAllViewModel> usersList = this.getAllUsers();
        modelAndView.addObject("users", usersList);

        return super.view("users-roles", modelAndView);
    }

    @GetMapping("/users/demote/{id}")
    public ModelAndView demote(@PathVariable(value = "id") String id, ModelAndView modelAndView) {
        try{
            boolean result = this.userService.demote(id);
        }catch (Exception e){
            e.printStackTrace();
        }
        List<UserAllViewModel> usersList = this.getAllUsers();
        modelAndView.addObject("users", usersList);

        return super.view("users-roles", modelAndView);
    }


    private List<UserAllViewModel> getAllUsers() {
        return this.userService
                .getAll()
                .stream()
                .map(userServiceModel -> {
                    UserAllViewModel userAllViewModel = this.modelMapper.map(userServiceModel, UserAllViewModel.class);
                    userAllViewModel.setRole(userServiceModel.extractAuthority());
                    return userAllViewModel;
                })
                .collect(Collectors.toList());
    }

}
