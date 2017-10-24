package com.aimacademyla.dao.impl;

import com.aimacademyla.dao.MonthlyFinancesSummaryDAO;
import com.aimacademyla.model.MonthlyFinancesSummary;
import com.aimacademyla.model.enums.AIMEntityType;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by davidkim on 4/10/17.
 */

@Repository("monthlyFinancesSummaryDAO")
@Transactional
public class MonthlyFinancesSummaryDAOImpl extends GenericDAOImpl<MonthlyFinancesSummary, Integer> implements MonthlyFinancesSummaryDAO {

    private final AIMEntityType AIM_ENTITY_TYPE = AIMEntityType.MONTHLY_FINANCES_SUMMARY;

    public MonthlyFinancesSummaryDAOImpl(){
        super(MonthlyFinancesSummary.class);
    }

    @Override
    public MonthlyFinancesSummary getMonthlyFinancesSummary(LocalDate date) {
        Session session = currentSession();
        Query query = session.createQuery("FROM Monthly_Finances_Summary WHERE MONTH(CycleStartDate) = MONTH(:date) AND YEAR(CycleStartDate) = YEAR(:date)")
                .setParameter("date", date);
        MonthlyFinancesSummary monthlyFinancesSummary = (MonthlyFinancesSummary) query.uniqueResult();
        session.flush();

        return monthlyFinancesSummary;
    }

    @Override
    public List<MonthlyFinancesSummary> getMonthlyFinancesSummariesInDateRange(LocalDate startDate, LocalDate endDate) {
        Session session = currentSession();
        Query query = session.createQuery("FROM Monthly_Finances_Summary BETWEEN :startDate AND :date")
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate);
        List<MonthlyFinancesSummary> monthlyFinancesSummaryList = query.getResultList();
        session.flush();

        return monthlyFinancesSummaryList;
    }

    @Override
    public MonthlyFinancesSummary getMonthlyFinancesSummaryCurrent() {
        Session session = currentSession();
        Query query = session.createQuery("FROM Monthly_Finances_Summary WHERE YEAR(curdate()) = YEAR(DateSummaryCreated)" +
                "AND MONTH(curdate()) = MONTH(DateSummaryCreated");
        MonthlyFinancesSummary monthlyFinancesSummary = (MonthlyFinancesSummary) query.uniqueResult();
        session.flush();

        return monthlyFinancesSummary;
    }

    @Override
    public AIMEntityType getAIMEntityType() {
        return AIM_ENTITY_TYPE;
    }
}
