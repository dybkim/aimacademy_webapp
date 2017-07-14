package com.aimacademyla.model.factory;

import com.aimacademyla.dao.CourseDAO;
import com.aimacademyla.dao.MemberMonthlyRegistrationDAO;
import com.aimacademyla.dao.MonthlyChargesSummaryDAO;
import com.aimacademyla.dao.SeasonDAO;
import com.aimacademyla.model.MonthlyChargesSummary;
import com.aimacademyla.model.Season;
import com.aimacademyla.service.CourseService;
import com.aimacademyla.service.MemberMonthlyRegistrationService;
import com.aimacademyla.service.MonthlyChargesSummaryService;
import com.aimacademyla.service.SeasonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by davidkim on 5/31/17.
 */

public class MonthlyChargesSummaryFactory {

    private static SeasonService seasonService;
    private static CourseService courseService;
    private static MemberMonthlyRegistrationService memberMonthlyRegistrationService;
    private static MonthlyChargesSummaryDAO monthlyChargesSummaryDAO;

    private static final Date startDate = new Date(2016, 1, 1);

    @Autowired
    public MonthlyChargesSummaryFactory(SeasonService seasonService,
                                        CourseService courseService,
                                        MemberMonthlyRegistrationService memberMonthlyRegistrationService,
                                        MonthlyChargesSummaryDAO monthlyChargesSummaryDAO){
        this.seasonService = seasonService;
        this.courseService = courseService;
        this.memberMonthlyRegistrationService = memberMonthlyRegistrationService;
        this.monthlyChargesSummaryDAO = monthlyChargesSummaryDAO;
    }

    public static List<MonthlyChargesSummary> getAllMonthlyChargesSummaries(){
        List<MonthlyChargesSummary> monthlyChargesSummaryList = monthlyChargesSummaryDAO.getAllMonthlyChargesSummaries();
        int monthsElapsed = calculateMonthsElapsed();
        if(monthlyChargesSummaryList.size() == monthsElapsed)
            return monthlyChargesSummaryList;

        return generateMonthlyChargesSummary();
    }

    public static MonthlyChargesSummary createInstance(Date date) {
        Season season = seasonService.getSeason(date);
        int numCourses = courseService.getCourseListByDate(date).size();
        int numMembers = memberMonthlyRegistrationService.getMemberMonthlyRegistrationList(date).size();

        MonthlyChargesSummary monthlyChargesSummary = new MonthlyChargesSummary();
        monthlyChargesSummary.setCycleStartDate(new Date(date.getYear(), date.getMonth(), 1));

        if(season != null)
            monthlyChargesSummary.setSeasonID(season.getSeasonID());

        monthlyChargesSummary.setNumCourses(numCourses);
        monthlyChargesSummary.setNumMembers(numMembers);

        monthlyChargesSummaryDAO.add(monthlyChargesSummary);

        return monthlyChargesSummary;
    }

    private static int calculateMonthsElapsed(){
        int monthsElapsed = 0;
        Date date = new Date();

        monthsElapsed += ((date.getYear() - startDate.getYear()) * 12) + (date.getMonth() - startDate.getMonth());
        return monthsElapsed;
    }

    private static List<MonthlyChargesSummary> generateMonthlyChargesSummary(){
        int monthsElapsed = calculateMonthsElapsed();

        int year;
        int month;
        Date date;

        MonthlyChargesSummary monthlyChargesSummary;
        List<MonthlyChargesSummary> monthlyChargesSummaryList = new ArrayList<>();

        for(int counter = 1; counter <= monthsElapsed; counter++){
            year = counter / 12;
            month = counter % 12;
            date = new Date(startDate.getYear() + year, startDate.getMonth() + month, startDate.getDay());
            monthlyChargesSummary = monthlyChargesSummaryDAO.getMonthlyChargesSummary(date);

            if(monthlyChargesSummaryDAO == null){
                monthlyChargesSummary = createInstance(date);
                monthlyChargesSummaryDAO.add(monthlyChargesSummary);
            }

            monthlyChargesSummaryList.add(monthlyChargesSummary);
        }

        return monthlyChargesSummaryList;
    }
}
