package com.aimacademyla.controller;

import com.aimacademyla.model.MonthlyChargesSummary;
import com.aimacademyla.service.MonthlyChargesSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Created by davidkim on 1/17/17.
 */

@Controller
public class HomeController {

    private MonthlyChargesSummaryService monthlyChargesSummaryService;

    @Autowired
    public HomeController(MonthlyChargesSummaryService monthlyChargesSummaryService){
        this.monthlyChargesSummaryService = monthlyChargesSummaryService;
    }

    @RequestMapping("/")
    public String home(Model model){
        List<MonthlyChargesSummary> monthlyChargesSummaryList = monthlyChargesSummaryService.getAllMonthlyChargesSummaries();
        model.addAttribute("monthlyChargesSummaryList", monthlyChargesSummaryList);
        return "home";
    }

    @RequestMapping("/admin")
    public String adminPage(){
        return "home";
    }

}
