package com.aimacademyla.service.impl;

import com.aimacademyla.dao.GenericDAO;
import com.aimacademyla.dao.MemberMonthlyRegistrationDAO;
import com.aimacademyla.dao.MonthlyFinancesSummaryDAO;
import com.aimacademyla.model.enums.AIMEntityType;
import com.aimacademyla.model.MonthlyFinancesSummary;
import com.aimacademyla.model.initializer.impl.MonthlyFinancesSummaryDefaultValueInitializer;
import com.aimacademyla.model.reference.TemporalReference;
import com.aimacademyla.service.CourseService;
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

    private final AIMEntityType AIM_ENTITY_TYPE = AIMEntityType.MONTHLY_FINANCES_SUMMARY;

    @Autowired
    public MonthlyFinancesSummaryServiceImpl(@Qualifier("monthlyFinancesSummaryDAO")GenericDAO<MonthlyFinancesSummary, Integer> genericDAO){
        super(genericDAO);
        this.monthlyFinancesSummaryDAO = (MonthlyFinancesSummaryDAO) genericDAO;
    }


    @Override
    public MonthlyFinancesSummary getMonthlyFinancesSummary(LocalDate date) {
        MonthlyFinancesSummary monthlyFinancesSummary =  monthlyFinancesSummaryDAO.getMonthlyFinancesSummary(date);

        if(monthlyFinancesSummary == null){
            monthlyFinancesSummary = new MonthlyFinancesSummaryDefaultValueInitializer(getDaoFactory()).setLocalDate(date)
                                                                                        .initialize();
            monthlyFinancesSummaryDAO.add(monthlyFinancesSummary);
        }

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
                monthlyFinancesSummary = new MonthlyFinancesSummaryDefaultValueInitializer(getDaoFactory())
                        .setLocalDate(date)
                        .initialize();
                monthlyFinancesSummaryDAO.add(monthlyFinancesSummary);
            }

            monthlyFinancesSummaryList.add(monthlyFinancesSummary);
        }

        return monthlyFinancesSummaryList;
    }

    @Override
    public AIMEntityType getAIMEntityType(){
        return AIM_ENTITY_TYPE;
    }
}
