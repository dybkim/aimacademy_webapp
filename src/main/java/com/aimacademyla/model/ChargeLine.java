package com.aimacademyla.model;

import javax.persistence.*;
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
    private Integer chargeID;

    @Column(name="TotalCharge")
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

    public Integer getChargeID() {
        return chargeID;
    }

    public void setChargeID(Integer chargeID) {
        this.chargeID = chargeID;
    }

    public double getTotalCharge() {
        return totalCharge;
    }

    public void setTotalCharge(double totalCharge) {
        this.totalCharge = totalCharge;
    }
}
