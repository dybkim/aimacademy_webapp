package com.aimacademyla.model.initializer.impl;

import com.aimacademyla.dao.MonthlyFinancesSummaryDAO;
import com.aimacademyla.dao.factory.DAOFactory;
import com.aimacademyla.dao.flow.impl.MonthlyFinancesSummaryDAOAccessFlow;
import com.aimacademyla.model.Member;
import com.aimacademyla.model.MonthlyFinancesSummary;
import com.aimacademyla.model.Payment;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PaymentDefaultValueInitializer extends GenericDefaultValueInitializerImpl<Payment>{

    private Member member;
    private LocalDate cycleStartDate;

    public PaymentDefaultValueInitializer(DAOFactory daoFactory) {
        super(daoFactory);
    }

    @Override
    public Payment initialize() {
        BigDecimal paymentAmount = BigDecimal.ZERO;
        MonthlyFinancesSummary monthlyFinancesSummary = (MonthlyFinancesSummary) new MonthlyFinancesSummaryDAOAccessFlow(getDAOFactory())
                                                                                        .addQueryParameter(cycleStartDate)
                                                                                        .get();

        Payment payment = new Payment();
        payment.setMember(member);
        payment.setCycleStartDate(cycleStartDate);
        payment.setPaymentAmount(paymentAmount);
        payment.setMonthlyFinancesSummary(monthlyFinancesSummary);
        return payment;
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
