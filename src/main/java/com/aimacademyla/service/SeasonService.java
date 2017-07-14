package com.aimacademyla.service;

import com.aimacademyla.model.Season;

import java.util.Date;

/**
 * Created by davidkim on 5/22/17.
 */
public interface SeasonService extends GenericService<Season, Integer>{
    Season getSeason(Date date);
}
