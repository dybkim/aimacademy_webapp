package com.aimacademyla.service.impl;

import com.aimacademyla.dao.GenericDAO;
import com.aimacademyla.dao.MonthlyChargesSummaryDAO;
import com.aimacademyla.model.MemberMonthlyRegistration;
import com.aimacademyla.model.MonthlyChargesSummary;
import com.aimacademyla.model.Season;
import com.aimacademyla.model.reference.TemporalReference;
import com.aimacademyla.service.CourseService;
import com.aimacademyla.service.MemberMonthlyRegistrationService;
import com.aimacademyla.service.MonthlyChargesSummaryService;
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
public class MonthlyChargesSummaryServiceImpl extends GenericServiceImpl<MonthlyChargesSummary, Integer> implements MonthlyChargesSummaryService {

    private MonthlyChargesSummaryDAO monthlyChargesSummaryDAO;
    private SeasonService seasonService;
    private CourseService courseService;
    private MemberMonthlyRegistrationService memberMonthlyRegistrationService;

    @Autowired
    public MonthlyChargesSummaryServiceImpl(@Qualifier("monthlyChargesSummaryDAO")GenericDAO<MonthlyChargesSummary, Integer> genericDAO,
                                            SeasonService seasonService,
                                            CourseService courseService,
                                            MemberMonthlyRegistrationService memberMonthlyRegistrationService){
        super(genericDAO);
        this.monthlyChargesSummaryDAO = (MonthlyChargesSummaryDAO) genericDAO;
        this.seasonService = seasonService;
        this. courseService = courseService;
        this.memberMonthlyRegistrationService = memberMonthlyRegistrationService;
    }

    @Override
    public List<MonthlyChargesSummary> getAllMonthlyChargesSummaries() {
        return monthlyChargesSummaryDAO.getAllMonthlyChargesSummaries();
    }

    @Override
    public MonthlyChargesSummary getMonthlyChargesSummary(LocalDate date) {
        MonthlyChargesSummary monthlyChargesSummary =  monthlyChargesSummaryDAO.getMonthlyChargesSummary(date);

        if(monthlyChargesSummary == null)
            monthlyChargesSummary = createMonthlyChargesSummaryInstance(date);

        return monthlyChargesSummary;
    }

    @Override
    public List<MonthlyChargesSummary> getMonthlyChargesSummariesInDateRange(LocalDate startDate, LocalDate endDate) {
        return monthlyChargesSummaryDAO.getMonthlyChargesSummariesInDateRange(startDate, endDate);
    }

    @Override
    public MonthlyChargesSummary getMonthlyChargesSummaryCurrent() {
        return monthlyChargesSummaryDAO.getMonthlyChargesSummaryCurrent();
    }

    private MonthlyChargesSummary createMonthlyChargesSummaryInstance(LocalDate date) {
        Season season = seasonService.getSeason(date);
        int numCourses = courseService.getCourseListByDate(date).size();
        int numMembers = memberMonthlyRegistrationService.getMemberMonthlyRegistrationList(date).size();

        MonthlyChargesSummary monthlyChargesSummary = new MonthlyChargesSummary();
        monthlyChargesSummary.setCycleStartDate(LocalDate.of(date.getYear(), date.getMonth(), date.getDayOfMonth()));

        if(season != null)
            monthlyChargesSummary.setSeasonID(season.getSeasonID());

        monthlyChargesSummary.setNumCourses(numCourses);
        monthlyChargesSummary.setNumMembers(numMembers);

        monthlyChargesSummaryDAO.add(monthlyChargesSummary);

        return monthlyChargesSummary;
    }

    private List<MonthlyChargesSummary> generateMonthlyChargesSummary(){
        int monthsElapsed = TemporalReference.numMonthsFromInception();

        int year;
        int month;
        LocalDate date;

        MonthlyChargesSummary monthlyChargesSummary;
        List<MonthlyChargesSummary> monthlyChargesSummaryList = new ArrayList<>();

        for(int counter = 1; counter <= monthsElapsed; counter++){
            year = TemporalReference.START_DATE.getDate().getYear() + (counter / 12);
            month = TemporalReference.START_DATE.getDate().getMonthValue() + (counter % 12);
            date = LocalDate.of(year, month, 1);
            monthlyChargesSummary = monthlyChargesSummaryDAO.getMonthlyChargesSummary(date);

            if(monthlyChargesSummaryDAO == null){
                monthlyChargesSummary = createMonthlyChargesSummaryInstance(date);
                monthlyChargesSummaryDAO.add(monthlyChargesSummary);
            }

            monthlyChargesSummaryList.add(monthlyChargesSummary);
        }

        return monthlyChargesSummaryList;
    }
}
