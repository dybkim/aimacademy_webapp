package com.aimacademyla.service.impl;

import com.aimacademyla.dao.GenericDAO;
import com.aimacademyla.dao.MemberMonthlyRegistrationDAO;
import com.aimacademyla.dao.MonthlyFinancesSummaryDAO;
import com.aimacademyla.model.MonthlyFinancesSummary;
import com.aimacademyla.model.Season;
import com.aimacademyla.model.reference.TemporalReference;
import com.aimacademyla.service.CourseService;
import com.aimacademyla.service.MemberMonthlyRegistrationService;
import com.aimacademyla.service.MonthlyFinancesSummaryService;
import com.aimacademyla.service.SeasonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by davidkim on 4/13/17.
 */

@Service
public class MonthlyFinancesSummaryServiceImpl extends GenericServiceImpl<MonthlyFinancesSummary, Integer> implements MonthlyFinancesSummaryService {

    private MonthlyFinancesSummaryDAO monthlyFinancesSummaryDAO;
    private SeasonService seasonService;
    private CourseService courseService;
    private MemberMonthlyRegistrationDAO memberMonthlyRegistrationDAO;

    @Autowired
    public MonthlyFinancesSummaryServiceImpl(@Qualifier("monthlyFinancesSummaryDAO")GenericDAO<MonthlyFinancesSummary, Integer> genericDAO,
                                             SeasonService seasonService,
                                             CourseService courseService,
                                             MemberMonthlyRegistrationDAO memberMonthlyRegistrationDAO){
        super(genericDAO);
        this.monthlyFinancesSummaryDAO = (MonthlyFinancesSummaryDAO) genericDAO;
        this.seasonService = seasonService;
        this.courseService = courseService;
        this.memberMonthlyRegistrationDAO = memberMonthlyRegistrationDAO;
    }

    @Override
    public List<MonthlyFinancesSummary> getAllMonthlyFinancesSummaries() {
        return monthlyFinancesSummaryDAO.getAllMonthlyFinancesSummaries();
    }

    @Override
    public MonthlyFinancesSummary getMonthlyFinancesSummary(LocalDate date) {
        MonthlyFinancesSummary monthlyFinancesSummary =  monthlyFinancesSummaryDAO.getMonthlyFinancesSummary(date);

        if(monthlyFinancesSummary == null)
            monthlyFinancesSummary = createMonthlyFinancesSummaryInstance(date);

        return monthlyFinancesSummary;
    }

    @Override
    public List<MonthlyFinancesSummary> getMonthlyFinancesSummariesInDateRange(LocalDate startDate, LocalDate endDate) {
        return monthlyFinancesSummaryDAO.getMonthlyFinancesSummariesInDateRange(startDate, endDate);
    }

    @Override
    public MonthlyFinancesSummary getMonthlyFinancesSummaryCurrent() {
        return monthlyFinancesSummaryDAO.getMonthlyFinancesSummaryCurrent();
    }

    private MonthlyFinancesSummary createMonthlyFinancesSummaryInstance(LocalDate date) {
        Season season = seasonService.getSeason(date);
        int numCourses = courseService.getCourseListByDate(date).size();
        int numMembers = memberMonthlyRegistrationDAO.getMemberMonthlyRegistrationList(date).size();

        MonthlyFinancesSummary monthlyFinancesSummary = new MonthlyFinancesSummary();
        monthlyFinancesSummary.setCycleStartDate(LocalDate.of(date.getYear(), date.getMonth(), date.getDayOfMonth()));

        if(season != null)
            monthlyFinancesSummary.setSeasonID(season.getSeasonID());

        monthlyFinancesSummary.setNumCourses(numCourses);
        monthlyFinancesSummary.setNumMembers(numMembers);

        monthlyFinancesSummaryDAO.add(monthlyFinancesSummary);

        return monthlyFinancesSummary;
    }

    private List<MonthlyFinancesSummary> generateMonthlyFinancesSummary(){
        int monthsElapsed = TemporalReference.numMonthsFromInception();

        int year;
        int month;
        LocalDate date;

        MonthlyFinancesSummary monthlyFinancesSummary;
        List<MonthlyFinancesSummary> monthlyFinancesSummaryList = new ArrayList<>();

        for(int counter = 1; counter <= monthsElapsed; counter++){
            year = TemporalReference.START_DATE.getDate().getYear() + (counter / 12);
            month = TemporalReference.START_DATE.getDate().getMonthValue() + (counter % 12);
            date = LocalDate.of(year, month, 1);
            monthlyFinancesSummary = monthlyFinancesSummaryDAO.getMonthlyFinancesSummary(date);

            if(monthlyFinancesSummaryDAO == null){
                monthlyFinancesSummary = createMonthlyFinancesSummaryInstance(date);
                monthlyFinancesSummaryDAO.add(monthlyFinancesSummary);
            }

            monthlyFinancesSummaryList.add(monthlyFinancesSummary);
        }

        return monthlyFinancesSummaryList;
    }
}
