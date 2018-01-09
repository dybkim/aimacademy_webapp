package com.aimacademyla.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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
    @Length(max=30)
    private String memberFirstName;

    @Column(name="MemberLastName")
    @NotEmpty(message = "Must provide member's last name")
    @Length(max=30)
    private String memberLastName;

    @Column(name="MemberAddress")
    @Length(max=30)
    private String memberStreetAddress;

    @Column(name="MemberAddressApartment")
    @Length(max=10)
    private String memberAddressApartment;

    @Column(name="MemberCity")
    @Length(max=30)
    private String memberCity;

    @Column(name="MemberState")
    @Length(max=2)
    private String memberState;

    @Column(name="MemberZipCode")
    @Length(max=5)
    private String memberZipCode;

    @Column(name="MemberEntryDate")
    @DateTimeFormat(pattern="MM/dd/yyyy")
    private LocalDate memberEntryDate;

    @Column(name="MemberPhoneNumber")
    @Length(max=10)
    private String memberPhoneNumber;

    @Column(name="MemberEmailAddress")
    @Length(max=40)
    private String memberEmailAddress;

    @Column(name="MembershipRate")
    @NumberFormat(style= NumberFormat.Style.CURRENCY)
    private BigDecimal membershipRate;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "member")
    @MapKey(name="memberCourseRegistrationID")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Map<Integer, MemberCourseRegistration> memberCourseRegistrationMap;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "member")
    @MapKey(name="memberMonthlyRegistrationID")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Map<Integer, MemberMonthlyRegistration> memberMonthlyRegistrationMap;

    @Override
    public boolean equals(Object object) throws IllegalArgumentException{
        if(!(object instanceof Member))
            throw new IllegalArgumentException("Parameter must be of Member type!");

        Member member = (Member) object;
        return memberID == member.getMemberID();
    }

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

    public BigDecimal getMembershipRate() {
        return membershipRate;
    }

    public void setMembershipRate(BigDecimal membershipRate) {
        this.membershipRate = membershipRate;
    }

    public void setMemberCourseRegistrationMap(Map<Integer, MemberCourseRegistration> memberCourseRegistrationMap) {
        this.memberCourseRegistrationMap = memberCourseRegistrationMap;
    }

    public void setMemberMonthlyRegistrationMap(Map<Integer, MemberMonthlyRegistration> memberMonthlyRegistrationMap) {
        this.memberMonthlyRegistrationMap = memberMonthlyRegistrationMap;
    }

    /*
     * IMPORTANT: Requires JsonIgnore annotation or else when a member object is converted to JSON,
     * Jackson Data Binding will try and lazy load the following collections
     */
    @JsonIgnore
    public Map<Integer, MemberCourseRegistration> getMemberCourseRegistrationMap() {
        return memberCourseRegistrationMap;
    }

    @JsonIgnore
    public Map<Integer, MemberMonthlyRegistration> getMemberMonthlyRegistrationMap() {
        return memberMonthlyRegistrationMap;
    }


}
