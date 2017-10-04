package com.aimacademyla.dao;

import com.aimacademyla.model.MonthlyFinancesSummary;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by davidkim on 4/10/17.
 */
public interface MonthlyFinancesSummaryDAO extends GenericDAO<MonthlyFinancesSummary, Integer>{ ;
    MonthlyFinancesSummary getMonthlyFinancesSummary(LocalDate date);
    List<MonthlyFinancesSummary> getMonthlyFinancesSummariesInDateRange(LocalDate startDate, LocalDate endDate);
    MonthlyFinancesSummary getMonthlyFinancesSummaryCurrent();
}
