package com.aimacademyla.dao.flow.impl;

import com.aimacademyla.dao.SeasonDAO;
import com.aimacademyla.dao.factory.DAOFactory;
import com.aimacademyla.model.Season;
import com.aimacademyla.model.builder.entity.SeasonBuilder;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.DateType;
import org.hibernate.type.LocalDateType;

import java.time.LocalDate;
import java.util.List;

public class SeasonDAOAccessFlow extends AbstractDAOAccessFlowImpl<Season> {

    private SeasonDAO seasonDAO;

    public SeasonDAOAccessFlow(DAOFactory daoFactory) {
        super(daoFactory);
        seasonDAO = (SeasonDAO) getDaoFactory().getDAO(Season.class);
        dispatch.put(LocalDate.class, this::handleLocalDate);
    }

    @Override
    public Season get() {
        Season season = seasonDAO.get(criteria);
        if(season == null){
            LocalDate date = (LocalDate) parameterHashMap.get(LocalDate.class);
            season = new SeasonBuilder().setLocalDate(date).build();
            seasonDAO.add(season);
        }
        return season;
    }

    @Override
    public List<Season> getList() {
        return seasonDAO.getList(criteria);
    }

    /*
     * Replaces handleLocalDate method in parent AbstractDAOAccessFlow class since Season instances have a unique way of being queried by date:
     * Season is queried by using a date range (between Season startDate and endDate)
     */
    void handleLocalDate(Object object, List<Criterion> criteria){
        LocalDate localDate = (LocalDate) object;
        Criterion startDateCriterion = Restrictions.sqlRestriction("{alias}.StartDate <= ?", localDate, LocalDateType.INSTANCE);
        Criterion endDateCriterion = Restrictions.sqlRestriction("{alias}.EndDate >= ?", localDate, LocalDateType.INSTANCE);
        criteria.add(Restrictions.and(startDateCriterion, endDateCriterion));
    }
}
