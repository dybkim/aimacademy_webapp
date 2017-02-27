package com.aimacademyla.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by davidkim on 1/24/17.
 */

@Controller
public class LoginController {

    @RequestMapping("/login")
    public String login(@RequestParam (value = "error", required = false) String error,
                        @RequestParam (value = "logout", required = false) String logout, Model model){

        if(error != null)
            model.addAttribute("error", "Invalid username or password!");

        if(logout != null)
            model.addAttribute("logout", "You have been logged out successfully!");

        return "login";
    }

}
