package com.aimacademyla.dao.impl;

import com.aimacademyla.dao.MonthlyFinancesSummaryDAO;
import com.aimacademyla.model.MonthlyFinancesSummary;
import com.aimacademyla.model.id.IDGenerationStrategy;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by davidkim on 4/10/17.
 */

@Repository("monthlyFinancesSummaryDAO")
@Transactional
public class MonthlyFinancesSummaryDAOImpl extends GenericDAOImpl<MonthlyFinancesSummary, Integer> implements MonthlyFinancesSummaryDAO {

    public MonthlyFinancesSummaryDAOImpl() {
        super(MonthlyFinancesSummary.class);
    }

    @Override
    public void removeList(List<MonthlyFinancesSummary> monthlyFinancesSummaryList){
        Session session = currentSession();
        List<Integer> monthlyFinancesSummaryIDList = new ArrayList<>();
        for(MonthlyFinancesSummary monthlyFinancesSummary : monthlyFinancesSummaryList)
            monthlyFinancesSummaryIDList.add(monthlyFinancesSummary.getMonthlyFinancesSummaryID());
        Query query = session.createQuery("DELETE FROM Monthly_Finances_Summary M WHERE M.monthlyFinancesSummaryID in :monthlyFinancesSummaryIDList");
        query.setParameterList("monthlyFinancesSummaryIDList", monthlyFinancesSummaryIDList);
        query.executeUpdate();
    }

    @Override
    public MonthlyFinancesSummary loadCollections(MonthlyFinancesSummary monthlyFinancesSummary){
        Session session = currentSession();
        monthlyFinancesSummary = get(monthlyFinancesSummary.getMonthlyFinancesSummaryID());
        Hibernate.initialize(monthlyFinancesSummary.getChargeSet());
        Hibernate.initialize(monthlyFinancesSummary.getPaymentSet());
        session.flush();

        return monthlyFinancesSummary;
    }
}
