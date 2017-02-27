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
public class MemberDAOImpl implements MemberDAO {

    private SessionFactory sessionFactory;

    @Autowired
    public MemberDAOImpl(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void addMember(Member member) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(member);
        session.flush();
    }

    @Override
    public void editMember(Member member) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(member);
        session.flush();
    }

    @Override
    public Member getMemberByID(int id) {
        Session session = sessionFactory.getCurrentSession();
        Member member = session.get(Member.class, id);
        session.flush();
        return member;
    }

    /**
     * Current Student list is derived from the Member table.
     * TODO: In the future, change the database source table from Member to StudentMember
     */
    @Override
    public List<Member> getMemberList() {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("FROM Member");
        List<Member> memberList = query.getResultList();
        session.flush();
        return memberList;
    }

    @Override
    public void deleteMember(Member member) {
        Session session = sessionFactory.getCurrentSession();
        session.delete(member);
        session.flush();
    }

    @Override
    public void editMembers(List<Member> memberList){
        Session session = sessionFactory.getCurrentSession();
        for(Member member : memberList)
            session.saveOrUpdate(member);
        session.flush();
    }

    @Override
    public List<Member> getMembersByCourse(Course course){
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("FROM Member WHERE memberID IN (SELECT studentMemberID FROM Student_Registration WHERE courseID = 0)");
        List<Member> memberList = query.getResultList();
        session.flush();

        return memberList;
    }
}
