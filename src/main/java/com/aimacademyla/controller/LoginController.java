package com.aimacademyla.controller;

import org.springframework.http.HttpRequest;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by davidkim on 1/24/17.
 */

@Controller
public class LoginController {

    @RequestMapping("/")
    public String landingPage(){
        return "redirect:/admin/home";
    }

    @RequestMapping("/login")
    public String login(@RequestParam (value = "error", required = false) String error,
                        @RequestParam(value ="logout", required = false) String logout,
                        @RequestParam(value = "sessionError", required = false) String sessionError,
                        Model model){

        if(error != null)
            model.addAttribute("error", "Invalid username or password!");

        if(logout != null){
            model.addAttribute("logout", "You have been logged out successfully!");
        }

        if(sessionError != null)
            model.addAttribute("sessionError", "Invalid session!");

        return "login";
    }
}
