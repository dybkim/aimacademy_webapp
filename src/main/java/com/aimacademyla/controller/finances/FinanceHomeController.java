package com.aimacademyla.controller.finances;

import com.aimacademyla.model.MonthlyFinancesSummary;
import com.aimacademyla.model.Season;
import com.aimacademyla.service.ChargeLineService;
import com.aimacademyla.service.CourseService;
import com.aimacademyla.service.MemberService;
import com.aimacademyla.service.MonthlyFinancesSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;

/**
 * Created by davidkim on 4/10/17.
 */

@Controller
@RequestMapping("/admin/finances")
public class FinanceHomeController {

    private CourseService courseService;

    private MemberService memberService;

    private ChargeLineService chargeLineService;

    private MonthlyFinancesSummaryService monthlyFinancesSummaryService;

    @Autowired
    public FinanceHomeController(CourseService courseService, MemberService memberService, ChargeLineService chargeLineService, MonthlyFinancesSummaryService monthlyFinancesSummaryService){
        this.courseService = courseService;
        this.memberService = memberService;
        this.chargeLineService = chargeLineService;
        this.monthlyFinancesSummaryService = monthlyFinancesSummaryService;
    }

    @RequestMapping()
    public String home(Model model){
        List<MonthlyFinancesSummary> monthlyFinancesSummaryList = monthlyFinancesSummaryService.getAllMonthlyFinancesSummaries();
        HashMap<Integer, String> seasonDescriptionHashMap = new HashMap<>();
        for(MonthlyFinancesSummary monthlyFinancesSummary : monthlyFinancesSummaryList)
            seasonDescriptionHashMap.put(monthlyFinancesSummary.getMonthlyFinancesSummaryID(), Season.SeasonDescription.toString(monthlyFinancesSummary.getCycleStartDate()));

        model.addAttribute("monthlyFinancesSummaryList", monthlyFinancesSummaryList);
        model.addAttribute("seasonDescriptionHashMap", seasonDescriptionHashMap);
        return "/finances/monthlyFinances";
    }
}
