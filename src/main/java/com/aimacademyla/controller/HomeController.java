package com.aimacademyla.controller;

import com.aimacademyla.model.*;
import com.aimacademyla.model.builder.impl.OutstandingChargesPaymentWrapperBuilder;
import com.aimacademyla.model.reference.TemporalReference;
import com.aimacademyla.model.wrapper.OutstandingChargesPaymentWrapper;
import com.aimacademyla.service.ChargeService;
import com.aimacademyla.service.MemberService;
import com.aimacademyla.service.MonthlyFinancesSummaryService;
import com.aimacademyla.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

/**
 * Created by davidkim on 1/17/17.
 */

@Controller
@RequestMapping("/admin")
public class HomeController {

    private MonthlyFinancesSummaryService monthlyFinancesSummaryService;
    private MemberService memberService;
    private ChargeService chargeService;
    private PaymentService paymentService;

    @Autowired
    public HomeController(MonthlyFinancesSummaryService monthlyFinancesSummaryService,
                          MemberService memberService,
                          ChargeService chargeService,
                          PaymentService paymentService){
        this.monthlyFinancesSummaryService = monthlyFinancesSummaryService;
        this.memberService = memberService;
        this.chargeService = chargeService;
        this.paymentService = paymentService;
    }

    @RequestMapping("/home")
    public String home(Model model,
                       @RequestParam(name="month", required = false) Integer month,
                       @RequestParam(name="year", required = false) Integer year){

        LocalDate cycleStartDate = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonthValue(), 1);

        if(month != null && year != null)
            cycleStartDate = LocalDate.of(year, month, 1);

        OutstandingChargesPaymentWrapperBuilder outstandingChargesPaymentWrapperBuilder = new OutstandingChargesPaymentWrapperBuilder(memberService, paymentService, chargeService);
        OutstandingChargesPaymentWrapper outstandingChargesPaymentWrapper = outstandingChargesPaymentWrapperBuilder.setCycleStartDate(cycleStartDate).build();

        List<Member> memberList = outstandingChargesPaymentWrapper.getMemberList();
        List<Member> inactiveMemberList = outstandingChargesPaymentWrapper.getInactiveMemberList();
        HashMap<Integer, BigDecimal> outstandingChargesHashMap = outstandingChargesPaymentWrapper.getOutstandingChargesHashMap();
        HashMap<Integer, BigDecimal> inactiveOutstandingChargesHashMap = outstandingChargesPaymentWrapper.getInactiveOutstandingChargesHashMap();

        List<LocalDate> monthsList = TemporalReference.getMonthList();
        Collections.reverse(monthsList);

        model.addAttribute("cycleStartDate", cycleStartDate);
        model.addAttribute("monthsList", monthsList);
        model.addAttribute("memberList", memberList);
        model.addAttribute("inactiveMemberList", inactiveMemberList);
        model.addAttribute("outstandingChargesHashMap", outstandingChargesHashMap);
        model.addAttribute("inactiveOutstandingChargesHashMap", inactiveOutstandingChargesHashMap);
        return "home";
    }

    @RequestMapping("/")
    public String adminPage(Model model){
        LocalDate selectedDate = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonthValue(), 1);
        model.addAttribute("selectedDate", selectedDate);
        return "home";
    }

}


