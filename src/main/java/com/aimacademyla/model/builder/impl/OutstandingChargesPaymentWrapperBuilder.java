package com.aimacademyla.model.builder.impl;

import com.aimacademyla.model.Charge;
import com.aimacademyla.model.Member;
import com.aimacademyla.model.Payment;
import com.aimacademyla.model.builder.GenericBuilder;
import com.aimacademyla.model.wrapper.OutstandingChargesPaymentWrapper;
import com.aimacademyla.service.ChargeService;
import com.aimacademyla.service.MemberService;
import com.aimacademyla.service.PaymentService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class OutstandingChargesPaymentWrapperBuilder implements GenericBuilder<OutstandingChargesPaymentWrapper>{

    private MemberService memberService;
    private PaymentService paymentService;
    private ChargeService chargeService;

    private OutstandingChargesPaymentWrapper outstandingChargesPaymentWrapper;
    private LocalDate cycleStartDate;

    private OutstandingChargesPaymentWrapperBuilder(){}

    public OutstandingChargesPaymentWrapperBuilder(MemberService memberService,
                                                   PaymentService paymentService,
                                                   ChargeService chargeService){
        outstandingChargesPaymentWrapper = new OutstandingChargesPaymentWrapper();
        this.memberService = memberService;
        this.chargeService = chargeService;
        this.paymentService = paymentService;
    }

    public OutstandingChargesPaymentWrapperBuilder setCycleStartDate(LocalDate cycleStartDate) {
        this.cycleStartDate = cycleStartDate;
        return this;
    }

    @Override
    public OutstandingChargesPaymentWrapper build() {
        List<Member> outstandingBalanceMemberList = new ArrayList<>();
        List<Member> paidBalanceMemberList = new ArrayList<>();

        HashMap<Integer, Member> allMemberHashMap = new HashMap<>();
        HashMap<Integer, BigDecimal> allMembersPaymentHashMap = new HashMap<>();
        HashMap<Integer, BigDecimal> allMembersChargesHashMap = new HashMap<>();
        HashMap<Integer, BigDecimal> balanceAmountHashMap = new HashMap<>();

        List<Charge> chargeList = chargeService.getChargesByDate(cycleStartDate);

        for(Charge charge : chargeList){
            Member member = memberService.get(charge.getMemberID());
            allMemberHashMap.put(member.getMemberID(), member);
            balanceAmountHashMap.put(member.getMemberID(), BigDecimal.ZERO);

            BigDecimal chargeAmount = charge.getChargeAmount();

            if(allMembersChargesHashMap.containsKey(member.getMemberID())){
                chargeAmount = allMembersChargesHashMap.get(member.getMemberID()).add(chargeAmount);
                allMembersChargesHashMap.put(member.getMemberID(), chargeAmount);
            }

            else
                allMembersChargesHashMap.put(member.getMemberID(), chargeAmount);

            if(!allMembersPaymentHashMap.containsKey(member.getMemberID())){
                Payment payment = paymentService.getPaymentForMemberByDate(member, cycleStartDate);
                allMembersPaymentHashMap.put(member.getMemberID(), payment.getPaymentAmount());
            }
        }

        for(Integer memberID : allMembersChargesHashMap.keySet()){
            BigDecimal outstandingBalance = allMembersChargesHashMap.get(memberID).subtract(allMembersPaymentHashMap.get(memberID));

            if(outstandingBalance.compareTo(BigDecimal.ZERO) <= 0) {
                paidBalanceMemberList.add(allMemberHashMap.get(memberID));
                balanceAmountHashMap.put(memberID, outstandingBalance);
            }

            else{
                outstandingBalanceMemberList.add(allMemberHashMap.get(memberID));
                balanceAmountHashMap.put(memberID, outstandingBalance);
            }
        }

        outstandingChargesPaymentWrapper.setCycleStartDate(cycleStartDate);
        outstandingChargesPaymentWrapper.setPaidBalanceMemberList(paidBalanceMemberList);
        outstandingChargesPaymentWrapper.setOutstandingBalanceMemberList(outstandingBalanceMemberList);
        outstandingChargesPaymentWrapper.setChargesAmountHashMap(allMembersChargesHashMap);
        outstandingChargesPaymentWrapper.setPaymentAmountHashMap(allMembersPaymentHashMap);
        outstandingChargesPaymentWrapper.setBalanceAmountHashMap(balanceAmountHashMap);

        return outstandingChargesPaymentWrapper;
    }
}
