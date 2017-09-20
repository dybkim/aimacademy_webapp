package com.aimacademyla.model.builder.initializer.impl;

import com.aimacademyla.model.MemberMonthlyRegistration;
import com.aimacademyla.model.MonthlyFinancesSummary;
import com.aimacademyla.model.Season;
import com.aimacademyla.model.builder.initializer.GenericDefaultValueInitializer;
import com.aimacademyla.service.CourseService;
import com.aimacademyla.service.MonthlyFinancesSummaryService;
import com.aimacademyla.service.SeasonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

public class MemberMonthlyRegistrationDefaultValueInitializer implements GenericDefaultValueInitializer<MemberMonthlyRegistration>{

    private SeasonService seasonService;

    private int memberID;
    private LocalDate localDate;
    private MemberMonthlyRegistration memberMonthlyRegistration;

    private MemberMonthlyRegistrationDefaultValueInitializer(){}

    public MemberMonthlyRegistrationDefaultValueInitializer(SeasonService seasonService){
        this.seasonService = seasonService;

        memberMonthlyRegistration = new MemberMonthlyRegistration();
    }

    @Override
    public MemberMonthlyRegistration initialize() {
        LocalDate cycleStartDate = LocalDate.of(localDate.getYear(), localDate.getMonthValue(), 1);
        Season season = seasonService.getSeason(cycleStartDate);
        memberMonthlyRegistration.setCycleStartDate(cycleStartDate);
        memberMonthlyRegistration.setMemberID(memberID);
        memberMonthlyRegistration.setSeasonID(season.getSeasonID());

        return memberMonthlyRegistration;
    }

    public MemberMonthlyRegistrationDefaultValueInitializer setMemberID(int memberID) {
        this.memberID = memberID;
        return this;
    }

    public MemberMonthlyRegistrationDefaultValueInitializer setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
        return this;
    }
}
