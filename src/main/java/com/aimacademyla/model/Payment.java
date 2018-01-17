package com.aimacademyla.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Entity
public class Payment extends AIMEntity implements Serializable {

    private static final long serialVersionUID = -1976230001989419509L;

    public static final int NO_PAYMENT = 1;

    public Payment(){
        chargeSet = new HashSet<>();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name="PaymentID")
    private int paymentID;

    @ManyToOne
    @JoinColumn(name="MemberID", referencedColumnName = "MemberID")
    private Member member;

    @Column(name="CycleStartDate")
    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private LocalDate cycleStartDate;

    @Column(name="PaymentAmount")
    @NumberFormat(style= NumberFormat.Style.CURRENCY)
    private BigDecimal paymentAmount;

    @Column(name="DatePaymentReceived")
    @DateTimeFormat(pattern="MM/dd/yyyy")
    private LocalDate datePaymentReceived;

    @ManyToOne
    @JoinColumn(name="MonthlyFinancesSummaryID", referencedColumnName = "MonthlyFinancesSummaryID")
    private MonthlyFinancesSummary monthlyFinancesSummary;

    @OneToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, mappedBy="payment")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Set<Charge> chargeSet;

    public void removeCharge(Charge charge){
        Iterator it = chargeSet.iterator();
        while(it.hasNext()){
            Charge iteratedCharge = (Charge) it.next();
            if(iteratedCharge.equals(charge)){
                it.remove();
                return;
            }
        }
    }

    public void addCharge(Charge charge) {
        if(chargeSet == null)
            return;

        chargeSet.add(charge);
        charge.setPayment(this);
    }

    public void updateCharge(Charge charge){
        removeCharge(charge);
        addCharge(charge);
    }

    @JsonIgnore
    public BigDecimal getBalance(){
        BigDecimal balance = BigDecimal.ZERO;

        for(Charge charge : chargeSet)
            balance = balance.add(charge.getChargeAmount()).subtract(charge.getDiscountAmount());

        balance = balance.subtract(paymentAmount);
        return balance;
    }

    @Override
    public int getBusinessID() {
        return paymentID;
    }

    @Override
    public void setBusinessID(int paymentID){
        this.paymentID = paymentID;
    }

    public int getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(int paymentID) {
        this.paymentID = paymentID;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public LocalDate getCycleStartDate() {
        return cycleStartDate;
    }

    public void setCycleStartDate(LocalDate date){
        this.cycleStartDate = date;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public LocalDate getDatePaymentReceived() {
        return datePaymentReceived;
    }

    public void setDatePaymentReceived(LocalDate datePaymentReceived) {
        this.datePaymentReceived = datePaymentReceived;
    }

    public MonthlyFinancesSummary getMonthlyFinancesSummary() {
        return monthlyFinancesSummary;
    }

    public void setMonthlyFinancesSummary(MonthlyFinancesSummary monthlyFinancesSummary) {
        this.monthlyFinancesSummary = monthlyFinancesSummary;
    }

    public Set<Charge> getChargeSet(){
        return chargeSet;
    }

    public void setChargeSet(Set<Charge> chargeSet){
        this.chargeSet = chargeSet;
    }

}
