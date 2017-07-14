package com.aimacademyla.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by davidkim on 6/14/17.
 */

@Entity(name="Member_Monthly_Registration")
public class MemberMonthlyRegistration implements Serializable{

    private static final long serialVersionUID = -1918184776043140894L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="MemberMonthlyRegistrationID")
    private int memberMonthlyRegistrationID;

    @Column(name="MemberID")
    private int memberID;

    @Column(name="SeasonID")
    private int seasonID;

    @Column(name="CycleStartDate")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private Date cycleStartDate;

    public int getMemberMonthlyRegistrationID() {
        return memberMonthlyRegistrationID;
    }

    public void setMemberMonthlyRegistrationID(int memberMonthlyRegistrationID) {
        this.memberMonthlyRegistrationID = memberMonthlyRegistrationID;
    }

    public int getMemberID() {
        return memberID;
    }

    public void setMemberID(int memberID) {
        this.memberID = memberID;
    }

    public int getSeasonID() {
        return seasonID;
    }

    public void setSeasonID(int seasonID) {
        this.seasonID = seasonID;
    }

    public Date getCycleStartDate() {
        return cycleStartDate;
    }

    public void setCycleStartDate(Date cycleStartDate) {
        this.cycleStartDate = cycleStartDate;
    }
}
