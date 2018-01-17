package com.aimacademyla.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Created by davidkim on 3/21/17.
 */

@Entity(name="Charge_Line")
public class ChargeLine extends AIMEntity implements Serializable{

    private static final long serialVersionUID = 6493974523582584972L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ChargeLineID")
    private int chargeLineID;

    @ManyToOne
    @JoinColumn(name="ChargeID", referencedColumnName = "ChargeID")
    @JsonBackReference
    private Charge charge;

    @OneToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, mappedBy = "chargeLine")
    @NotFound(action= NotFoundAction.IGNORE)
    @JsonManagedReference
    private Attendance attendance;

    @Column(name="BillableUnitsBilled")
    private BigDecimal billableUnitsBilled;

    @Column(name="ChargeAmount")
    @NumberFormat(style= NumberFormat.Style.CURRENCY)
    private BigDecimal chargeAmount;

    @Column(name="dateCharged")
    @DateTimeFormat(pattern="MM/dd/yyyy")
    private LocalDate dateCharged;

    /*
     * Have to override equals in order to implement a Set of ChargeLines
     */
    @Override
    public boolean equals(Object object){
        if(this == object)
            return true;

        if(!(object instanceof ChargeLine))
            return false;

        ChargeLine chargeLine = (ChargeLine) object;
        return chargeLineID == chargeLine.getChargeLineID();
    }

    @Override
    public int getBusinessID() {
        return chargeLineID;
    }

    @Override
    public void setBusinessID(int chargeLineID){this.chargeLineID = chargeLineID;}

    public int getChargeLineID() {
        return chargeLineID;
    }

    public void setChargeLineID(int chargeLineID) {
        this.chargeLineID = chargeLineID;
    }

    public void setAttendance(Attendance attendance){
        this.attendance = attendance;
    }

    public Attendance getAttendance() {
        return attendance;
    }

    public Charge getCharge() {
        return charge;
    }

    public void setCharge(Charge charge) {
        this.charge = charge;
    }

    public BigDecimal getChargeAmount() {
        return chargeAmount;
    }

    public void setChargeAmount(BigDecimal chargeAmount) {
        this.chargeAmount = chargeAmount;
    }

    public BigDecimal getBillableUnitsBilled() {
        return billableUnitsBilled;
    }

    public void setBillableUnitsBilled(BigDecimal billableUnitsBilled) {
        this.billableUnitsBilled = billableUnitsBilled;
    }

    public LocalDate getDateCharged() {
        return dateCharged;
    }

    public void setDateCharged(LocalDate dateCharged) {
        this.dateCharged = dateCharged;
    }
}
