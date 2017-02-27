package com.aimacademyla.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by davidkim on 1/17/17.
 */

@Controller
public class HomeController {

    @RequestMapping("/")
    public String home(){
        return "home";
    }

    @RequestMapping("/admin")
    public String adminPage(){
        return "home";
    }


}
