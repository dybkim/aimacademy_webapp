package com.aimacademyla.model.builder.entity;

import com.aimacademyla.model.Member;
import com.aimacademyla.model.Payment;
import com.aimacademyla.model.builder.GenericBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PaymentBuilder implements GenericBuilder<Payment> {
    private Member member;
    private LocalDate cycleStartDate;
    private BigDecimal paymentAmount;

    public Payment build(){
        Payment payment = new Payment();
        payment.setMember(member);
        payment.setCycleStartDate(cycleStartDate);
        payment.setPaymentAmount(paymentAmount);
        return payment;
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
}
