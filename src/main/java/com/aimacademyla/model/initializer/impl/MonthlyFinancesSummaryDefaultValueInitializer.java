package com.aimacademyla.model.initializer.impl;

import com.aimacademyla.dao.CourseDAO;
import com.aimacademyla.dao.MemberMonthlyRegistrationDAO;
import com.aimacademyla.dao.SeasonDAO;
import com.aimacademyla.dao.factory.DAOFactory;
import com.aimacademyla.model.MemberMonthlyRegistration;
import com.aimacademyla.model.MonthlyFinancesSummary;
import com.aimacademyla.model.Season;
import com.aimacademyla.model.enums.AIMEntityType;
import com.aimacademyla.model.initializer.GenericDefaultValueInitializer;
import com.aimacademyla.service.CourseService;
import com.aimacademyla.service.MemberMonthlyRegistrationService;
import com.aimacademyla.service.SeasonService;
import com.aimacademyla.service.factory.ServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MonthlyFinancesSummaryDefaultValueInitializer extends GenericDefaultValueInitializerImpl<MonthlyFinancesSummary> implements GenericDefaultValueInitializer<MonthlyFinancesSummary>{

    private MemberMonthlyRegistrationDAO memberMonthlyRegistrationDAO;
    private CourseDAO courseDAO;
    private SeasonDAO seasonDAO;

    private LocalDate localDate;

    public MonthlyFinancesSummaryDefaultValueInitializer(DAOFactory daoFactory){
        super(daoFactory);
        this.memberMonthlyRegistrationDAO = (MemberMonthlyRegistrationDAO) getDAOFactory().getDAO(AIMEntityType.MEMBER_MONTHLY_REGISTRATION);
        this.courseDAO = (CourseDAO) getDAOFactory().getDAO(AIMEntityType.COURSE);
        this.seasonDAO  = (SeasonDAO) getDAOFactory().getDAO(AIMEntityType.SEASON);
    }

    @Override
    public MonthlyFinancesSummary initialize() {
        MonthlyFinancesSummary monthlyFinancesSummary = new MonthlyFinancesSummary();

        Season season = seasonDAO.getSeason(localDate);
        if(season == null)
            season = seasonDAO.get(Season.NO_SEASON_FOUND);

        int numCourses = courseDAO.getCourseListByDate(localDate).size();
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
