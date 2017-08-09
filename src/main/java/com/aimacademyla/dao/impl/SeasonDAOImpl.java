package com.aimacademyla.dao.impl;

import com.aimacademyla.dao.SeasonDAO;
import com.aimacademyla.model.Season;
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

    public SeasonDAOImpl(){
        super(Season.class);
    }

    @Override
    public Season getSeasonByDate(LocalDate date) {
        Session session = currentSession();
        Query query = session.createQuery("FROM Season WHERE StartDate < :date AND EndDate > :date").setParameter("date", date);
        Season season;

        try {
            season = (Season) query.getSingleResult();
        }catch(NoResultException e){
            season = session.get(Season.class, Season.NO_SEASON_FOUND);
            return season;
        }

        return season;
    }
}

