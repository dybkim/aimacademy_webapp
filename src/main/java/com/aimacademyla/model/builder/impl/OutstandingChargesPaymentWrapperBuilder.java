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

        List<Member> allMemberList = memberService.getMemberList();
        List<Member> memberList;
        List<Member> inactiveMemberList;

        HashMap<Integer, Member> allMemberHashMap = new HashMap<>();
        HashMap<Integer, Member> memberHashMap = new HashMap<>();
        HashMap<Integer, Member> inactiveMemberHashMap;
        List<Charge> chargeList = chargeService.getChargesByDate(cycleStartDate);

        Iterator it = allMemberList.iterator();

        for(Member member : allMemberList)
            allMemberHashMap.put(member.getMemberID(), member);

        for(Charge charge : chargeList){
            Payment payment = paymentService.getPaymentForCharge(charge);
            BigDecimal totalAmountPaid = charge.getDiscountAmount().add(payment.getPaymentAmount());

            if(charge.getChargeAmount().subtract(totalAmountPaid).compareTo(BigDecimal.ZERO) > 0)
                memberHashMap.put(charge.getMemberID(), allMemberHashMap.get(charge.getMemberID()));
        }


        for(int memberID : memberHashMap.keySet())
            allMemberHashMap.remove(memberID);

        inactiveMemberHashMap = allMemberHashMap;

        memberList = new ArrayList<Member>(memberHashMap.values());
        inactiveMemberList = new ArrayList<Member>(inactiveMemberHashMap.values());

        HashMap<Integer, BigDecimal> allOutstandingChargesHashMap = generateOutstandingChargesHashMap(allMemberList, cycleStartDate);
        HashMap<Integer, BigDecimal> outstandingChargesHashMap = generateOutstandingChargesHashMap(memberList, cycleStartDate);
        HashMap<Integer, BigDecimal> inactiveOutstandingChargesHashMap = generateOutstandingChargesHashMap(inactiveMemberList, cycleStartDate);

        outstandingChargesPaymentWrapper.setCycleStartDate(cycleStartDate);
        outstandingChargesPaymentWrapper.setInactiveMemberList(inactiveMemberList);
        outstandingChargesPaymentWrapper.setMemberList(memberList);
        outstandingChargesPaymentWrapper.setInactiveOutstandingChargesHashMap(inactiveOutstandingChargesHashMap);
        outstandingChargesPaymentWrapper.setOutstandingChargesHashMap(outstandingChargesHashMap);

        return outstandingChargesPaymentWrapper;
    }

    /**
     * Returned HashMap is mapped by: <memberID, outstandingBalance>
    */
    private HashMap<Integer, BigDecimal> generateOutstandingChargesHashMap(List<Member> memberList, LocalDate date){
        HashMap<Integer, BigDecimal> outstandingChargesMap = new HashMap<>();

        for(Member member : memberList){
            BigDecimal outstandingBalance = BigDecimal.valueOf(0);
            List<Charge> chargeList = chargeService.getChargesByMemberByDate(member, date);

            for(Charge charge : chargeList){
                Payment payment = paymentService.getPaymentForCharge(charge);
                outstandingBalance = outstandingBalance.add(charge.getChargeAmount().subtract(charge.getDiscountAmount()).subtract(payment.getPaymentAmount()));
            }

            outstandingChargesMap.put(member.getMemberID(), outstandingBalance);
        }

        return outstandingChargesMap;
    }
}
