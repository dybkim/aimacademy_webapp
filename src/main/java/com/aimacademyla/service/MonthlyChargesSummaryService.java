package com.aimacademyla.service;

import com.aimacademyla.model.MonthlyChargesSummary;

import java.util.Date;
import java.util.List;

/**
 * Created by davidkim on 4/13/17.
 */
public interface MonthlyChargesSummaryService extends GenericService<MonthlyChargesSummary, Integer>{
    List<MonthlyChargesSummary> getAllMonthlyChargesSummaries();
    MonthlyChargesSummary getMonthlyChargesSummary(Date date);
    List<MonthlyChargesSummary> getMonthlyChargesSummariesInDateRange(Date startDate, Date endDate);
    MonthlyChargesSummary getMonthlyChargesSummaryCurrent();
}
