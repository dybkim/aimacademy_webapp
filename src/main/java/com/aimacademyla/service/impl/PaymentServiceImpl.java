package com.aimacademyla.service.impl;

import com.aimacademyla.dao.GenericDAO;
import com.aimacademyla.dao.PaymentDAO;
import com.aimacademyla.model.MonthlyFinancesSummary;
import com.aimacademyla.model.Payment;
import com.aimacademyla.service.MonthlyFinancesSummaryService;
import com.aimacademyla.service.PaymentService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl extends GenericServiceImpl<Payment, Integer> implements PaymentService{

    private static final Logger logger = LogManager.getLogger(PaymentServiceImpl.class.getName());

    private PaymentDAO paymentDAO;
    private MonthlyFinancesSummaryService monthlyFinancesSummaryService;

    @Autowired
    public PaymentServiceImpl(@Qualifier("paymentDAO")GenericDAO<Payment, Integer> genericDAO,
                              MonthlyFinancesSummaryService monthlyFinancesSummaryService){
        super(genericDAO);
        this.monthlyFinancesSummaryService = monthlyFinancesSummaryService;
    }

    @Override
    public void addPayment(Payment payment){
        MonthlyFinancesSummary monthlyFinancesSummary = payment.getMonthlyFinancesSummary();
        logger.debug("Adding Payment to MonthlyFinancesSummary");
        monthlyFinancesSummary.addPayment(payment);
        monthlyFinancesSummaryService.update(monthlyFinancesSummary);
        logger.debug("Added Payment to MonthlyFinancesSummary, current Payment Balance: " + payment.getBalance());
    }
    @Override
    public void updatePayment(Payment payment){
        MonthlyFinancesSummary monthlyFinancesSummary = payment.getMonthlyFinancesSummary();
        logger.debug("Updating Payment in MonthlyFinancesSummary with Charges: ");
        monthlyFinancesSummary.updatePayment(payment);
        monthlyFinancesSummaryService.update(monthlyFinancesSummary);
        logger.debug("Updated Payment in MonthlyFinancesSummary, current Payment Balance: " + payment.getBalance());
    }

    @Override
    public void removePayment(Payment payment){
        MonthlyFinancesSummary monthlyFinancesSummary = payment.getMonthlyFinancesSummary();
        logger.debug("Removing Payment from MonthlyFinancesSummary");
        monthlyFinancesSummary.removePayment(payment);
        monthlyFinancesSummaryService.update(monthlyFinancesSummary);
        logger.debug("Removed Payment MonthlyFinancesSummary, currentPayment Balance: " + payment.getBalance());
   }
}
