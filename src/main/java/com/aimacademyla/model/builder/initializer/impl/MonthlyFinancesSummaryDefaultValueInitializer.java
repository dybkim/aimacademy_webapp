package com.aimacademyla.model.builder.initializer.impl;

import com.aimacademyla.dao.MemberMonthlyRegistrationDAO;
import com.aimacademyla.model.MemberMonthlyRegistration;
import com.aimacademyla.model.MonthlyFinancesSummary;
import com.aimacademyla.model.Season;
import com.aimacademyla.model.builder.initializer.GenericDefaultValueInitializer;
import com.aimacademyla.service.CourseService;
import com.aimacademyla.service.SeasonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MonthlyFinancesSummaryDefaultValueInitializer implements GenericDefaultValueInitializer<MonthlyFinancesSummary>{

    private MemberMonthlyRegistrationDAO memberMonthlyRegistrationDAO;
    private CourseService courseService;
    private SeasonService seasonService;

    private LocalDate localDate;
    private MonthlyFinancesSummary monthlyFinancesSummary;

    private MonthlyFinancesSummaryDefaultValueInitializer(){}

    public MonthlyFinancesSummaryDefaultValueInitializer(MemberMonthlyRegistrationDAO memberMonthlyRegistrationDAO, CourseService courseService, SeasonService seasonService) {
        this.memberMonthlyRegistrationDAO = memberMonthlyRegistrationDAO;
        this.courseService = courseService;
        this.seasonService = seasonService;
        monthlyFinancesSummary = new MonthlyFinancesSummary();
    }

    @Override
    public MonthlyFinancesSummary initialize() {
        Season season = seasonService.getSeason(localDate);
        int numCourses = courseService.getCourseListByDate(localDate).size();
        int numMembers = memberMonthlyRegistrationDAO.getMemberMonthlyRegistrationList(localDate).size();

        monthlyFinancesSummary.setCycleStartDate(LocalDate.of(localDate.getYear(), localDate.getMonth(), localDate.getDayOfMonth()));

        if(season != null)
            monthlyFinancesSummary.setSeasonID(season.getSeasonID());

        monthlyFinancesSummary.setNumCourses(numCourses);
        monthlyFinancesSummary.setNumMembers(numMembers);
        monthlyFinancesSummary.setNumTotalCharges(0);
        monthlyFinancesSummary.setNumChargesFulfilled(0);
        monthlyFinancesSummary.setTotalChargeAmount(BigDecimal.ZERO);
        monthlyFinancesSummary.setTotalPaymentAmount(BigDecimal.ZERO);

        return monthlyFinancesSummary;
    }

    public MonthlyFinancesSummaryDefaultValueInitializer setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
        return this;
    }
}
