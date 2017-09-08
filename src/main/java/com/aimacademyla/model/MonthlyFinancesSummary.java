package com.aimacademyla.model;

import com.aimacademyla.model.reference.TemporalReference;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Created by davidkim on 4/10/17.
 */
@Entity(name="Monthly_Finances_Summary")
public class MonthlyFinancesSummary implements Serializable{

    private static final long serialVersionUID = 399088079685831204L;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="MonthlyFinancesSummaryID")
    private int monthlyFinancesSummaryID;

    @Column(name="SeasonID")
    private Integer seasonID;

    @Column(name="NumTotalCharges")
    private int numTotalCharges;

    @Column(name="NumChargesFulfilled")
    private int numChargesFulfilled;

    @Column(name="CycleStartDate")
    @DateTimeFormat(pattern="MM/dd/yyyy")
    private LocalDate cycleStartDate;

    @Column(name="NumMembers")
    private int numMembers;

    @Column(name="NumCourses")
    private int numCourses;

    @Column(name="TotalChargeAmount")
    @NumberFormat(style= NumberFormat.Style.CURRENCY)
    private BigDecimal totalChargeAmount;

    @Column(name="TotalPaymentAmount")
    @NumberFormat(style= NumberFormat.Style.CURRENCY)
    private BigDecimal totalPaymentAmount;

    public MonthlyFinancesSummary(){
        numTotalCharges = 0;
        numChargesFulfilled = 0;
    }
    public int getMonthlyFinancesSummaryID() {
        return monthlyFinancesSummaryID;
    }

    public void setMonthlyFinancesSummaryID(int monthlyFinancesSummaryID) {
        this.monthlyFinancesSummaryID = monthlyFinancesSummaryID;
    }

    public int getNumTotalCharges() {
        return numTotalCharges;
    }

    public void setNumTotalCharges(int numTotalCharges) {
        this.numTotalCharges = numTotalCharges;
    }

    public int getNumChargesFulfilled() {
        return numChargesFulfilled;
    }

    public void setNumChargesFulfilled(int numChargesFulfilled) {
        this.numChargesFulfilled = numChargesFulfilled;
    }

    public LocalDate getCycleStartDate() {
        return cycleStartDate;
    }

    public void setCycleStartDate(LocalDate cycleStartDate) {
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

    public BigDecimal getTotalChargeAmount() {
        return totalChargeAmount;
    }

    public void setTotalChargeAmount(BigDecimal totalChargeAmount) {
        this.totalChargeAmount = totalChargeAmount;
    }

    public BigDecimal getTotalPaymentAmount() {
        return totalPaymentAmount;
    }

    public void setTotalPaymentAmount(BigDecimal totalPaymentAmount) {
        this.totalPaymentAmount = totalPaymentAmount;
    }
}
