package com.aimacademyla.model.initializer.impl;

import com.aimacademyla.dao.CourseDAO;
import com.aimacademyla.dao.MemberMonthlyRegistrationDAO;
import com.aimacademyla.dao.SeasonDAO;
import com.aimacademyla.dao.factory.DAOFactory;
import com.aimacademyla.dao.flow.impl.CourseDAOAccessFlow;
import com.aimacademyla.dao.flow.impl.MemberMonthlyRegistrationDAOAccessFlow;
import com.aimacademyla.dao.flow.impl.SeasonDAOAccessFlow;
import com.aimacademyla.model.Course;
import com.aimacademyla.model.MemberMonthlyRegistration;
import com.aimacademyla.model.MonthlyFinancesSummary;
import com.aimacademyla.model.Season;
import com.aimacademyla.model.initializer.DefaultValueInitializer;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class MonthlyFinancesSummaryDefaultValueInitializer extends GenericDefaultValueInitializerImpl<MonthlyFinancesSummary>{

    private SeasonDAO seasonDAO;

    private LocalDate localDate;

    public MonthlyFinancesSummaryDefaultValueInitializer(DAOFactory daoFactory){
        super(daoFactory);
        this.seasonDAO  = (SeasonDAO) getDAOFactory().getDAO(Season.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public MonthlyFinancesSummary initialize() {
        MonthlyFinancesSummary monthlyFinancesSummary = new MonthlyFinancesSummary();

        Season season = (Season) new SeasonDAOAccessFlow(getDAOFactory())
                                    .addQueryParameter(localDate)
                                    .get();

        List<Course> courseList =  new CourseDAOAccessFlow(getDAOFactory())
                                        .addQueryParameter(localDate)
                                        .getList();

        List<MemberMonthlyRegistration> memberMonthlyRegistrationList = new MemberMonthlyRegistrationDAOAccessFlow(getDAOFactory())
                                                                            .addQueryParameter(localDate)
                                                                            .getList();

        if(season == null)
            season = seasonDAO.get(Season.NO_SEASON_FOUND);

        int numCourses = courseList.size();
        int numMembers = memberMonthlyRegistrationList.size();

        monthlyFinancesSummary.setCycleStartDate(LocalDate.of(localDate.getYear(), localDate.getMonth(), localDate.getDayOfMonth()));

        if(season != null)
            monthlyFinancesSummary.setSeason(season);

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
