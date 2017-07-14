package com.aimacademyla.dao.impl;

import com.aimacademyla.dao.MemberMonthlyRegistrationDAO;
import com.aimacademyla.model.Member;
import com.aimacademyla.model.MemberMonthlyRegistration;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by davidkim on 6/14/17.
 */

@Repository("memberMonthlyRegistrationDAO")
@Transactional
public class MemberMonthlyRegistrationDAOImpl extends GenericDAOImpl<MemberMonthlyRegistration, Integer> implements MemberMonthlyRegistrationDAO{

    public MemberMonthlyRegistrationDAOImpl() {
        super(MemberMonthlyRegistration.class);
    }


    @Override
    public List<MemberMonthlyRegistration> getMemberMonthlyRegistrationList(Date date) {
        Session session =  currentSession();
        Query query = session.createQuery("FROM Member_Monthly_Registration WHERE MONTH(CycleStartDate) = MONTH(:date)");
        query.setParameter("date", date);
        List<MemberMonthlyRegistration> memberMonthlyRegistrations = query.getResultList();
        session.flush();

        return memberMonthlyRegistrations;
    }

    @Override
    public void addMemberMonthlyRegistrationList(List<MemberMonthlyRegistration> memberMonthlyRegistrationList){
        Session session = currentSession();
        for(MemberMonthlyRegistration memberMonthlyRegistration : memberMonthlyRegistrationList)
            session.saveOrUpdate(memberMonthlyRegistration);
        session.flush();
    }

    @Override
    public void updateMemberMonthlyRegistrationList(List<MemberMonthlyRegistration> memberMonthlyRegistrationList) {
        Session session = currentSession();
        for(MemberMonthlyRegistration memberMonthlyRegistration : memberMonthlyRegistrationList)
            session.saveOrUpdate(memberMonthlyRegistration);
        session.flush();
    }

    @Override
    public void removeMemberMonthlyRegistrationList(List<MemberMonthlyRegistration> memberMonthlyRegistrationList) {
        Session session = currentSession();
        for(MemberMonthlyRegistration memberMonthlyRegistration : memberMonthlyRegistrationList)
            session.remove(memberMonthlyRegistration);
        session.flush();
    }
}
