package com.aimacademyla.controller.summaries;

import com.aimacademyla.service.MonthlyFinancesSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by davidkim on 7/3/17.
 */

@Controller
@RequestMapping("/admin/summaries")
public class SummariesController {

    private MonthlyFinancesSummaryService monthlyFinancesSummaryService;

    @Autowired
    public SummariesController(MonthlyFinancesSummaryService monthlyFinancesSummaryService){
        this.monthlyFinancesSummaryService = monthlyFinancesSummaryService;
    }

    @RequestMapping("/viewSummary/{id}")
    public String viewSummaries(@PathVariable("id") int id, Model model ){
        return "/summaries/viewMonthlyFinancesSummary";
    }
}
