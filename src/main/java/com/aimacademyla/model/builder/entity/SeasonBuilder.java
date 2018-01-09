package com.aimacademyla.model.builder.entity;

import com.aimacademyla.model.Season;
import com.aimacademyla.model.builder.GenericBuilder;

import java.time.LocalDate;

public class SeasonBuilder implements GenericBuilder<Season>{
    private LocalDate localDate;

    public SeasonBuilder setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
        return this;
    }

    @Override
    public Season build() {
        Season season = new Season();
        Season.SeasonDescription seasonDescription = Season.SeasonDescription.getSeasonDescription(localDate);
        season.setStartDate(Season.SeasonDescription.getSeasonStartDate(localDate));
        season.setSeasonTitle(seasonDescription.toString(localDate));
        season.setEndDate(Season.SeasonDescription.getSeasonEndDate(localDate));
        return season;
    }
}
