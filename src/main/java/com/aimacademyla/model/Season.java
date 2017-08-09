package com.aimacademyla.model;

import com.aimacademyla.model.reference.TemporalReference;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * Created by davidkim on 5/22/17.
 */
@Entity
public class Season implements Serializable {

    private static final long serialVersionUID = 681613110921037803L;

    public static final int NO_SEASON_FOUND = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="SeasonID")
    private int seasonID;

    @Column(name="SeasonTitle")
    private String seasonTitle;

    @Column(name="StartDate")
    @DateTimeFormat(pattern="MM/dd/yyyy")
    private LocalDate startDate;

    @Column(name="EndDate")
    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private LocalDate endDate;

    public int getSeasonID() {
        return seasonID;
    }

    public void setSeasonID(int seasonID) {
        this.seasonID = seasonID;
    }

    public String getSeasonTitle() {
        return seasonTitle;
    }

    public void setSeasonTitle(String seasonTitle) {
        this.seasonTitle = seasonTitle;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
