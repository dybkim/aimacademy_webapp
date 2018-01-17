package com.aimacademyla.service;

import com.aimacademyla.model.Member;
import com.aimacademyla.model.MemberMonthlyRegistration;
import com.aimacademyla.model.dto.MemberListDTO;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * Created by davidkim on 6/19/17.
 */
public interface MemberMonthlyRegistrationService extends GenericService<MemberMonthlyRegistration, Integer>{

    void addMemberMonthlyRegistration(MemberMonthlyRegistration memberMonthlyRegistration);
    void updateMemberMonthlyRegistration(MemberMonthlyRegistration memberMonthlyRegistration);
    void removeMemberMonthlyRegistration(MemberMonthlyRegistration memberMonthlyRegistration);
    void updateMemberMonthlyRegistrationList(MemberListDTO memberListDTO);
    List<MemberMonthlyRegistration> getList(LocalDate cycleStartDate);
}
