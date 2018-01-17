package com.aimacademyla.service.impl;

import com.aimacademyla.dao.GenericDAO;
import com.aimacademyla.dao.flow.impl.SeasonDAOAccessFlow;
import com.aimacademyla.model.Season;
import com.aimacademyla.service.SeasonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * Created by davidkim on 5/22/17.
 */

@Service
public class SeasonServiceImpl extends GenericServiceImpl<Season, Integer> implements SeasonService{

    @Autowired
    public SeasonServiceImpl(@Qualifier("seasonDAO") GenericDAO<Season, Integer> genericDAO){
        super(genericDAO);
    }

    @Override
    public Season getSeason(LocalDate date) {
        return (Season) new SeasonDAOAccessFlow()
                                    .addQueryParameter(date)
                                    .get();
    }
}
