package com.aimacademyla.service.impl;

import com.aimacademyla.dao.GenericDAO;
import com.aimacademyla.dao.MonthlyChargesSummaryDAO;
import com.aimacademyla.dao.impl.GenericDAOImpl;
import com.aimacademyla.model.MonthlyChargesSummary;
import com.aimacademyla.model.factory.MonthlyChargesSummaryFactory;
import com.aimacademyla.service.MonthlyChargesSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by davidkim on 4/13/17.
 */

@Service
public class MonthlyChargesSummaryServiceImpl extends GenericServiceImpl<MonthlyChargesSummary, Integer> implements MonthlyChargesSummaryService {

    private MonthlyChargesSummaryDAO monthlyChargesSummaryDAO;

    @Autowired
    public MonthlyChargesSummaryServiceImpl(@Qualifier("monthlyChargesSummaryDAO")GenericDAO<MonthlyChargesSummary, Integer> genericDAO){
        super(genericDAO);
        this.monthlyChargesSummaryDAO = (MonthlyChargesSummaryDAO) genericDAO;
    }

    @Override
    public List<MonthlyChargesSummary> getAllMonthlyChargesSummaries() {
        //return MonthlyChargesSummaryFactory.getAllMonthlyChargesSummaries();
        return monthlyChargesSummaryDAO.getAllMonthlyChargesSummaries();
    }

    @Override
    public MonthlyChargesSummary getMonthlyChargesSummary(Date date) {
        MonthlyChargesSummary monthlyChargesSummary =  monthlyChargesSummaryDAO.getMonthlyChargesSummary(date);

        if(monthlyChargesSummary == null)
            monthlyChargesSummary = MonthlyChargesSummaryFactory.createInstance(date);

        return monthlyChargesSummary;
    }

    @Override
    public List<MonthlyChargesSummary> getMonthlyChargesSummariesInDateRange(Date startDate, Date endDate) {
        return monthlyChargesSummaryDAO.getMonthlyChargesSummariesInDateRange(startDate, endDate);
    }

    @Override
    public MonthlyChargesSummary getMonthlyChargesSummaryCurrent() {
        return monthlyChargesSummaryDAO.getMonthlyChargesSummaryCurrent();
    }


}
