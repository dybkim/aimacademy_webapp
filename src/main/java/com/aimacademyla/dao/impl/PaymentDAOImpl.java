package com.aimacademyla.dao.impl;

import com.aimacademyla.dao.PaymentDAO;
import com.aimacademyla.model.Charge;
import com.aimacademyla.model.Course;
import com.aimacademyla.model.Member;
import com.aimacademyla.model.Payment;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.util.List;

@Repository("paymentDAO")
@Transactional
public class PaymentDAOImpl extends GenericDAOImpl<Payment, Integer> implements PaymentDAO {

    public PaymentDAOImpl() {
        super(Payment.class);
    }

    @Override
    public List<Payment> getPaymentsByMember(Member member) {
        Session session = currentSession();
        Query query = session.createQuery("FROM Payment WHERE memberID = :memberID");
        query.setParameter("memberID", member.getMemberID());
        List<Payment> paymentList = query.getResultList();
        session.flush();

        return paymentList;
    }

    @Override
    public List<Payment> getPaymentsByMemberForCourse(Member member, Course course) {
        Session session = currentSession();
        Query query = session.createQuery("FROM Payment WHERE memberID = :memberID AND courseID = :courseID");
        query.setParameter("memberID", member.getMemberID()).setParameter("courseID", course.getCourseID());
        List<Payment> paymentList = query.getResultList();
        session.flush();

        return paymentList;
    }

    @Override
    public Payment getPaymentForCharge(Charge charge){
        Session session = currentSession();
        Query query = session.createQuery("FROM Payment WHERE chargeID = :chargeID");
        query.setParameter("chargeID", charge.getChargeID());
        Payment payment = (Payment) query.uniqueResult();
        session.flush();

        return payment;
    }
}
