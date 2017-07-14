package com.aimacademyla.dao;

import com.aimacademyla.model.Season;

import java.util.Date;

/**
 * Created by davidkim on 5/22/17.
 */
public interface SeasonDAO extends GenericDAO<Season, Integer>{
    Season getSeason(Date date);
}
