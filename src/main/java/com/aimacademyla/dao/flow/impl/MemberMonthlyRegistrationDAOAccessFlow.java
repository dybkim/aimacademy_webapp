package com.aimacademyla.dao.flow.impl;

import com.aimacademyla.dao.MemberMonthlyRegistrationDAO;
import com.aimacademyla.dao.factory.DAOFactory;
import com.aimacademyla.model.Course;
import com.aimacademyla.model.Member;
import com.aimacademyla.model.MemberMonthlyRegistration;
import com.aimacademyla.model.enums.AIMEntityType;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.IntegerType;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemberMonthlyRegistrationDAOAccessFlow extends AbstractDAOAccessFlowImpl<MemberMonthlyRegistration>{

    private MemberMonthlyRegistrationDAO memberMonthlyRegistrationDAO;

    public MemberMonthlyRegistrationDAOAccessFlow(DAOFactory daoFactory) {
        super(daoFactory);
        this.memberMonthlyRegistrationDAO = (MemberMonthlyRegistrationDAO) daoFactory.getDAO(MemberMonthlyRegistration.class);
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
