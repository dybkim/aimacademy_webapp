package com.aimacademyla.dao.impl;

import com.aimacademyla.dao.ChargeDAO;
import com.aimacademyla.model.Charge;
import com.aimacademyla.model.Course;
import com.aimacademyla.model.Member;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by davidkim on 4/10/17.
 */

@Repository("chargeDAO")
@Transactional
public class ChargeDAOImpl extends GenericDAOImpl<Charge,Integer> implements ChargeDAO {

    public ChargeDAOImpl(){
        super(Charge.class);
    }

    @Override
    public List<Charge> getChargesByMember(int memberID) {
        Session session = currentSession();
        Query query = session.createQuery("FROM Charge WHERE memberID = :memberID");
        query.setParameter("memberID", memberID);
        List<Charge> chargeList = query.getResultList();
        session.flush();
        return chargeList;
    }

    @Override
    public List<Charge> getChargesByMember(Member member) {
        return getChargesByMember(member.getMemberID());
    }

    @Override
    public List<Charge> getChargesByMemberForCourse(Member member, Course course){
        Session session = currentSession();
        Query query = session.createQuery("FROM Charge WHERE memberID = :memberID AND courseID = :courseID");
        query.setParameter("memberID", member.getMemberID()).setParameter("courseID", course.getCourseID());
        List<Charge> chargeList = query.getResultList();
        session.flush();

        return chargeList;
    }

    @Override
    public List<Charge> getChargesByMemberByDate(Member member, LocalDate localDate) {
        Session session = currentSession();
        Query query = session.createQuery("FROM Charge WHERE memberID = :memberID AND YEAR(CycleStartDate) = YEAR(:localDate) AND MONTH(CycleStartDate) = MONTH(:localDate)");
        query.setParameter("memberID", member.getMemberID()).setParameter("localDate", localDate);
        List<Charge> chargeList = query.getResultList();
        session.flush();

        return chargeList;
    }

    @Override
    public Charge getChargeByMemberForCourseByDate(Member member, Course course, LocalDate date){
        Session session = currentSession();
        Query query = session.createQuery("FROM Charge WHERE memberID = :memberID AND courseID = :courseID AND MONTH(CycleStartDate) = MONTH(:date) AND YEAR(CycleStartDate) = YEAR(:date)");
        query.setParameter("memberID", member.getMemberID()).setParameter("courseID", course.getCourseID()).setParameter("date", date);

        /**
         * Generate and save new charge instance if charge is not found
         */
        Charge charge = (Charge) query.uniqueResult();
        session.flush();

        return charge;
    }

    @Override
    public List<Charge> getChargesByCourse(int courseID) {
        Session session = currentSession();
        Query query = session.createQuery("FROM Charge WHERE courseID = :courseID");
        query.setParameter("courseID", courseID);
        List<Charge> chargeList = query.getResultList();
        session.flush();

        return chargeList;
    }

    @Override
    public List<Charge> getChargesByCourse(Course course) {
        Session session = currentSession();
        Query query = session.createQuery("FROM Charge WHERE courseID = :courseID");
        query.setParameter("courseID", course.getCourseID());
        List<Charge> chargeList = query.getResultList();
        session.flush();

        return chargeList;
    }
}
