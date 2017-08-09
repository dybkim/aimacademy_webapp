package com.aimacademyla.service.impl;

import com.aimacademyla.dao.GenericDAO;
import com.aimacademyla.dao.PaymentDAO;
import com.aimacademyla.model.Charge;
import com.aimacademyla.model.Course;
import com.aimacademyla.model.Member;
import com.aimacademyla.model.Payment;
import com.aimacademyla.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

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
    public Payment getPaymentForCharge(Charge charge) {
        Payment payment = paymentDAO.getPaymentForCharge(charge);

        if(payment == null)
            payment = generateNoPayment(charge);

        return payment;
    }

    private Payment generateNoPayment(Charge charge){
        Payment payment = new Payment();
        payment.setPaymentID(Payment.NO_PAYMENT);
        payment.setChargeID(charge.getChargeID());
        payment.setPaymentAmount(0);
        return payment;
    }
}
