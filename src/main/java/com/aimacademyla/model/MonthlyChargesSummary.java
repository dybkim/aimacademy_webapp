package com.aimacademyla.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by davidkim on 4/10/17.
 */
@Entity(name="Monthly_Charges_Summary")
public class MonthlyChargesSummary implements Serializable{

    private static final long serialVersionUID = 399088079685831204L;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="MonthlyChargesSummaryID")
    private int monthlyChargesSummaryID;

    @Column(name="SeasonID")
    private Integer seasonID;

    @Column(name="NumTotalCharges")
    private int numChargesTotal;

    @Column(name="NumChargesFulfilled")
    private int numChargesFulfilled;

    @Column(name="CycleStartDate")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern="MM/dd/yyyy")
    private Date cycleStartDate;

    @Column(name="NumMembers")
    private int numMembers;

    @Column(name="NumCourses")
    private int numCourses;

    public MonthlyChargesSummary(){
        numChargesTotal = 0;
        numChargesFulfilled = 0;
    }
    public int getMonthlyChargesSummaryID() {
        return monthlyChargesSummaryID;
    }

    public void setMonthlyChargesSummaryID(int monthlyChargesSummaryID) {
        this.monthlyChargesSummaryID = monthlyChargesSummaryID;
    }

    public int getNumChargesTotal() {
        return numChargesTotal;
    }

    public void setNumChargesTotal(int numChargesTotal) {
        this.numChargesTotal = numChargesTotal;
    }

    public int getNumChargesFulfilled() {
        return numChargesFulfilled;
    }

    public void setNumChargesFulfilled(int numChargesFulfilled) {
        this.numChargesFulfilled = numChargesFulfilled;
    }

    public Date getCycleStartDate() {
        return cycleStartDate;
    }

    public void setCycleStartDate(Date cycleStartDate) {
        this.cycleStartDate = cycleStartDate;
    }

    public int getNumMembers() {
        return numMembers;
    }

    public void setNumMembers(int numMembers) {
        this.numMembers = numMembers;
    }

    public Integer getSeasonID() {
        return seasonID;
    }

    public void setSeasonID(int seasonID) {
        this.seasonID = seasonID;
    }

    public int getNumCourses() {
        return numCourses;
    }

    public void setNumCourses(int numCourses) {
        this.numCourses = numCourses;
    }
}
