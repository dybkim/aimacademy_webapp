package com.aimacademyla.model.factory;

import com.aimacademyla.dao.SeasonDAO;
import com.aimacademyla.model.Season;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * Created by davidkim on 7/4/17.
 */
public class SeasonFactory {

    private static SeasonDAO seasonDAO;

    @Autowired
    public SeasonFactory(SeasonDAO seasonDAO){
        this.seasonDAO = seasonDAO;
    }

    public Season createInstance(Date date){
        return null;
    }
}
