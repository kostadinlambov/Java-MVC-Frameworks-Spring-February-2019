package org.kl.residentevil.web.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller
public class HomeController extends BaseController {

    @GetMapping("/")
    public ModelAndView home(HttpSession session) {
        if(session.getAttribute("role") != null){
            return super.redirect("/home");
        }
        return super.view("index");
    }

    @GetMapping("/home")
    public ModelAndView home(Authentication principal, ModelAndView modelAndView, HttpSession session) {
        String authority = principal
                .getAuthorities()
                .stream()
                .findFirst()
                .get()
                .getAuthority();

        System.out.println(authority);
        session.setAttribute("role", authority);
        session.setAttribute("currentUserName", principal.getName());
        modelAndView.addObject("currentUserName", principal.getName());
        return super.view("home", modelAndView);
    }


}
