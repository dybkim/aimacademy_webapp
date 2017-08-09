package com.aimacademyla.model;

import com.aimacademyla.model.reference.TemporalReference;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.time.LocalDate;

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
    @Pattern(regexp="^(0|[1-9][0-9]*)$", message="Charge amount must be numeric")
    private double chargeAmount;

    @Column(name="CycleStartDate")
    @DateTimeFormat(pattern="MM/dd/yyyy")
    private LocalDate cycleStartDate;

    @Column(name="PaymentID")
    private Integer paymentID;

    @Column(name="SeasonID")
    private Integer seasonID;

    @Column(name="MonthlyChargesSummaryID")
    private Integer monthlyChargesSummaryID;

    @Column(name="Description")
    private String description;

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

    public LocalDate getCycleStartDate() {
        return cycleStartDate;
    }

    public void setCycleStartDate(LocalDate cycleStartDate) {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
