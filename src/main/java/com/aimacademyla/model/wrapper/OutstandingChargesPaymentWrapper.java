package com.aimacademyla.model.wrapper;

import com.aimacademyla.model.Member;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

public class OutstandingChargesPaymentWrapper implements Serializable{

    private static final long serialVersionUID = 8281548987324398595L;

    private LocalDate cycleStartDate;
    private List<Member> outstandingBalanceMemberList;
    private List<Member> paidBalanceMemberList;
    private HashMap<Integer, BigDecimal> paymentAmountHashMap;
    private HashMap<Integer, BigDecimal> chargesAmountHashMap;
    private HashMap<Integer, BigDecimal> balanceAmountHashMap;

    public LocalDate getCycleStartDate() {
        return cycleStartDate;
    }

    public void setCycleStartDate(LocalDate cycleStartDate) {
        this.cycleStartDate = cycleStartDate;
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
}
