package com.aimacademyla.model.builder.impl;

import com.aimacademyla.model.Charge;
import com.aimacademyla.model.Member;
import com.aimacademyla.model.builder.GenericBuilder;
import com.aimacademyla.model.enums.AIMEntityType;
import com.aimacademyla.model.wrapper.MemberCourseFinancesWrapper;
import com.aimacademyla.service.ChargeService;
import com.aimacademyla.service.PaymentService;
import com.aimacademyla.service.factory.ServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;

public class MemberCourseFinancesWrapperBuilder extends GenericBuilderImpl<MemberCourseFinancesWrapper> implements GenericBuilder<MemberCourseFinancesWrapper>{

    private PaymentService paymentService;
    private ChargeService chargeService;

    private LocalDate cycleStartDate;
    private Member member;

    public MemberCourseFinancesWrapperBuilder(ServiceFactory serviceFactory){
        super(serviceFactory);
        this.paymentService = (PaymentService) getServiceFactory().getService(AIMEntityType.PAYMENT);
        this.chargeService = (ChargeService) getServiceFactory().getService(AIMEntityType.CHARGE);
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
        MemberCourseFinancesWrapper memberCourseFinancesWrapper = new MemberCourseFinancesWrapper();

        BigDecimal totalChargeAmount = BigDecimal.valueOf(0);
        BigDecimal totalPaymentAmount = paymentService.getPaymentForMemberByDate(member, cycleStartDate).getPaymentAmount();

        List<Charge> chargeList = chargeService.getChargesByMemberByDate(member, cycleStartDate);
        Iterator it = chargeList.iterator();

        //Remove charges from charge list that have an amount of 0 dollars
        while(it.hasNext()){
            Charge charge = (Charge) it.next();

            if(charge.getChargeAmount().compareTo(BigDecimal.ZERO) <= 0){
                it.remove();
                continue;
            }

            totalChargeAmount = totalChargeAmount.add((charge.getChargeAmount().subtract(charge.getDiscountAmount())));
        }

        memberCourseFinancesWrapper.setChargeList(chargeList);
        memberCourseFinancesWrapper.setDate(cycleStartDate);
        memberCourseFinancesWrapper.setTotalChargeAmount(totalChargeAmount);
        memberCourseFinancesWrapper.setTotalPaymentAmount(totalPaymentAmount);

        return memberCourseFinancesWrapper;
    }
}
