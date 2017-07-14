package com.aimacademyla.service.impl;

import com.aimacademyla.dao.GenericDAO;
import com.aimacademyla.dao.MemberMonthlyRegistrationDAO;
import com.aimacademyla.model.MemberMonthlyRegistration;
import com.aimacademyla.service.MemberMonthlyRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by davidkim on 6/19/17.
 */

@Service
public class MemberMonthlyRegistrationServiceImpl extends GenericServiceImpl<MemberMonthlyRegistration, Integer> implements MemberMonthlyRegistrationService {

    private MemberMonthlyRegistrationDAO memberMonthlyRegistrationDAO;

    @Autowired
    public MemberMonthlyRegistrationServiceImpl(@Qualifier("memberMonthlyRegistrationDAO") GenericDAO<MemberMonthlyRegistration, Integer> genericDAO){
        super(genericDAO);
        this.memberMonthlyRegistrationDAO = (MemberMonthlyRegistrationDAO) genericDAO;
    }

    @Override
    public List<MemberMonthlyRegistration> getMemberMonthlyRegistrationList(Date date) {
        return memberMonthlyRegistrationDAO.getMemberMonthlyRegistrationList(date);
    }

    @Override
    public void addMemberMonthlyRegistrationList(List<MemberMonthlyRegistration> memberMonthlyRegistrationList){
        memberMonthlyRegistrationDAO.addMemberMonthlyRegistrationList(memberMonthlyRegistrationList);
    }

    @Override
    public void updateMemberMonthlyRegistrationList(List<MemberMonthlyRegistration> memberMonthlyRegistrationList){
        memberMonthlyRegistrationDAO.updateMemberMonthlyRegistrationList(memberMonthlyRegistrationList);
    }

    @Override
    public void removeMemberMonthlyRegistrationList(List<MemberMonthlyRegistration> memberMonthlyRegistrationList){
        memberMonthlyRegistrationDAO.removeMemberMonthlyRegistrationList(memberMonthlyRegistrationList);
    }
}
