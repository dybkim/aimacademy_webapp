package com.aimacademyla.model.initializer.impl;

import com.aimacademyla.dao.SeasonDAO;
import com.aimacademyla.dao.flow.impl.CourseDAOAccessFlow;
import com.aimacademyla.dao.flow.impl.MemberMonthlyRegistrationDAOAccessFlow;
import com.aimacademyla.dao.flow.impl.SeasonDAOAccessFlow;
import com.aimacademyla.model.Course;
import com.aimacademyla.model.MemberMonthlyRegistration;
import com.aimacademyla.model.MonthlyFinancesSummary;
import com.aimacademyla.model.Season;
import com.aimacademyla.model.builder.entity.MonthlyFinancesSummaryBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class MonthlyFinancesSummaryDefaultValueInitializer extends GenericDefaultValueInitializerImpl<MonthlyFinancesSummary>{

    private SeasonDAO seasonDAO;

    private LocalDate localDate;

    public MonthlyFinancesSummaryDefaultValueInitializer(){
        this.seasonDAO  = (SeasonDAO) getDAOFactory().getDAO(Season.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public MonthlyFinancesSummary initialize() {
        Season season = (Season) new SeasonDAOAccessFlow()
                                    .addQueryParameter(localDate)
                                    .get();

        List<Course> courseList =  new CourseDAOAccessFlow()
                                        .addQueryParameter(localDate)
                                        .getList();

        List<MemberMonthlyRegistration> memberMonthlyRegistrationList = new MemberMonthlyRegistrationDAOAccessFlow()
                                                                            .addQueryParameter(localDate)
                                                                            .getList();

        if(season == null)
            season = seasonDAO.get(Season.NO_SEASON_FOUND);

        int numCourses = courseList.size();
        int numMembers = memberMonthlyRegistrationList.size();

        return new MonthlyFinancesSummaryBuilder()
                    .setCycleStartDate(LocalDate.of(localDate.getYear(), localDate.getMonth(), localDate.getDayOfMonth()))
                    .setSeason(season)
                    .setNumCourses(numCourses)
                    .setNumMembers(numMembers)
                    .setNumTotalCharges(0)
                    .setTotalChargeAmount(BigDecimal.ZERO)
                    .setTotalPaymentAmount(BigDecimal.ZERO)
                    .build();
    }

    public MonthlyFinancesSummaryDefaultValueInitializer setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
        return this;
    }
}
