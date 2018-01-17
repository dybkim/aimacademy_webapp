package com.aimacademyla.model.builder.entity;

import com.aimacademyla.model.Member;
import com.aimacademyla.model.MonthlyFinancesSummary;
import com.aimacademyla.model.Payment;
import com.aimacademyla.model.builder.GenericBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PaymentBuilder implements GenericBuilder<Payment> {

    private int paymentID;
    private Member member;
    private LocalDate cycleStartDate;
    private BigDecimal paymentAmount;
    private MonthlyFinancesSummary monthlyFinancesSummary;

    public Payment build(){
        Payment payment = new Payment();
        payment.setMember(member);
        payment.setCycleStartDate(cycleStartDate);
        payment.setPaymentAmount(paymentAmount);
        payment.setMonthlyFinancesSummary(monthlyFinancesSummary);
        return payment;
    }

    public PaymentBuilder setPaymentID(int paymentID){
        this.paymentID = paymentID;
        return this;
    }

    public PaymentBuilder setMember(Member member) {
        this.member = member;
        return this;
    }

    public PaymentBuilder setCycleStartDate(LocalDate cycleStartDate) {
        this.cycleStartDate = cycleStartDate;
        return this;
    }

    public PaymentBuilder setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
        return this;
    }

    public PaymentBuilder setMonthlyFinancesSummary(MonthlyFinancesSummary monthlyFinancesSummary){
        this.monthlyFinancesSummary = monthlyFinancesSummary;
        return this;
    }
}
