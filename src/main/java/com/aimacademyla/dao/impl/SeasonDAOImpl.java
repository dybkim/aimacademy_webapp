package com.aimacademyla.dao.impl;

import com.aimacademyla.dao.SeasonDAO;
import com.aimacademyla.model.Season;
import com.aimacademyla.model.enums.AIMEntityType;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.time.LocalDate;
import java.util.Date;

/**
 * Created by davidkim on 5/22/17.
 */

@Repository("seasonDAO")
@Transactional
public class SeasonDAOImpl extends GenericDAOImpl<Season, Integer> implements SeasonDAO{

    private final AIMEntityType AIM_ENTITY_TYPE = AIMEntityType.SEASON;

    public SeasonDAOImpl(){
        super(Season.class);
    }

    @Override
    public Season getSeason(LocalDate date) {
        Session session = currentSession();
        Query query = session.createQuery("FROM Season WHERE StartDate < :date AND EndDate > :date").setParameter("date", date);
        Season season = (Season) query.uniqueResult();

        return season;
    }

    @Override
    public AIMEntityType getAIMEntityType() {
        return AIM_ENTITY_TYPE;
    }
}

