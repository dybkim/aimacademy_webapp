package com.aimacademyla.dao.impl;

import com.aimacademyla.dao.ChargeDAO;
import com.aimacademyla.model.Charge;
import com.aimacademyla.model.Course;
import com.aimacademyla.model.Member;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
        Query query = session.createQuery("FROM Charge WHERE MemberID = :memberID");
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
    public List<Charge> getChargesByCourse(int courseID) {
        Session session = currentSession();
        Query query = session.createQuery("FROM Charge WHERE CourseID = :courseID");
        query.setParameter("courseID", courseID);
        List<Charge> chargeList = query.getResultList();
        session.flush();

        return chargeList;
    }

    @Override
    public List<Charge> getChargesByCourse(Course course) {
        Session session = currentSession();
        Query query = session.createQuery("FROM Charge WHERE CourseID = :courseID");
        query.setParameter("courseID", course.getCourseID());
        List<Charge> chargeList = query.getResultList();
        session.flush();

        return chargeList;
    }
}
