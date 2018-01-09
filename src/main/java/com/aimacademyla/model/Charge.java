package com.aimacademyla.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

/**
 * Charge Entity represents the sum of related ChargeLine entities
 *
 * Created by davidkim on 3/9/17.
 */

@Entity(name="Charges")
public class Charge implements Serializable{

    private static final long serialVersionUID = 1346555619431916040L;
    private static final Logger logger = LogManager.getLogger(Charge.class.getName());

    public Charge(){
        chargeLineSet = new HashSet<>();
    }

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="ChargeID")
    private int chargeID;

    @ManyToOne
    @JoinColumn(name="MemberID")
    private Member member;

    @ManyToOne
    @JoinColumn(name="CourseID")
    private Course course;

    @Column(name="ChargeAmount")
    @NumberFormat(style= NumberFormat.Style.CURRENCY)
//    @Pattern(regexp="^(0|[1-9][0-9]*)$", message="Charge amount must be numeric")
    private BigDecimal chargeAmount;

    @Column(name="CycleStartDate")
    @DateTimeFormat(pattern="MM/dd/yyyy")
    private LocalDate cycleStartDate;

    @ManyToOne
    @JoinColumn(name="PaymentID")
    private Payment payment;

    @ManyToOne
    @JoinColumn(name="MonthlyFinancesSummaryID")
    private MonthlyFinancesSummary monthlyFinancesSummary;

    @Column(name="Description")
    @Length(max=30)
    private String description;

    @Column(name="DiscountAmount")
    @NumberFormat(style= NumberFormat.Style.CURRENCY)
    private BigDecimal discountAmount;

    @Column(name="BillableUnitsBilled")
    private BigDecimal billableUnitsBilled;

    @Column(name="BillableUnitType")
    @Length(max=10)
    private String billableUnitType;

    @Column(name="NumChargeLines")
    private int numChargeLines;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "charge", orphanRemoval = true)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonManagedReference("chargeLine")
    private Set<ChargeLine> chargeLineSet;

    private ChargeLine getChargeLine(int chargeLineID){
        for(ChargeLine chargeLine : chargeLineSet)
            if(chargeLine.getChargeLineID() == chargeLineID)
                return chargeLine;

        return null;
    }

    private void removeChargeLine(int chargeLineID){
        Iterator it = chargeLineSet.iterator();
        while(it.hasNext()){
            ChargeLine chargeLine = (ChargeLine) it.next();
            if(chargeLine.getChargeLineID() == chargeLineID) {
                it.remove();
                return;
            }
        }
    }

    public void addChargeLine(ChargeLine chargeLine){
        if(chargeLineSet == null)
            return;

        logger.debug("Adding ChargeLine, current chargeAmount is: " + chargeAmount + ", number of ChargeLines is: "+ chargeLineSet.size());
        BigDecimal chargeLineAmount = chargeLine.getChargeAmount();
        chargeAmount = chargeAmount.add(chargeLineAmount);

        if(billableUnitsBilled == null)
            billableUnitsBilled = BigDecimal.ZERO;

        billableUnitsBilled = billableUnitsBilled.add(course.getBillableUnitDuration());

        numChargeLines++;

        this.chargeLineSet.add(chargeLine);
        chargeLine.setCharge(this);

        logger.debug("Added ChargeLine, current chargeAmount is: " + chargeAmount + ", number of ChargeLines is: "+ chargeLineSet.size());
    }

    public void updateChargeLine(ChargeLine chargeLine){
        if(chargeLineSet == null)
            return;

        logger.debug("Updating ChargeLine, current chargeAmount is: " + chargeAmount + ", number of ChargeLines is: "+ chargeLineSet.size());
        removeChargeLine(chargeLine);
        addChargeLine(chargeLine);
        logger.debug("Updated ChargeLine, current chargeAmount is: " + chargeAmount + ", number of ChargeLines is: "+ chargeLineSet.size());
    }

    public void removeChargeLine(ChargeLine chargeLine){
        if(chargeLineSet == null || getChargeLine(chargeLine.getChargeLineID()) == null)
            return;


        logger.debug("Removing ChargeLine, current chargeAmount is: " + chargeAmount + ", number of ChargeLines is: "+ chargeLineSet.size());
        chargeAmount = chargeAmount.subtract(chargeLine.getChargeAmount());

        if(billableUnitsBilled == null)
            billableUnitsBilled = BigDecimal.ZERO;

        BigDecimal chargeLineBillableUnitsBilled = chargeLine.getBillableUnitsBilled();
        if(chargeLineBillableUnitsBilled == null)
            chargeLineBillableUnitsBilled = BigDecimal.ZERO;

        billableUnitsBilled = billableUnitsBilled.subtract(chargeLineBillableUnitsBilled);

        numChargeLines--;

        removeChargeLine(chargeLine.getChargeLineID());

        logger.debug("Removed ChargeLine, current chargeAmount is: " + chargeAmount + ", number of ChargeLines is: "+ chargeLineSet.size());
    }

    public BigDecimal calculateChargeAmount(){
        if(chargeLineSet == null)
            return BigDecimal.ZERO;

        BigDecimal chargeAmount = BigDecimal.ZERO;

        for(ChargeLine chargeLine : chargeLineSet)
            chargeAmount = chargeAmount.add(chargeLine.getChargeAmount());

        return chargeAmount;
    }

    public int getChargeID() {
        return chargeID;
    }

    public void setChargeID(int chargeID) {
        this.chargeID = chargeID;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public BigDecimal getChargeAmount() {
        return chargeAmount;
    }

    public void setChargeAmount(BigDecimal chargeAmount) {
        this.chargeAmount = chargeAmount;
    }

    public LocalDate getCycleStartDate() {
        return cycleStartDate;
    }

    public void setCycleStartDate(LocalDate cycleStartDate) {
        this.cycleStartDate = cycleStartDate;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public MonthlyFinancesSummary getMonthlyFinancesSummary() {
        return monthlyFinancesSummary;
    }

    public void setMonthlyFinancesSummary(MonthlyFinancesSummary monthlyFinancesSummary) {
        this.monthlyFinancesSummary = monthlyFinancesSummary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getBillableUnitsBilled() {
        return billableUnitsBilled;
    }

    public void setBillableUnitsBilled(BigDecimal billableUnitsBilled) {
        this.billableUnitsBilled = billableUnitsBilled;
    }

    public String getBillableUnitType() {
        return billableUnitType;
    }

    public void setBillableUnitType(String billableUnitType) {
        this.billableUnitType = billableUnitType;
    }

    public Set<ChargeLine> getChargeLineSet() {
        return chargeLineSet;
    }

    public void setChargeLineSet(Set<ChargeLine> chargeLineSet) {
        this.chargeLineSet = chargeLineSet;
    }

    public int getNumChargeLines() {
        return numChargeLines;
    }

    public void setNumChargeLines(int numChargeLines) {
        this.numChargeLines = numChargeLines;
    }

    @Override
    public boolean equals(Object object){
        if(!(object instanceof Charge))
            throw new IllegalArgumentException("Argument must be of type Charge!");

        Charge charge = (Charge) object;
        return charge.getChargeID() == this.chargeID;
    }

}
