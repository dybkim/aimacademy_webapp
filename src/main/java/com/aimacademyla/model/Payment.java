package com.aimacademyla.model;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Payment implements Serializable {

    private static final long serialVersionUID = -1976230001989419509L;

    public static final int NO_PAYMENT = -1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="PaymentID")
    private int paymentID;

    @Column(name="MemberID")
    private int memberID;

    @Column(name="ChargeID")
    private int chargeID;

    @Column(name="PaymentAmount")
    @NumberFormat(style= NumberFormat.Style.CURRENCY)
    private BigDecimal paymentAmount;

    @Column(name="DatePaymentReceived")
    @DateTimeFormat(pattern="MM/dd/yyyy")
    private LocalDate datePaymentReceived;

    public int getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(int paymentID) {
        this.paymentID = paymentID;
    }

    public int getMemberID() {
        return memberID;
    }

    public void setMemberID(int memberID) {
        this.memberID = memberID;
    }

    public int getChargeID() {
        return chargeID;
    }

    public void setChargeID(int chargeID) {
        this.chargeID = chargeID;
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
}
