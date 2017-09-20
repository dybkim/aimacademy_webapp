package com.aimacademyla.model.builder.impl;

import com.aimacademyla.model.Member;
import com.aimacademyla.model.MemberMonthlyRegistration;
import com.aimacademyla.model.builder.GenericBuilder;
import com.aimacademyla.model.wrapper.MemberListWrapper;
import com.aimacademyla.service.MemberMonthlyRegistrationService;
import com.aimacademyla.service.MemberService;
import org.springframework.beans.factory.annotation.Configurable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class MemberListWrapperBuilder implements GenericBuilder<MemberListWrapper> {

    private MemberService memberService;
    private MemberMonthlyRegistrationService memberMonthlyRegistrationService;

    private MemberListWrapper memberListWrapper;
    private LocalDate cycleStartDate;

    private MemberListWrapperBuilder(){
    }


    public MemberListWrapperBuilder(MemberService memberService, MemberMonthlyRegistrationService memberMonthlyRegistrationService) {
        this.memberService = memberService;
        this.memberMonthlyRegistrationService = memberMonthlyRegistrationService;
        memberListWrapper = new MemberListWrapper();
    }

    public MemberListWrapperBuilder setCycleStartDate(LocalDate cycleStartDate){
        this.cycleStartDate = cycleStartDate;
        return this;
    }

    @Override
    public MemberListWrapper build() {
        List<Member> memberList = new ArrayList<>();
        List<Member> inactiveMemberList = memberService.getMemberList();
        HashMap<Integer, Boolean> membershipHashMap = new HashMap<>();

        List<MemberMonthlyRegistration> memberMonthlyRegistrationList = memberMonthlyRegistrationService.getMemberMonthlyRegistrationList(cycleStartDate);

        for(Member member : inactiveMemberList)
            membershipHashMap.put(member.getMemberID(), false);

        for(MemberMonthlyRegistration memberMonthlyRegistration : memberMonthlyRegistrationList){
            Member member = memberService.get(memberMonthlyRegistration.getMemberID());
            memberList.add(member);
            membershipHashMap.put(member.getMemberID(), true);

            Iterator it = inactiveMemberList.iterator();
            while(it.hasNext()){
                Member iteratedMember = (Member) it.next();
                if(member.getMemberID() == iteratedMember.getMemberID()){
                    it.remove();
                    break;
                }
            }
        }

        memberListWrapper.setMemberList(memberList);
        memberListWrapper.setInactiveMemberList(inactiveMemberList);
        memberListWrapper.setMembershipHashMap(membershipHashMap);
        return memberListWrapper;
    }
}
