package com.aimacademyla.dao.flow.impl;

import com.aimacademyla.dao.MonthlyFinancesSummaryDAO;
import com.aimacademyla.dao.factory.DAOFactory;
import com.aimacademyla.model.Member;
import com.aimacademyla.model.MonthlyFinancesSummary;
import com.aimacademyla.model.enums.AIMEntityType;
import com.aimacademyla.model.initializer.impl.MonthlyFinancesSummaryDefaultValueInitializer;

import java.time.LocalDate;
import java.util.List;


public class MonthlyFinancesSummaryDAOAccessFlow extends AbstractDAOAccessFlowImpl<MonthlyFinancesSummary>{

    private MonthlyFinancesSummaryDAO monthlyFinancesSummaryDAO;

    public MonthlyFinancesSummaryDAOAccessFlow(DAOFactory daoFactory) {
        super(daoFactory);
        this.monthlyFinancesSummaryDAO = (MonthlyFinancesSummaryDAO) daoFactory.getDAO(MonthlyFinancesSummary.class);
        dispatch.put(LocalDate.class, super::handleLocalDate);
    }

    @Override
    public MonthlyFinancesSummary get() {
        LocalDate cycleStartDate = (LocalDate) getQueryParameter(LocalDate.class);

        MonthlyFinancesSummary monthlyFinancesSummary = monthlyFinancesSummaryDAO.get(criteria);

        if(monthlyFinancesSummary == null)
            monthlyFinancesSummary = new MonthlyFinancesSummaryDefaultValueInitializer(getDaoFactory())
                    .setLocalDate(cycleStartDate)
                    .initialize();
            monthlyFinancesSummaryDAO.add(monthlyFinancesSummary);
        return monthlyFinancesSummary;
    }

    @Override
    public List<MonthlyFinancesSummary> getList(){
        return monthlyFinancesSummaryDAO.getList(criteria);
    }
}
