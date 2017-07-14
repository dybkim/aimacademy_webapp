package com.aimacademyla.dao.impl;

import com.aimacademyla.dao.MemberDAO;
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
 * Created by davidkim on 1/20/17.
 */

@Repository("memberDAO")
@Transactional
public class MemberDAOImpl extends GenericDAOImpl<Member,Integer> implements MemberDAO {
    public MemberDAOImpl(){
        super(Member.class);
    }

    @Override
    public List<Member> getMemberList(){
        Session session = currentSession();
        Query query = session.createQuery("FROM Member");
        List<Member> memberList = query.getResultList();
        session.flush();

        return memberList;
    }

    @Override
    public List<Member> getActiveMembers(){
        Session session = currentSession();
        Query query = session.createQuery("FROM Member WHERE MemberIsActive = 1");
        List<Member> memberList = query.getResultList();
        session.flush();

        return memberList;
    }

    @Override
    public List<Member> getMembersByCourse(Course course){
        Session session = currentSession();
        Query query = session.createQuery("FROM Member WHERE memberID IN (SELECT memberID FROM Member_Course_Registration WHERE courseID = :courseID)");
        query.setParameter("courseID", course.getCourseID());
        List<Member> memberList = query.getResultList();
        session.flush();

        return memberList;
    }

    @Override
    public void updateMemberList(List<Member> memberList){
        Session session = currentSession();

        for(Member member : memberList)
            session.saveOrUpdate(member);
    }
}
