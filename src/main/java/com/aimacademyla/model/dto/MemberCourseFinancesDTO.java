package com.aimacademyla.model.dto;

import com.aimacademyla.model.Charge;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class MemberCourseFinancesDTO implements Serializable{

    private static final long serialVersionUID = -4035158236340063169L;

    private List<Charge> chargeList;
    private LocalDate date;
    private BigDecimal totalChargeAmount;
    private BigDecimal totalPaymentAmount;

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
}
