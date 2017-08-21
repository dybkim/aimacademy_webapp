package com.aimacademyla.model.wrapper;

import com.aimacademyla.model.Charge;
import com.aimacademyla.model.Payment;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MemberCourseFinancesWrapper implements Serializable{

    private static final long serialVersionUID = -4035158236340063169L;

    private List<Charge> chargeList;
    private LocalDate date;
    private BigDecimal totalChargeAmount;
    private BigDecimal totalPaymentAmount;
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

    public BigDecimal getTotalChargeAmount() {
        return totalChargeAmount;
    }

    public void setTotalChargeAmount(BigDecimal totalChargeAmount) {
        this.totalChargeAmount = totalChargeAmount;
    }

    public BigDecimal getTotalPaymentAmount() {
        return totalPaymentAmount;
    }

    public void setTotalPaymentAmount(BigDecimal totalPaymentAmount) {
        this.totalPaymentAmount = totalPaymentAmount;
    }

    public HashMap<Integer, Payment> getChargePaymentHashMap() {
        return chargePaymentHashMap;
    }

    public void setChargePaymentHashMap(HashMap<Integer, Payment> chargePaymentHashMap) {
        this.chargePaymentHashMap = chargePaymentHashMap;
    }
}
