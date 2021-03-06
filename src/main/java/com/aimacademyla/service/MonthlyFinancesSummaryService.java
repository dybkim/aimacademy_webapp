package com.aimacademyla.service;

import com.aimacademyla.model.MonthlyFinancesSummary;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by davidkim on 4/13/17.
 */
public interface MonthlyFinancesSummaryService extends GenericService<MonthlyFinancesSummary, Integer>{
    MonthlyFinancesSummary getMonthlyFinancesSummary(LocalDate date);
}
