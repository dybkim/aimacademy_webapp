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
    private List<Member> memberList;
    private List<Member> inactiveMemberList;
    private HashMap<Integer, BigDecimal> outstandingChargesHashMap;
    private HashMap<Integer, BigDecimal> inactiveOutstandingChargesHashMap;

    public LocalDate getCycleStartDate() {
        return cycleStartDate;
    }

    public void setCycleStartDate(LocalDate cycleStartDate) {
        this.cycleStartDate = cycleStartDate;
    }

    public List<Member> getMemberList() {
        return memberList;
    }

    public void setMemberList(List<Member> memberList) {
        this.memberList = memberList;
    }

    public List<Member> getInactiveMemberList() {
        return inactiveMemberList;
    }

    public void setInactiveMemberList(List<Member> inactiveMemberList) {
        this.inactiveMemberList = inactiveMemberList;
    }

    public HashMap<Integer, BigDecimal> getOutstandingChargesHashMap() {
        return outstandingChargesHashMap;
    }

    public void setOutstandingChargesHashMap(HashMap<Integer, BigDecimal> outstandingChargesHashMap) {
        this.outstandingChargesHashMap = outstandingChargesHashMap;
    }

    public HashMap<Integer, BigDecimal> getInactiveOutstandingChargesHashMap() {
        return inactiveOutstandingChargesHashMap;
    }

    public void setInactiveOutstandingChargesHashMap(HashMap<Integer, BigDecimal> inactiveOutstandingChargesHashMap) {
        this.inactiveOutstandingChargesHashMap = inactiveOutstandingChargesHashMap;
    }
}
