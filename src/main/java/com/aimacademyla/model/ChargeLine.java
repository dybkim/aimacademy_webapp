package com.aimacademyla.model;

import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by davidkim on 3/21/17.
 */

@Entity(name="Charge_Line")
public class ChargeLine implements Serializable{

    private static final long serialVersionUID = 6493974523582584972L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ChargeLineID")
    private int chargeLineID;

    @Column(name="AttendanceID")
    private int attendanceID;

    @JoinColumn(name="ChargeID")
    private int chargeID;

    @Column(name="BillableUnitsBilled")
    private BigDecimal billableUnitsBilled;

    @Column(name="TotalCharge")
    @NumberFormat(style= NumberFormat.Style.CURRENCY)
    private BigDecimal totalCharge;

    public int getChargeLineID() {
        return chargeLineID;
    }

    public void setChargeLineID(int chargeLineID) {
        this.chargeLineID = chargeLineID;
    }

    public int getAttendanceID() {
        return attendanceID;
    }

    public void setAttendanceID(int attendanceID) {
        this.attendanceID = attendanceID;
    }

    public int getChargeID() {
        return chargeID;
    }

    public void setChargeID(int chargeID) {
        this.chargeID = chargeID;
    }

    public BigDecimal getTotalCharge() {
        return totalCharge;
    }

    public void setTotalCharge(BigDecimal totalCharge) {
        this.totalCharge = totalCharge;
    }

    public BigDecimal getBillableUnitsBilled() {
        return billableUnitsBilled;
    }

    public void setBillableUnitsBilled(BigDecimal billableUnitsBilled) {
        this.billableUnitsBilled = billableUnitsBilled;
    }
}
