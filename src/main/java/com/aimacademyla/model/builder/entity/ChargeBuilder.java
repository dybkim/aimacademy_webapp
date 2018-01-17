package com.aimacademyla.model.builder.entity;

import com.aimacademyla.dao.MonthlyFinancesSummaryDAO;
import com.aimacademyla.dao.flow.impl.MonthlyFinancesSummaryDAOAccessFlow;
import com.aimacademyla.model.*;
import com.aimacademyla.model.builder.GenericBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ChargeBuilder implements GenericBuilder<Charge>{

    private int chargeID;
    private Member member;
    private Course course;
    private LocalDate cycleStartDate;
    private String description;
    private String billableUnitsType;
    private int numChargeLines;
    private BigDecimal chargeAmount;
    private BigDecimal discountAmount;
    private BigDecimal billableUnitsBilled;
    private MonthlyFinancesSummary monthlyFinancesSummary;
    private Payment payment;

    @Override
    public Charge build() {
        Charge charge = new Charge();
        charge.setChargeID(chargeID);
        charge.setMember(member);
        charge.setCourse(course);
        charge.setCycleStartDate(cycleStartDate);
        charge.setChargeAmount(chargeAmount);
        charge.setDiscountAmount(discountAmount);
        charge.setMonthlyFinancesSummary(monthlyFinancesSummary);
        charge.setBillableUnitType(course.getBillableUnitType());
        charge.setBillableUnitsBilled(billableUnitsBilled);
        charge.setDescription(description);
        charge.setMonthlyFinancesSummary(monthlyFinancesSummary);
        charge.setNumChargeLines(numChargeLines);
        charge.setBillableUnitType(billableUnitsType);
        charge.setPayment(payment);
        return charge;
    }

    public ChargeBuilder setChargeID(int chargeID){
        this.chargeID = chargeID;
        return this;
    }
    public ChargeBuilder setMember(Member member){
        this.member = member;
        return this;
    }

    public ChargeBuilder setCourse(Course course){
        this.course = course;
        return this;
    }

    public ChargeBuilder setCycleStartDate(LocalDate cycleStartDate){
        this.cycleStartDate = cycleStartDate;
        return this;
    }

    public ChargeBuilder setChargeAmount(BigDecimal chargeAmount){
        this.chargeAmount = chargeAmount;
         return this;
    }

    public ChargeBuilder setDiscountAmount(BigDecimal discountAmount){
        this.discountAmount = discountAmount;
        return this;
    }

    public ChargeBuilder setBillableUnitsBilled(BigDecimal billableUnitsBilled){
        this.billableUnitsBilled = billableUnitsBilled;
        return this;
    }

    public ChargeBuilder setDescription(String chargeDescription){
        this.description = chargeDescription;
        return this;
    }

    public ChargeBuilder setMonthlyFinancesSummary(MonthlyFinancesSummary monthlyFinancesSummary){
        this.monthlyFinancesSummary = monthlyFinancesSummary;
        return this;
    }

    public ChargeBuilder setNumChargeLines(int numChargeLines){
        this.numChargeLines = numChargeLines;
        return this;
    }

    public ChargeBuilder setBillableUnitsType(String billableUnitsType){
        this.billableUnitsType = billableUnitsType;
        return this;
    }

    public ChargeBuilder setPayment(Payment payment){
        this.payment = payment;
        return this;
    }
}
