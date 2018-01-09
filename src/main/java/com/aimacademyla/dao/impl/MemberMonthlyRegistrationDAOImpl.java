package com.aimacademyla.dao.impl;

import com.aimacademyla.dao.MemberMonthlyRegistrationDAO;
import com.aimacademyla.model.Charge;
import com.aimacademyla.model.Member;
import com.aimacademyla.model.MemberMonthlyRegistration;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    public void removeList(List<MemberMonthlyRegistration> memberMonthlyRegistrationList){
        Session session = currentSession();
        List<Integer> memberMonthlyRegistrationIDList = new ArrayList<>();
        for(MemberMonthlyRegistration memberMonthlyRegistration : memberMonthlyRegistrationList)
            memberMonthlyRegistrationIDList.add(memberMonthlyRegistration.getMemberMonthlyRegistrationID());
        Query query = session.createQuery("DELETE FROM Member_Monthly_Registration M WHERE M.memberMonthlyRegistrationID in :memberMonthlyRegistrationIDList");
        query.setParameterList("memberMonthlyRegistrationIDList", memberMonthlyRegistrationIDList);
        query.executeUpdate();
    }
}
