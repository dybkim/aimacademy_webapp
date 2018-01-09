package com.aimacademyla.dao.impl;

import com.aimacademyla.dao.PaymentDAO;
import com.aimacademyla.model.Charge;
import com.aimacademyla.model.Course;
import com.aimacademyla.model.Member;
import com.aimacademyla.model.Payment;
import com.aimacademyla.model.enums.AIMEntityType;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository("paymentDAO")
@Transactional
public class PaymentDAOImpl extends GenericDAOImpl<Payment, Integer> implements PaymentDAO {

    public PaymentDAOImpl() {
        super(Payment.class);
    }

    @Override
    public void removeList(List<Payment> paymentList){
        Session session = currentSession();
        List<Integer> paymentIDList = new ArrayList<>();
        for(Payment payment : paymentList)
            paymentIDList.add(payment.getPaymentID());
        Query query = session.createQuery("DELETE FROM Payment P WHERE P.paymentID in :paymentIDList");
        query.setParameterList("paymentIDList", paymentIDList);
        query.executeUpdate();
    }

    @Override
    public Payment getEager(Integer paymentID){
        return loadCollections(get(paymentID));
    }

    @Override
    public Payment loadCollections(Payment payment){
        Session session = currentSession();
        payment = session.get(Payment.class, payment.getPaymentID());
        Hibernate.initialize(payment.getChargeSet());
        session.flush();

        return payment;
    }
}
