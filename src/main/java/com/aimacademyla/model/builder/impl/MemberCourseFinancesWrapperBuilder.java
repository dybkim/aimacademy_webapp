package com.aimacademyla.model.builder.impl;

import com.aimacademyla.formatter.LocalDateFormatter;
import com.aimacademyla.model.Charge;
import com.aimacademyla.model.Member;
import com.aimacademyla.model.Payment;
import com.aimacademyla.model.builder.GenericBuilder;
import com.aimacademyla.model.wrapper.MemberCourseFinancesWrapper;
import com.aimacademyla.service.ChargeService;
import com.aimacademyla.service.PaymentService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class MemberCourseFinancesWrapperBuilder implements GenericBuilder<MemberCourseFinancesWrapper>{

    private PaymentService paymentService;
    private ChargeService chargeService;
    private MemberCourseFinancesWrapper memberCourseFinancesWrapper;

    private LocalDate cycleStartDate;
    private Member member;


    private MemberCourseFinancesWrapperBuilder(){}

    public MemberCourseFinancesWrapperBuilder(PaymentService paymentService,
                                              ChargeService chargeService){
        memberCourseFinancesWrapper = new MemberCourseFinancesWrapper();
        this.paymentService = paymentService;
        this.chargeService = chargeService;
    }

    public MemberCourseFinancesWrapperBuilder setCycleStartDate(LocalDate cycleStartDate) {
        this.cycleStartDate = cycleStartDate;
        return this;
    }

    public MemberCourseFinancesWrapperBuilder setMember(Member member) {
        this.member = member;
        return this;
    }

    public MemberCourseFinancesWrapper build(){
        HashMap<Integer, Payment> chargePaymentHashMap = new HashMap<>();

        BigDecimal totalChargeAmount = BigDecimal.valueOf(0);
        BigDecimal totalPaymentAmount = BigDecimal.valueOf(0);

        List<Charge> chargeList = chargeService.getChargesByMemberByDate(member, cycleStartDate);
        Iterator it = chargeList.iterator();

        /**
         * Remove charges from charge list that have an amount of 0 dollars
         */
        while(it.hasNext()){
            Charge charge = (Charge) it.next();

            if(charge.getChargeAmount().compareTo(BigDecimal.ZERO) <= 0){
                it.remove();
                continue;
            }

            totalChargeAmount = totalChargeAmount.add((charge.getChargeAmount().subtract(charge.getDiscountAmount())));
            Payment payment = paymentService.getPaymentForCharge(charge);

            if(payment.getPaymentID() != Payment.NO_PAYMENT){
                totalPaymentAmount = totalPaymentAmount.add(payment.getPaymentAmount());
                chargePaymentHashMap.put(charge.getChargeID(), payment);
            }
        }

        memberCourseFinancesWrapper.setChargeList(chargeList);
        memberCourseFinancesWrapper.setDate(cycleStartDate);
        memberCourseFinancesWrapper.setTotalChargeAmount(totalChargeAmount);
        memberCourseFinancesWrapper.setTotalPaymentAmount(totalPaymentAmount);
        memberCourseFinancesWrapper.setChargePaymentHashMap(chargePaymentHashMap);

        return memberCourseFinancesWrapper;
    }
}
