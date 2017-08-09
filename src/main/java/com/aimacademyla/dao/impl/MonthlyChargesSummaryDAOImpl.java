package com.aimacademyla.dao.impl;

import com.aimacademyla.dao.MonthlyChargesSummaryDAO;
import com.aimacademyla.model.MonthlyChargesSummary;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * Created by davidkim on 4/10/17.
 */

@Repository("monthlyChargesSummaryDAO")
@Transactional
public class MonthlyChargesSummaryDAOImpl extends GenericDAOImpl<MonthlyChargesSummary, Integer> implements MonthlyChargesSummaryDAO {

    public MonthlyChargesSummaryDAOImpl(){
        super(MonthlyChargesSummary.class);
    }

    @Override
    public List<MonthlyChargesSummary> getAllMonthlyChargesSummaries() {
        Session session = currentSession();
        Query query = session.createQuery("FROM Monthly_Charges_Summary");
        List<MonthlyChargesSummary> monthlyChargesSummaryList = query.getResultList();
        session.flush();

        return monthlyChargesSummaryList;
    }

    @Override
    public MonthlyChargesSummary getMonthlyChargesSummary(LocalDate date) {
        Session session = currentSession();
        Query query = session.createQuery("FROM MonthlySummary WHERE MONTH(CycleStartDate) = MONTH(:date) AND YEAR(CycleStartDate) = YEAR(:date)")
                .setParameter("date", date);
        MonthlyChargesSummary monthlyChargesSummary = (MonthlyChargesSummary) query.uniqueResult();
        session.flush();

        return monthlyChargesSummary;
    }

    @Override
    public List<MonthlyChargesSummary> getMonthlyChargesSummariesInDateRange(LocalDate startDate, LocalDate endDate) {
        Session session = currentSession();
        Query query = session.createQuery("FROM MonthlySummary BETWEEN :startDate AND :date")
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate);
        List<MonthlyChargesSummary> monthlyChargesSummaryList = query.getResultList();
        session.flush();

        return monthlyChargesSummaryList;
    }

    @Override
    public MonthlyChargesSummary getMonthlyChargesSummaryCurrent() {
        Session session = currentSession();
        Query query = session.createQuery("FROM MonthlySummary WHERE YEAR(curdate()) = YEAR(DateSummaryCreated)" +
                "AND MONTH(curdate()) = MONTH(DateSummaryCreated");
        MonthlyChargesSummary monthlyChargesSummary = (MonthlyChargesSummary) query.uniqueResult();
        session.flush();

        return monthlyChargesSummary;
    }
}
