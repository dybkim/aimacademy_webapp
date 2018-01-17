package com.aimacademyla.model;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Created by davidkim on 6/14/17.
 */

@Entity(name="Member_Monthly_Registration")
public class MemberMonthlyRegistration extends AIMEntity implements Serializable{

    private static final long serialVersionUID = -1918184776043140894L;
    public static final int INACTIVE = -1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="MemberMonthlyRegistrationID")
    private int memberMonthlyRegistrationID;

    @ManyToOne
    @JoinColumn(name="MemberID", referencedColumnName = "MemberID")
    private Member member;

    @ManyToOne
    @JoinColumn(name="SeasonID", referencedColumnName = "SeasonID")
    private Season season;

    @Column(name="CycleStartDate")
    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private LocalDate cycleStartDate;

    @Column(name="MembershipCharge")
    @NumberFormat(style= NumberFormat.Style.CURRENCY)
    private BigDecimal membershipCharge;

    @OneToOne(cascade = {CascadeType.MERGE,CascadeType.REFRESH})
    @JoinColumn(name="ChargeID", referencedColumnName = "ChargeID")
    private Charge charge;

    @Override
    public int getBusinessID() {
        return memberMonthlyRegistrationID;
    }

    @Override
    public void setBusinessID(int memberMonthlyRegistrationID){
        this.memberMonthlyRegistrationID = memberMonthlyRegistrationID;
    }

    public int getMemberMonthlyRegistrationID() {
        return memberMonthlyRegistrationID;
    }

    public void setMemberMonthlyRegistrationID(int memberMonthlyRegistrationID) {
        this.memberMonthlyRegistrationID = memberMonthlyRegistrationID;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Season getSeason() {
        return season;
    }

    public void setSeason(Season season) {
        this.season = season;
    }

    public LocalDate getCycleStartDate() {
        return cycleStartDate;
    }

    public void setCycleStartDate(LocalDate cycleStartDate) {
        this.cycleStartDate = cycleStartDate;
    }

    public BigDecimal getMembershipCharge() {
        return membershipCharge;
    }

    public void setMembershipCharge(BigDecimal membershipCharge) {
        this.membershipCharge = membershipCharge;
    }

    public Charge getCharge() {
        return charge;
    }

    public void setCharge(Charge charge) {
        this.charge = charge;
    }
}
