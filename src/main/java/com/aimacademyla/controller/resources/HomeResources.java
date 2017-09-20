package com.aimacademyla.controller.resources;

import com.aimacademyla.model.builder.impl.OutstandingChargesPaymentWrapperBuilder;
import com.aimacademyla.model.wrapper.OutstandingChargesPaymentWrapper;
import com.aimacademyla.service.ChargeService;
import com.aimacademyla.service.MemberService;
import com.aimacademyla.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;

@Controller
@RequestMapping("/admin/home/rest")
public class HomeResources {

    private MemberService memberService;
    private ChargeService chargeService;
    private PaymentService paymentService;

    @Autowired
    public HomeResources(MemberService memberService,
                         ChargeService chargeService,
                         PaymentService paymentService) {
        this.memberService = memberService;
        this.chargeService = chargeService;
        this.paymentService = paymentService;
    }

    @RequestMapping("/memberChargesList")
    @ResponseBody
    public OutstandingChargesPaymentWrapper getMemberChargesList(@RequestParam("month") int month,
                                                                 @RequestParam("year") int year){
        LocalDate cycleStartDate = LocalDate.of(year, month, 1);
        OutstandingChargesPaymentWrapperBuilder outstandingChargesPaymentWrapperBuilder = new OutstandingChargesPaymentWrapperBuilder(memberService, paymentService, chargeService);
        return outstandingChargesPaymentWrapperBuilder.setCycleStartDate(cycleStartDate).build();
    }
}

