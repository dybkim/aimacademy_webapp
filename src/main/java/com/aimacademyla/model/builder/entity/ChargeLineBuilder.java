package com.aimacademyla.model.builder.entity;

import com.aimacademyla.model.Attendance;
import com.aimacademyla.model.Charge;
import com.aimacademyla.model.ChargeLine;
import com.aimacademyla.model.builder.GenericBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ChargeLineBuilder implements GenericBuilder<ChargeLine> {

    private Attendance attendance;
    private LocalDate dateCharged;
    private BigDecimal chargeAmount;
    private Charge charge;
    private BigDecimal billableUnitsBilled;

    @Override
    public ChargeLine build() {
        ChargeLine chargeLine = new ChargeLine();
        chargeLine.setAttendance(attendance);
        chargeLine.setChargeAmount(chargeAmount);
        chargeLine.setCharge(charge);
        chargeLine.setBillableUnitsBilled(billableUnitsBilled);
        chargeLine.setDateCharged(dateCharged);
        return chargeLine;
    }

    public ChargeLineBuilder setAttendance(Attendance attendance) {
        this.attendance = attendance;
        return this;
    }

    public ChargeLineBuilder setChargeAmount(BigDecimal chargeAmount) {
        this.chargeAmount = chargeAmount;
        return this;
    }

    public ChargeLineBuilder setCharge(Charge charge) {
        this.charge = charge;
        return this;
    }

    public ChargeLineBuilder setBillableUnitsBilled(BigDecimal billableUnitsBilled){
        this.billableUnitsBilled = billableUnitsBilled;
        return this;
    }

    public ChargeLineBuilder setDateCharged(LocalDate dateCharged){
        this.dateCharged = dateCharged;
        return this;
    }
}