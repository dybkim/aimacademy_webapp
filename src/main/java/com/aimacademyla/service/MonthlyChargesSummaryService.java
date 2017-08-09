package com.aimacademyla.service;

import com.aimacademyla.model.MonthlyChargesSummary;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * Created by davidkim on 4/13/17.
 */
public interface MonthlyChargesSummaryService extends GenericService<MonthlyChargesSummary, Integer>{
    List<MonthlyChargesSummary> getAllMonthlyChargesSummaries();
    MonthlyChargesSummary getMonthlyChargesSummary(LocalDate date);
    List<MonthlyChargesSummary> getMonthlyChargesSummariesInDateRange(LocalDate startDate, LocalDate endDate);
    MonthlyChargesSummary getMonthlyChargesSummaryCurrent();
}
