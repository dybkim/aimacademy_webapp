package com.aimacademyla.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Charge Entity represents a cumulation of ChargeLine entities
 *
 * Created by davidkim on 3/9/17.
 */

@Entity(name="Charge")
public class Charge implements Serializable{

    private static final long serialVersionUID = 1346555619431916040L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="ChargeID")
    private int chargeID;

    @Column(name="MemberID")
    private int memberID;

    @Column(name="CourseID")
    private int courseID;

    @Column(name="ChargeAmount")
    private double chargeAmount;

    @Column(name="CycleStartDate")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern="MM/dd/yyyy")
    private Date cycleStartDate;

    @Column(name="PaymentID")
    private Integer paymentID;

    @Column(name="SeasonID")
    private Integer seasonID;

    @Column(name="MonthlyChargesSummaryID")
    private Integer monthlyChargesSummaryID;

    public int getChargeID() {
        return chargeID;
    }

    public void setChargeID(int chargeID) {
        this.chargeID = chargeID;
    }

    public int getMemberID() {
        return memberID;
    }

    public void setMemberID(int memberID) {
        this.memberID = memberID;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public double getChargeAmount() {
        return chargeAmount;
    }

    public void setChargeAmount(double chargeAmount) {
        this.chargeAmount = chargeAmount;
    }

    public Date getCycleStartDate() {
        return cycleStartDate;
    }

    public void setCycleStartDate(Date cycleStartDate) {
        this.cycleStartDate = cycleStartDate;
    }

    public Integer getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(Integer paymentID) {
        this.paymentID = paymentID;
    }

    public Integer getSeasonID() {
        return seasonID;
    }

    public void setSeasonID(Integer seasonID) {
        this.seasonID = seasonID;
    }

    public Integer getMonthlyChargesSummaryID() {
        return monthlyChargesSummaryID;
    }

    public void setMonthlyChargesSummaryID(Integer monthlyChargesSummaryID) {
        this.monthlyChargesSummaryID = monthlyChargesSummaryID;
    }
}
