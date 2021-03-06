package com.aimacademyla.model.dto;

import com.aimacademyla.model.Charge;
import com.aimacademyla.model.Member;
import com.aimacademyla.model.temporal.CyclePeriod;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

public class OutstandingChargesPaymentDTO implements Serializable{

    private static final long serialVersionUID = 8281548987324398595L;

    private CyclePeriod cyclePeriod;
    private List<Member> outstandingBalanceMemberList;
    private List<Member> paidBalanceMemberList;
    private HashMap<Integer, BigDecimal> paymentAmountHashMap;
    private HashMap<Integer, BigDecimal> chargesAmountHashMap;
    private HashMap<Integer, BigDecimal> balanceAmountHashMap;
    private HashMap<Integer, List<Charge>> chargeListHashMap;

    public CyclePeriod getCyclePeriod() {
        return cyclePeriod;
    }

    public void setCyclePeriod(CyclePeriod cyclePeriod) {
        this.cyclePeriod = cyclePeriod;
    }

    public List<Member> getOutstandingBalanceMemberList() {
        return outstandingBalanceMemberList;
    }

    public void setOutstandingBalanceMemberList(List<Member> memberList) {
        this.outstandingBalanceMemberList = memberList;
    }

    public List<Member> getPaidBalanceMemberList() {
        return paidBalanceMemberList;
    }

    public void setPaidBalanceMemberList(List<Member> paidBalanceMemberList) {
        this.paidBalanceMemberList = paidBalanceMemberList;
    }

    public HashMap<Integer, BigDecimal> getPaymentAmountHashMap() {
        return paymentAmountHashMap;
    }

    public void setPaymentAmountHashMap(HashMap<Integer, BigDecimal> paymentAmountHashMap) {
        this.paymentAmountHashMap = paymentAmountHashMap;
    }

    public HashMap<Integer, BigDecimal> getChargesAmountHashMap() {
        return chargesAmountHashMap;
    }

    public void setChargesAmountHashMap(HashMap<Integer, BigDecimal> chargesAmountHashMap) {
        this.chargesAmountHashMap = chargesAmountHashMap;
    }

    public HashMap<Integer, BigDecimal> getBalanceAmountHashMap() {
        return balanceAmountHashMap;
    }

    public void setBalanceAmountHashMap(HashMap<Integer, BigDecimal> balanceAmountHashMap) {
        this.balanceAmountHashMap = balanceAmountHashMap;
    }

    public HashMap<Integer, List<Charge>> getChargeListHashMap() {
        return chargeListHashMap;
    }

    public void setChargeListHashMap(HashMap<Integer, List<Charge>> chargeListHashMap) {
        this.chargeListHashMap = chargeListHashMap;
    }
}

