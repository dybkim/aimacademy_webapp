package com.aimacademyla.model;

import com.aimacademyla.model.reference.TemporalReference;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.text.ParseException;
import java.time.LocalDate;

/**
 * Created by davidkim on 1/18/17.
 */

@Entity
public class Member implements Serializable{

    private static final long serialVersionUID = -6974012771726031996L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="MemberID")
    private int memberID;

    @Column(name="MemberFirstName")
    @NotEmpty(message = "Must provide member's first name")
    private String memberFirstName;

    @Column(name="MemberLastName")
    @NotEmpty(message = "Must provide member's last name")
    private String memberLastName;

    @Column(name="MemberAddress")
    private String memberStreetAddress;

    @Column(name="MemberAddressApartment")
    private String memberAddressApartment;

    @Column(name="MemberCity")
    private String memberCity;

    @Column(name="MemberState")
    private String memberState;

    @Column(name="MemberZipCode")
    private String memberZipCode;

    @Column(name="MemberEntryDate")
    @DateTimeFormat(pattern="MM/dd/yyyy")
    private LocalDate memberEntryDate;

    @Column(name="MemberPhoneNumber")
    private String memberPhoneNumber;

    @Column(name="MemberEmailAddress")
    private String memberEmailAddress;

    @Column(name="MemberIsActive")
    private boolean memberIsActive;

    public int getMemberID() {
        return memberID;
    }

    public void setMemberID(int memberID) {
        this.memberID = memberID;
    }

    public String getMemberFirstName() {
        return memberFirstName;
    }

    public void setMemberFirstName(String memberFirstName) {
        this.memberFirstName = memberFirstName;
    }

    public String getMemberLastName() {
        return memberLastName;
    }

    public void setMemberLastName(String memberLastName) {
        this.memberLastName = memberLastName;
    }

    public String getMemberStreetAddress() {
        return memberStreetAddress;
    }

    public void setMemberStreetAddress(String memberStreetAddress) {
        this.memberStreetAddress = memberStreetAddress;
    }

    public String getMemberAddressApartment() {
        return memberAddressApartment;
    }

    public void setMemberAddressApartment(String memberAddressApartment) {
        this.memberAddressApartment = memberAddressApartment;
    }

    public String getMemberCity() {
        return memberCity;
    }

    public void setMemberCity(String memberCity) {
        this.memberCity = memberCity;
    }

    public String getMemberState() {
        return memberState;
    }

    public void setMemberState(String memberState) {
        this.memberState = memberState;
    }

    public String getMemberZipCode() {
        return memberZipCode;
    }

    public void setMemberZipCode(String memberZipCode) {
        this.memberZipCode = memberZipCode;
    }

    public LocalDate getMemberEntryDate() throws ParseException{
        return this.memberEntryDate;
    }

    public void setMemberEntryDate(LocalDate memberEntryDate) {
        this.memberEntryDate = memberEntryDate;
    }

    public String getMemberPhoneNumber() {
        return memberPhoneNumber;
    }

    public void setMemberPhoneNumber(String memberPhoneNumber) {
        this.memberPhoneNumber = memberPhoneNumber;
    }

    public String getMemberEmailAddress() {
        return memberEmailAddress;
    }

    public void setMemberEmailAddress(String memberEmailAddress) {
        this.memberEmailAddress = memberEmailAddress;
    }

    public boolean getMemberIsActive() {
        return memberIsActive;
    }

    public void setMemberIsActive(boolean memberIsActive) {
        this.memberIsActive = memberIsActive;
    }

}
