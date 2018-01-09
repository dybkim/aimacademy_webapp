package com.aimacademyla.service.impl;

import com.aimacademyla.dao.GenericDAO;
import com.aimacademyla.dao.MemberMonthlyRegistrationDAO;
import com.aimacademyla.dao.MonthlyFinancesSummaryDAO;
import com.aimacademyla.dao.flow.impl.MonthlyFinancesSummaryDAOAccessFlow;
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

    @Autowired
    public MonthlyFinancesSummaryServiceImpl(@Qualifier("monthlyFinancesSummaryDAO")GenericDAO<MonthlyFinancesSummary, Integer> genericDAO){
        super(genericDAO);
        this.monthlyFinancesSummaryDAO = (MonthlyFinancesSummaryDAO) genericDAO;
    }

    @Override
    public MonthlyFinancesSummary getMonthlyFinancesSummary(LocalDate date){
        return (MonthlyFinancesSummary) new MonthlyFinancesSummaryDAOAccessFlow(getDAOFactory())
                                                .addQueryParameter(date)
                                                .get();
    }
}
