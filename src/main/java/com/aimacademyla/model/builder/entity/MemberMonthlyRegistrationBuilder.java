package com.aimacademyla.model.builder.entity;

import com.aimacademyla.model.Member;
import com.aimacademyla.model.MemberMonthlyRegistration;
import com.aimacademyla.model.Season;
import com.aimacademyla.model.builder.GenericBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MemberMonthlyRegistrationBuilder implements GenericBuilder<MemberMonthlyRegistration>{

    private int memberMonthlyRegistrationID;
    private Member member;
    private LocalDate cycleStartDate;
    private BigDecimal membershipCharge;
    private Season season;

    public MemberMonthlyRegistrationBuilder setMemberMonthlyRegistrationID(int memberMonthlyRegistrationID){
        this.memberMonthlyRegistrationID = memberMonthlyRegistrationID;
        return this;
    }

    public MemberMonthlyRegistrationBuilder setMember(Member member) {
        this.member = member;
        return this;
    }

    public MemberMonthlyRegistrationBuilder setCycleStartDate(LocalDate cycleStartDate) {
        this.cycleStartDate = cycleStartDate;
        return this;
    }

    public MemberMonthlyRegistrationBuilder setMembershipCharge(BigDecimal membershipCharge) {
        this.membershipCharge = membershipCharge;
        return this;
    }

    public MemberMonthlyRegistrationBuilder setSeason(Season season) {
        this.season = season;
        return this;
    }

    @Override
    public MemberMonthlyRegistration build() {
        MemberMonthlyRegistration memberMonthlyRegistration = new MemberMonthlyRegistration();
        memberMonthlyRegistration.setMember(member);
        memberMonthlyRegistration.setCycleStartDate(cycleStartDate);
        memberMonthlyRegistration.setMembershipCharge(membershipCharge);
        memberMonthlyRegistration.setSeason(season);
        return memberMonthlyRegistration;
    }
}
