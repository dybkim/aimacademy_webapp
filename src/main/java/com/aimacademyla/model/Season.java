package com.aimacademyla.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by davidkim on 5/22/17.
 */
public class Season implements Serializable {

    private static final long serialVersionUID = 681613110921037803L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="SeasonID")
    private int seasonID;

    @Column(name="StartDate")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern="MM/dd/yyyy")
    private Date startDate;

    @Column(name="EndDate")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private Date endDate;

    public int getSeasonID() {
        return seasonID;
    }

    public void setSeasonID(int seasonID) {
        this.seasonID = seasonID;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
