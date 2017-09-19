package com.aimacademyla.model.wrapper;

import com.aimacademyla.model.Charge;
import com.aimacademyla.model.ChargeLine;
import com.aimacademyla.model.Course;
import com.aimacademyla.model.Member;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

public class MemberChargesFinancesWrapper implements Serializable{

    private static final long serialVersionUID = 6771940585400057881L;

    /**
     * All hashmaps are mapped with chargeID's as the key
     */

    private Member member;

    private HashMap<Integer, Charge> chargeHashMap;

    private HashMap<Integer, Course> courseHashMap;

    private HashMap<Integer, List<ChargeLine>> chargeLineListHashMap;

    private HashMap<Integer, BigDecimal> hoursBilledHashMap;

    private List<LocalDate> monthsList;

    private LocalDate cycleStartDate;

    private int monthSelectedIndex;

    private BigDecimal hoursBilledTotal;

    private BigDecimal totalChargesAmount;

    private BigDecimal totalDiscountAmount;

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public HashMap<Integer, Charge> getChargeHashMap() {
        return chargeHashMap;
    }

    public void setChargeHashMap(HashMap<Integer, Charge> chargeHashMap){
        this.chargeHashMap = chargeHashMap;
    }

    public LocalDate getCycleStartDate() {
        return cycleStartDate;
    }

    public void setCycleStartDate(LocalDate cycleStartDate) {
        this.cycleStartDate = cycleStartDate;
    }

    public HashMap<Integer, Course> getCourseHashMap() {
        return courseHashMap;
    }

    public void setCourseHashMap(HashMap<Integer, Course> courseHashMap) {
        this.courseHashMap = courseHashMap;
    }

    public HashMap<Integer, List<ChargeLine>> getChargeLineListHashMap() {
        return chargeLineListHashMap;
    }

    public void setChargeLineListHashMap(HashMap<Integer, List<ChargeLine>> chargeLineListHashMap) {
        this.chargeLineListHashMap = chargeLineListHashMap;
    }

    public HashMap<Integer, BigDecimal> getHoursBilledHashMap() {
        return hoursBilledHashMap;
    }

    public void setHoursBilledHashMap(HashMap<Integer, BigDecimal> hoursBilledHashMap) {
        this.hoursBilledHashMap = hoursBilledHashMap;
    }

    public List<LocalDate> getMonthsList() {
        return monthsList;
    }

    public void setMonthsList(List<LocalDate> monthsList) {
        this.monthsList = monthsList;
    }

    public int getMonthSelectedIndex() {
        return monthSelectedIndex;
    }

    public void setMonthSelectedIndex(int monthSelectedIndex) {
        this.monthSelectedIndex = monthSelectedIndex;
    }

    public BigDecimal getHoursBilledTotal() {
        return hoursBilledTotal;
    }

    public void setHoursBilledTotal(BigDecimal hoursBilledTotal) {
        this.hoursBilledTotal = hoursBilledTotal;
    }

    public BigDecimal getTotalChargesAmount() {
        return totalChargesAmount;
    }

    public void setTotalChargesAmount(BigDecimal totalChargesAmount) {
        this.totalChargesAmount = totalChargesAmount;
    }

    public BigDecimal getTotalDiscountAmount() {
        return totalDiscountAmount;
    }

    public void setTotalDiscountAmount(BigDecimal totalDiscountAmount) {
        this.totalDiscountAmount = totalDiscountAmount;
    }

}
