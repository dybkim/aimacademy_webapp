package com.aimacademyla.dao;

import com.aimacademyla.model.MonthlyChargesSummary;

import java.util.Date;
import java.util.List;

/**
 * Created by davidkim on 4/10/17.
 */
public interface MonthlyChargesSummaryDAO extends GenericDAO<MonthlyChargesSummary, Integer>{
    List<MonthlyChargesSummary> getAllMonthlyChargesSummaries();
    MonthlyChargesSummary getMonthlyChargesSummary(Date date);
    List<MonthlyChargesSummary> getMonthlyChargesSummariesInDateRange(Date startDate, Date endDate);
    MonthlyChargesSummary getMonthlyChargesSummaryCurrent();
}
