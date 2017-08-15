package com.aimacademyla.controller;

import com.aimacademyla.formatter.LocalDateFormatter;
import com.aimacademyla.model.*;
import com.aimacademyla.service.ChargeService;
import com.aimacademyla.service.MemberService;
import com.aimacademyla.service.MonthlyChargesSummaryService;
import com.aimacademyla.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by davidkim on 1/17/17.
 */

@Controller
public class HomeController {

    private MonthlyChargesSummaryService monthlyChargesSummaryService;
    private MemberService memberService;
    private ChargeService chargeService;
    private PaymentService paymentService;

    @Autowired
    public HomeController(MonthlyChargesSummaryService monthlyChargesSummaryService,
                          MemberService memberService,
                          ChargeService chargeService,
                          PaymentService paymentService){
        this.monthlyChargesSummaryService = monthlyChargesSummaryService;
        this.memberService = memberService;
        this.chargeService = chargeService;
        this.paymentService = paymentService;
    }

    @RequestMapping("/home")
    public String home(Model model){
        List<Member> allMemberList = memberService.getMemberList();
        List<Member> memberList = new ArrayList<>();
        List<Member> inactiveMemberList = new ArrayList<>();
        LocalDate cycleStartDate = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonthValue(), 1);
        HashMap<Integer, Double> allOutstandingChargesHashMap = generateOutstandingChargesHashMap(allMemberList, cycleStartDate);
        HashMap<Integer, Double> outstandingChargesHashMap = new HashMap<>();
        HashMap<Integer, Double> inactiveOutstandingChargesHashMap = new HashMap<>();

        Iterator it = allMemberList.iterator();

        while(it.hasNext()){
            Member member = (Member) it.next();
            if(member.getMemberIsActive()) {
                memberList.add(member);
                outstandingChargesHashMap.put(member.getMemberID(), allOutstandingChargesHashMap.get(member.getMemberID()));
                it.remove();
                continue;
            }

            inactiveMemberList.add(member);
            inactiveOutstandingChargesHashMap.put(member.getMemberID(), allOutstandingChargesHashMap.get(member.getMemberID()));
        }

        model.addAttribute("memberList", memberList);
        model.addAttribute("inactiveMemberList", inactiveMemberList);
        model.addAttribute("outstandingChargesHashMap", outstandingChargesHashMap);
        model.addAttribute("inactiveOutstandingChargesHashMap", inactiveOutstandingChargesHashMap);
        model.addAttribute("cycleStartDate", cycleStartDate);

        return "home";
    }

    @RequestMapping("/admin")
    public String adminPage(){
        return "home";
    }

    private HashMap<Integer, Double> generateOutstandingChargesHashMap(List<Member> memberList, LocalDate date){
        HashMap<Integer, Double> outstandingChargesMap = new HashMap<>();

        for(Member member : memberList){
            double outstandingBalance = 0;
            List<Charge> chargeList = chargeService.getChargesByMemberByDate(member, date);

            for(Charge charge : chargeList){
                Payment payment = paymentService.getPaymentForCharge(charge);
                outstandingBalance += charge.getChargeAmount() - charge.getDiscountAmount() - payment.getPaymentAmount();
            }

            outstandingChargesMap.put(member.getMemberID(), outstandingBalance);
        }

        return outstandingChargesMap;
    }
}
