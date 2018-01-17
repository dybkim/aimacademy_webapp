package com.aimacademyla.model.builder.entity;

import com.aimacademyla.model.MonthlyFinancesSummary;
import com.aimacademyla.model.Season;
import com.aimacademyla.model.builder.GenericBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MonthlyFinancesSummaryBuilder implements GenericBuilder<MonthlyFinancesSummary>{

    private int monthlyFinancesSummaryID;
    private LocalDate cycleStartDate;
    private Season season;
    private int numCourses;
    private int numTotalCharges;
    private int numMembers;
    private BigDecimal totalChargeAmount;
    private BigDecimal totalPaymentAmount;

    @Override
    public MonthlyFinancesSummary build() {
        MonthlyFinancesSummary monthlyFinancesSummary = new MonthlyFinancesSummary();
        monthlyFinancesSummary.setMonthlyFinancesSummaryID(monthlyFinancesSummaryID);
        monthlyFinancesSummary.setCycleStartDate(cycleStartDate);
        monthlyFinancesSummary.setSeason(season);
        monthlyFinancesSummary.setNumCourses(numCourses);
        monthlyFinancesSummary.setNumMembers(numMembers);
        monthlyFinancesSummary.setTotalChargeAmount(totalChargeAmount);
        monthlyFinancesSummary.setTotalPaymentAmount(totalPaymentAmount);
        return monthlyFinancesSummary;
    }

    public MonthlyFinancesSummaryBuilder setMonthlyFinancesSummaryID(int monthlyFinancesSummaryID){
        this.monthlyFinancesSummaryID = monthlyFinancesSummaryID;
        return this;
    }

    public MonthlyFinancesSummaryBuilder setCycleStartDate(LocalDate cycleStartDate){
        this.cycleStartDate = cycleStartDate;
        return this;
    }

    public MonthlyFinancesSummaryBuilder setSeason(Season season){
        this.season = season;
        return this;
    }

    public MonthlyFinancesSummaryBuilder setNumCourses(int numCourses){
        this.numCourses = numCourses;
        return this;
    }

    public MonthlyFinancesSummaryBuilder setNumTotalCharges(int numTotalCharges){
        this.numTotalCharges = numTotalCharges;
        return this;
    }

    public MonthlyFinancesSummaryBuilder setNumMembers(int numMembers){
        this.numMembers = numMembers;
        return this;
    }

    public MonthlyFinancesSummaryBuilder setTotalChargeAmount(BigDecimal totalChargeAmount){
        this.totalChargeAmount = totalChargeAmount;
        return this;
    }

    public MonthlyFinancesSummaryBuilder setTotalPaymentAmount(BigDecimal totalPaymentAmount){
        this.totalPaymentAmount = totalPaymentAmount;
        return this;
    }
}
