package com.aimacademyla.dao.impl;

import com.aimacademyla.dao.SeasonDAO;
import com.aimacademyla.model.Charge;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    public void removeList(List<Season> seasonList){
        Session session = currentSession();
        List<Integer> seasonIDList = new ArrayList<>();
        for(Season season : seasonList)
            seasonIDList.add(season.getSeasonID());
        Query query = session.createQuery("DELETE FROM Season S WHERE S.seasonID in :seasonIDList");
        query.setParameterList("seasonIDList", seasonIDList);
        query.executeUpdate();
    }
}

