package com.aimacademyla.service;

import com.aimacademyla.model.Member;
import com.aimacademyla.model.MemberMonthlyRegistration;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * Created by davidkim on 6/19/17.
 */
public interface MemberMonthlyRegistrationService extends GenericService<MemberMonthlyRegistration, Integer>{
    MemberMonthlyRegistration getMemberMonthlyRegistrationForMemberByDate(Member member, LocalDate date);
    List<MemberMonthlyRegistration> getMemberMonthlyRegistrationList(LocalDate date);
    void addMemberMonthlyRegistrationList(List<MemberMonthlyRegistration> memberMonthlyRegistrationList);
    void updateMemberMonthlyRegistrationList(List<MemberMonthlyRegistration> memberMonthlyRegistrationList);
    void removeMemberMonthlyRegistrationList(List<MemberMonthlyRegistration> memberMonthlyRegistrationList);

    List<Member> getActiveMembers();
    List<Member> getActiveMembersForMonth(LocalDate date);


}
