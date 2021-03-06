package com.aimacademyla.dao.flow.impl;

import com.aimacademyla.dao.PaymentDAO;
import com.aimacademyla.model.Member;
import com.aimacademyla.model.Payment;
import com.aimacademyla.model.initializer.impl.PaymentDefaultValueInitializer;

import java.time.LocalDate;
import java.util.List;

public class PaymentDAOAccessFlow extends AbstractDAOAccessFlowImpl<Payment>{

    private PaymentDAO paymentDAO;

    public PaymentDAOAccessFlow() {
        paymentDAO = (PaymentDAO) getDAOFactory().getDAO(Payment.class);
        dispatch.put(Member.class, super::handleMember);
        dispatch.put(LocalDate.class, super::handleLocalDate);
    }

    @Override
    public Payment get(){
        Member member = (Member) parameterHashMap.get(Member.class);
        LocalDate cycleStartDate = (LocalDate) parameterHashMap.get(LocalDate.class);

        Payment payment = paymentDAO.get(criteria);

        if(payment == null){
            payment = new PaymentDefaultValueInitializer()
                            .setMember(member)
                            .setCycleStartDate(LocalDate.of(cycleStartDate.getYear(), cycleStartDate.getMonthValue(), 1))
                            .initialize();
            paymentDAO.add(payment);
        }

        return payment;
    }

    @Override
    public List<Payment> getList(){
        return paymentDAO.getList(criteria);
    }
}
