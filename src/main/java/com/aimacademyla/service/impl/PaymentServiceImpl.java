package com.aimacademyla.service.impl;

import com.aimacademyla.dao.GenericDAO;
import com.aimacademyla.dao.PaymentDAO;
import com.aimacademyla.model.Course;
import com.aimacademyla.model.Member;
import com.aimacademyla.model.Payment;
import com.aimacademyla.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class PaymentServiceImpl extends GenericServiceImpl<Payment, Integer> implements PaymentService{

    private PaymentDAO paymentDAO;

    @Autowired
    public PaymentServiceImpl(@Qualifier("paymentDAO")GenericDAO<Payment, Integer> genericDAO){
        super(genericDAO);
        this.paymentDAO = (PaymentDAO) genericDAO;
    }

    @Override
    public List<Payment> getPaymentsByMember(Member member) {
        return paymentDAO.getPaymentsByMember(member);
    }

    @Override
    public List<Payment> getPaymentsByMemberForCourse(Member member, Course course) {
        return paymentDAO.getPaymentsByMemberForCourse(member, course);
    }

    @Override
    public List<Payment> getPaymentsForDate(LocalDate date) {
        return paymentDAO.getPaymentsForDate(date);
    }

    @Override
    public Payment getPaymentForMemberByDate(Member member, LocalDate date){
        Payment payment = paymentDAO.getPaymentForMemberByDate(member, date);

        if(payment == null){
            payment = new Payment();
            payment.setMemberID(member.getMemberID());
            payment.setCycleStartDate(LocalDate.of(date.getYear(), date.getMonthValue(), 1));
            payment.setPaymentAmount(BigDecimal.ZERO);
        }


        return payment;
    }

    @Override
    public void remove(List<Payment> paymentList){
        for(Payment payment : paymentList)
            remove(payment);
    }
}
