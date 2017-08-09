package com.aimacademyla.model;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

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

    @Column(name="ChargeID")
    private int chargeID;

    @Column(name="TotalCharge")
    @Pattern(regexp="^(0|[1-9][0-9]*)$", message="Charge amount must be numeric")
    private double totalCharge;

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

    public double getTotalCharge() {
        return totalCharge;
    }

    public void setTotalCharge(double totalCharge) {
        this.totalCharge = totalCharge;
    }
}
