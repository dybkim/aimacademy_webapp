package com.aimacademyla.service;

import com.aimacademyla.model.MonthlyFinancesSummary;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by davidkim on 4/13/17.
 */
public interface MonthlyFinancesSummaryService extends GenericService<MonthlyFinancesSummary, Integer>{
    List<MonthlyFinancesSummary> getAllMonthlyFinancesSummaries();
    MonthlyFinancesSummary getMonthlyFinancesSummary(LocalDate date);
    List<MonthlyFinancesSummary> getMonthlyFinancesSummariesInDateRange(LocalDate startDate, LocalDate endDate);
    MonthlyFinancesSummary getMonthlyFinancesSummaryCurrent();
}
