package com.aimacademyla.model;

import com.aimacademyla.model.reference.TemporalReference;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Charge Entity represents a cumulation of ChargeLine entities
 *
 * Created by davidkim on 3/9/17.
 */

@Entity(name="Charges")
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
    @NumberFormat(style= NumberFormat.Style.CURRENCY)
//    @Pattern(regexp="^(0|[1-9][0-9]*)$", message="Charge amount must be numeric")
    private BigDecimal chargeAmount;

    @Column(name="CycleStartDate")
    @DateTimeFormat(pattern="MM/dd/yyyy")
    private LocalDate cycleStartDate;

    @Column(name="PaymentID")
    private Integer paymentID;

    @Column(name="SeasonID")
    private Integer seasonID;

    @Column(name="MonthlyFinancesSummaryID")
    private Integer monthlyFinancesSummaryID;

    @Column(name="Description")
    private String description;

    @Column(name="DiscountAmount")
    @NumberFormat(style= NumberFormat.Style.CURRENCY)
    private BigDecimal discountAmount;

    @Column(name="NumChargeLines")
    private int numChargeLines;

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

    public BigDecimal getChargeAmount() {
        return chargeAmount;
    }

    public void setChargeAmount(BigDecimal chargeAmount) {
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

    public Integer getMonthlyFinancesSummaryID() {
        return monthlyFinancesSummaryID;
    }

    public void setMonthlyFinancesSummaryID(Integer monthlyFinancesSummaryID) {
        this.monthlyFinancesSummaryID = monthlyFinancesSummaryID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public int getNumChargeLines() {
        return numChargeLines;
    }

    public void setNumChargeLines(int numChargeLines) {
        this.numChargeLines = numChargeLines;
    }
}
