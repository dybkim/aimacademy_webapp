package com.aimacademyla.controller.finances;

import com.aimacademyla.model.MonthlyChargesSummary;
import com.aimacademyla.service.ChargeLineService;
import com.aimacademyla.service.CourseService;
import com.aimacademyla.service.MemberService;
import com.aimacademyla.service.MonthlyChargesSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

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

    private MonthlyChargesSummaryService monthlyChargesSummaryService;

    @Autowired
    public FinanceHomeController(CourseService courseService, MemberService memberService, ChargeLineService chargeLineService, MonthlyChargesSummaryService monthlyChargesSummaryService){
        this.courseService = courseService;
        this.memberService = memberService;
        this.chargeLineService = chargeLineService;
        this.monthlyChargesSummaryService = monthlyChargesSummaryService;
    }

    @RequestMapping()
    public String getFinancesListHome(Model model){
        List<MonthlyChargesSummary> monthlyChargesSummaryList = monthlyChargesSummaryService.getAllMonthlyChargesSummaries();
        model.addAttribute("monthlySummaryList", monthlyChargesSummaryList);
        return "/finances/financesListHome";
    }
}
