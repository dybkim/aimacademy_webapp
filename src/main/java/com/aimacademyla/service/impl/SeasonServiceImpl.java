package com.aimacademyla.service.impl;

import com.aimacademyla.dao.GenericDAO;
import com.aimacademyla.dao.SeasonDAO;
import com.aimacademyla.model.Season;
import com.aimacademyla.service.SeasonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;

/**
 * Created by davidkim on 5/22/17.
 */

@Service
public class SeasonServiceImpl extends GenericServiceImpl<Season, Integer> implements SeasonService{

    private SeasonDAO seasonDAO;

    @Autowired
    public SeasonServiceImpl(@Qualifier("seasonDAO") GenericDAO<Season, Integer> genericDAO){
        super(genericDAO);
        this.seasonDAO = (SeasonDAO) genericDAO;
    }

    @Override
    public Season getSeason(LocalDate date) {
        Season season = seasonDAO.getSeasonByDate(date);
        
        if(season == null)
            season = seasonDAO.get(Season.NO_SEASON_FOUND);

        return season;
    }

}
