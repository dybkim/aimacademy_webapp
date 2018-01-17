package com.aimacademyla.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.security.access.method.P;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

/**
 * Created by davidkim on 4/10/17.
 */
@Entity(name="Monthly_Finances_Summary")
public class MonthlyFinancesSummary extends AIMEntity implements Serializable{

    private static final long serialVersionUID = 399088079685831204L;

    private static final Logger logger = LogManager.getLogger(MonthlyFinancesSummary.class.getName());

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="MonthlyFinancesSummaryID")
    private int monthlyFinancesSummaryID;

    @ManyToOne
    @JoinColumn(name="SeasonID", referencedColumnName = "SeasonID")
    private Season season;

    @Column(name="NumTotalCharges")
    private int numTotalCharges;

    @Column(name="NumChargesFulfilled")
    private int numChargesFulfilled;

    @Column(name="NumTotalPayments")
    private int numTotalPayments;

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

    @OneToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH},
            mappedBy = "monthlyFinancesSummary",
            orphanRemoval = true)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Set<Charge> chargeSet;

    @OneToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH},
            mappedBy = "monthlyFinancesSummary",
            orphanRemoval = true)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Set<Payment> paymentSet;

    public MonthlyFinancesSummary(){
        numTotalCharges = 0;
        numChargesFulfilled = 0;
        chargeSet = new HashSet<>();
        paymentSet = new HashSet<>();
    }

    public void addCharge(Charge charge){
        if(chargeSet == null)
            return;

        if(getCharge(charge.getChargeID()) != null){
            updateCharge(charge);
            return;
        }

        logger.debug("Current totalChargeAmount is: " + totalChargeAmount + ", and numTotalCharges: " + numTotalCharges);
        logger.debug("Adding Charge: " + charge.getChargeID() + " with chargeAmount: " + charge.getChargeAmount());
        totalChargeAmount = totalChargeAmount.add((charge.getChargeAmount()).subtract(charge.getDiscountAmount()));

        chargeSet.add(charge);
        charge.setMonthlyFinancesSummary(this);
        numTotalCharges = chargeSet.size();
        logger.debug("New totalChargeAmount is: " + totalChargeAmount +", and numTotalCharges: " + numTotalCharges);
    }

    public void updateCharge(Charge charge){
        if(chargeSet == null)
            return;

        logger.debug("Updating Charge: " + charge.getChargeID());
        removeCharge(charge);
        addCharge(charge);
    }

    public void removeCharge(Charge charge){
        if(chargeSet == null)
            return;

        Charge persistedCharge = getCharge(charge.getChargeID());
        if(persistedCharge == null)
            return;

        logger.debug("Current totalChargeAmount is: " + totalChargeAmount + ", and numTotalCharges: " + numTotalCharges);
        logger.debug("Removing Charge: " + persistedCharge.getChargeID() + " with chargeAmount: " + persistedCharge.getChargeAmount());
        totalChargeAmount = totalChargeAmount.subtract((persistedCharge.getChargeAmount()).subtract(persistedCharge.getDiscountAmount()));
        logger.debug("New totalChargeAmount is: " + totalChargeAmount +", and numTotalCharges: " + numTotalCharges);
        Iterator it = chargeSet.iterator();
        while(it.hasNext()){
            Charge iteratedCharge = (Charge) it.next();
            if(iteratedCharge.equals(charge)){
                it.remove();
                numTotalCharges = chargeSet.size();
                break;
            }
        }
    }

    private Payment getPayment(int paymentID){
        for(Payment payment : paymentSet)
            if(payment.getPaymentID() == paymentID)
                return payment;
        return null;
    }

    public void addPayment(Payment payment){
        if(paymentSet == null)
            return;

        totalPaymentAmount = totalPaymentAmount.add(payment.getPaymentAmount());
        numTotalPayments++;

        paymentSet.add(payment);
        payment.setMonthlyFinancesSummary(this);
    }

    public void updatePayment(Payment payment){
        if(paymentSet == null)
            return;

        removePayment(payment);
        addPayment(payment);
    }

    public  void removePayment(Payment payment){
        if(paymentSet == null)
            return;

        Payment persistedPayment = getPayment(payment.getPaymentID());
        if(persistedPayment == null)
            return;

        totalPaymentAmount = totalPaymentAmount.subtract(payment.getPaymentAmount());
        numTotalPayments--;
        Iterator it = paymentSet.iterator();
        while(it.hasNext()){
            Payment iteratedPayment = (Payment) it.next();
            if(iteratedPayment.equals(payment)){
                it.remove();
                break;
            }
        }
    }

    @Override
    public int getBusinessID() {
        return monthlyFinancesSummaryID;
    }

    @Override
    public void setBusinessID(int monthlyFinancesSummaryID){
        this.monthlyFinancesSummaryID = monthlyFinancesSummaryID;
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

    public int getNumTotalPayments() {
        return numTotalPayments;
    }

    public void setNumTotalPayments(int numTotalPayments) {
        this.numTotalPayments = numTotalPayments;
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

    public Season getSeason() {
        return season;
    }

    public void setSeason(Season season) {
        this.season = season;
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

    public Set<Charge> getChargeSet() {
        return chargeSet;
    }

    public void setChargeSet(Set<Charge> chargeSet) {
        this.chargeSet = chargeSet;
    }

    public Set<Payment> getPaymentSet() {
        return paymentSet;
    }

    public void setPaymentSet(Set<Payment> paymentSet){
        this.paymentSet = paymentSet;
    }

    private Charge getCharge(int chargeID){
        for(Charge charge : chargeSet)
            if(charge.getChargeID() == chargeID)
                return charge;

        return null;
    }
}
