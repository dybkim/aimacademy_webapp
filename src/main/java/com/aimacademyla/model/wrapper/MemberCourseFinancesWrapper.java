package com.aimacademyla.model.wrapper;

import com.aimacademyla.model.Charge;
import com.aimacademyla.model.Payment;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MemberCourseFinancesWrapper implements Serializable{

    private static final long serialVersionUID = -4035158236340063169L;

    private List<Charge> chargeList;
    private LocalDate date;
    private double totalChargeAmount;
    private double totalPaymentAmount;
    private HashMap<Integer, Payment> chargePaymentHashMap;

    public List<Charge> getChargeList() {
        return chargeList;
    }

    public void setChargeList(List<Charge> chargeList) {
        this.chargeList = chargeList;
    }

    public LocalDate getDate(){
        return date;
    }

    public void setDate(LocalDate date){
        this.date = date;
    }

    public double getTotalChargeAmount() {
        return totalChargeAmount;
    }

    public void setTotalChargeAmount(double totalChargeAmount) {
        this.totalChargeAmount = totalChargeAmount;
    }

    public double getTotalPaymentAmount() {
        return totalPaymentAmount;
    }

    public void setTotalPaymentAmount(double totalPaymentAmount) {
        this.totalPaymentAmount = totalPaymentAmount;
    }

    public HashMap<Integer, Payment> getChargePaymentHashMap() {
        return chargePaymentHashMap;
    }

    public void setChargePaymentHashMap(HashMap<Integer, Payment> chargePaymentHashMap) {
        this.chargePaymentHashMap = chargePaymentHashMap;
    }

    public static class MemberCourseFinancesWrapperBuilder{
        private List<Charge> chargeList;
        private LocalDate date;
        private double totalChargeAmount;
        private double totalPaymentAmount;
        private HashMap<Integer, Payment> chargePaymentHashMap;

        public MemberCourseFinancesWrapperBuilder setChargeList(List<Charge> chargeList) {
            this.chargeList = chargeList;
            return this;
        }

        public MemberCourseFinancesWrapperBuilder setDate(LocalDate date) {
            this.date = date;
            return this;
        }

        public MemberCourseFinancesWrapperBuilder setTotalChargeAmount(double totalChargeAmount) {
            this.totalChargeAmount = totalChargeAmount;
            return this;
        }

        public MemberCourseFinancesWrapperBuilder setTotalPaymentAmount(double totalPaymentAmount) {
            this.totalPaymentAmount = totalPaymentAmount;
            return this;
        }

        public MemberCourseFinancesWrapperBuilder setChargePaymentHashMap(HashMap<Integer, Payment> chargePaymentHashMap) {
            this.chargePaymentHashMap = chargePaymentHashMap;
            return this;
        }

        public MemberCourseFinancesWrapper build(){
            MemberCourseFinancesWrapper memberCourseFinancesWrapper = new MemberCourseFinancesWrapper();
            memberCourseFinancesWrapper.setChargeList(chargeList);
            memberCourseFinancesWrapper.setDate(date);
            memberCourseFinancesWrapper.setTotalChargeAmount(totalChargeAmount);
            memberCourseFinancesWrapper.setTotalPaymentAmount(totalPaymentAmount);
            memberCourseFinancesWrapper.setChargePaymentHashMap(chargePaymentHashMap);
            return memberCourseFinancesWrapper;
        }
    }
}
