package com.aimacademyla.dao.impl;

import com.aimacademyla.dao.MemberDAO;
import com.aimacademyla.model.Member;
import com.aimacademyla.model.id.IDGenerationStrategy;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    public void removeList(List<Member> memberList){
        Session session = currentSession();
        List<Integer> memberIDList = new ArrayList<>();
        for(Member member : memberList)
            memberIDList.add(member.getMemberID());
        Query query = session.createQuery("DELETE FROM Member M WHERE M.memberID in :memberIDList");
        query.setParameterList("memberIDList", memberIDList);
        query.executeUpdate();
    }

    @Override
    public Member getEager(Integer memberID){
        return loadCollections(get(memberID));
    }

    @Override
    public Member loadCollections(Member member){
        Session session = currentSession();
        member = get(member.getMemberID());
        Hibernate.initialize(member.getMemberMonthlyRegistrationMap());
        Hibernate.initialize(member.getMemberCourseRegistrationMap());
        session.flush();

        return member;
    }
}
