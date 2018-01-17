package com.aimacademyla.dao.flow.impl;

import com.aimacademyla.dao.MemberMonthlyRegistrationDAO;
import com.aimacademyla.model.Member;
import com.aimacademyla.model.MemberMonthlyRegistration;

import java.time.LocalDate;
import java.util.List;

public class MemberMonthlyRegistrationDAOAccessFlow extends AbstractDAOAccessFlowImpl<MemberMonthlyRegistration>{

    private MemberMonthlyRegistrationDAO memberMonthlyRegistrationDAO;

    public MemberMonthlyRegistrationDAOAccessFlow() {
        this.memberMonthlyRegistrationDAO = (MemberMonthlyRegistrationDAO) getDAOFactory().getDAO(MemberMonthlyRegistration.class);
        dispatch.put(Member.class, super::handleMember);
        dispatch.put(LocalDate.class, super::handleLocalDate);
    }

    @Override
    public MemberMonthlyRegistration get() {
        MemberMonthlyRegistration memberMonthlyRegistration = memberMonthlyRegistrationDAO.get(criteria);

        /*
         * Create dummy MemberMonthlyRegistration if no MemberMonthlyRegistration exists for that month
         * so that a null object isn't returned
         */
        if(memberMonthlyRegistration == null){
            memberMonthlyRegistration = new MemberMonthlyRegistration();
            memberMonthlyRegistration.setMemberMonthlyRegistrationID(MemberMonthlyRegistration.INACTIVE);
        }

        return memberMonthlyRegistration;
    }

    @Override
    public List<MemberMonthlyRegistration> getList(){
        return memberMonthlyRegistrationDAO.getList(criteria);
    }

}
