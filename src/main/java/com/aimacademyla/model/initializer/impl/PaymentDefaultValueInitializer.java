package com.aimacademyla.model.initializer.impl;

import com.aimacademyla.dao.flow.impl.MonthlyFinancesSummaryDAOAccessFlow;
import com.aimacademyla.model.Member;
import com.aimacademyla.model.MonthlyFinancesSummary;
import com.aimacademyla.model.Payment;
import com.aimacademyla.model.builder.entity.PaymentBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PaymentDefaultValueInitializer extends GenericDefaultValueInitializerImpl<Payment>{

    private Member member;
    private LocalDate cycleStartDate;

    @Override
    public Payment initialize() {
        BigDecimal paymentAmount = BigDecimal.ZERO;
        MonthlyFinancesSummary monthlyFinancesSummary = (MonthlyFinancesSummary) new MonthlyFinancesSummaryDAOAccessFlow()
                                                                                        .addQueryParameter(cycleStartDate)
                                                                                        .get();
        return new PaymentBuilder()
                        .setMember(member)
                        .setCycleStartDate(cycleStartDate)
                        .setPaymentAmount(paymentAmount)
                        .setMonthlyFinancesSummary(monthlyFinancesSummary)
                        .build();
    }

    public PaymentDefaultValueInitializer setMember(Member member) {
        this.member = member;
        return this;
    }

    public PaymentDefaultValueInitializer setCycleStartDate(LocalDate cycleStartDate) {
        this.cycleStartDate = cycleStartDate;
        return this;
    }
}
