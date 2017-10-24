package com.aimacademyla.model.builder.impl;

import com.aimacademyla.model.Member;
import com.aimacademyla.model.MemberMonthlyRegistration;
import com.aimacademyla.model.builder.GenericBuilder;
import com.aimacademyla.model.enums.AIMEntityType;
import com.aimacademyla.model.wrapper.MemberListWrapper;
import com.aimacademyla.service.MemberMonthlyRegistrationService;
import com.aimacademyla.service.MemberService;
import com.aimacademyla.service.factory.ServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class MemberListWrapperBuilder extends GenericBuilderImpl<MemberListWrapper> implements GenericBuilder<MemberListWrapper> {

    private MemberService memberService;
    private MemberMonthlyRegistrationService memberMonthlyRegistrationService;

    private LocalDate cycleStartDate;

    public MemberListWrapperBuilder(ServiceFactory serviceFactory) {
        super(serviceFactory);
        this.memberService = (MemberService) getServiceFactory().getService(AIMEntityType.MEMBER);
        this.memberMonthlyRegistrationService = (MemberMonthlyRegistrationService) getServiceFactory().getService(AIMEntityType.MEMBER_MONTHLY_REGISTRATION);
    }

    public MemberListWrapperBuilder setCycleStartDate(LocalDate cycleStartDate){
        this.cycleStartDate = cycleStartDate;
        return this;
    }

    @Override
    public MemberListWrapper build() {
        MemberListWrapper memberListWrapper = new MemberListWrapper();
        List<Member> memberList = new ArrayList<>();
        List<Member> inactiveMemberList = memberService.getList();
        HashMap<Integer, Boolean> membershipHashMap = new HashMap<>();

        List<MemberMonthlyRegistration> memberMonthlyRegistrationList = memberMonthlyRegistrationService.getMemberMonthlyRegistrationList(cycleStartDate);

        Iterator it = inactiveMemberList.iterator();

        /*
         * Since MemberIDs start from 1001, any entity less than 1001 has to be removed
         */
        while(it.hasNext()){
            Member member = (Member) it.next();
            if(member.getMemberID() <= 1000)
                it.remove();
        }

        for(Member member : inactiveMemberList)
            membershipHashMap.put(member.getMemberID(), false);

        for(MemberMonthlyRegistration memberMonthlyRegistration : memberMonthlyRegistrationList){
            Member member = memberService.get(memberMonthlyRegistration.getMemberID());
            memberList.add(member);
            membershipHashMap.put(member.getMemberID(), true);

            it = inactiveMemberList.iterator();
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
