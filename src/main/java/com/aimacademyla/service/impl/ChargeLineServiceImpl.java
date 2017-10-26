package com.aimacademyla.service.impl;

import com.aimacademyla.dao.ChargeLineDAO;
import com.aimacademyla.dao.GenericDAO;
import com.aimacademyla.model.enums.AIMEntityType;
import com.aimacademyla.model.Charge;
import com.aimacademyla.model.ChargeLine;
import com.aimacademyla.model.Course;
import com.aimacademyla.service.ChargeLineService;
import com.aimacademyla.service.ChargeService;
import com.aimacademyla.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by davidkim on 4/6/17.
 *
 * NOTE: Modifying chargeLine instances also modifies the associated charge instances, specifically the totalAmount, numTotalChargeLines, and hoursBilled
 */

@Service
public class ChargeLineServiceImpl extends GenericServiceImpl<ChargeLine,Integer> implements ChargeLineService{

    private ChargeLineDAO chargeLineDAO;
    private ChargeService chargeService;
    private CourseService courseService;

    private final AIMEntityType AIM_ENTITY_TYPE = AIMEntityType.CHARGE_LINE;

    @Autowired
    public ChargeLineServiceImpl(@Qualifier("chargeLineDAO") GenericDAO<ChargeLine, Integer> genericDAO, ChargeService chargeService, CourseService courseService){
        super(genericDAO);
        this.chargeLineDAO = (ChargeLineDAO) genericDAO;
        this.chargeService = chargeService;
        this.courseService = courseService;
    }

    @Override
    public void add(ChargeLine chargeLine){
        BigDecimal chargeLineAmount = chargeLine.getTotalCharge();
        Charge charge = chargeService.get(chargeLine.getChargeID());
        Course course = courseService.get(charge.getCourseID());

        BigDecimal chargeAmount = charge.getChargeAmount().add(chargeLineAmount);
        charge.setChargeAmount(chargeAmount);

        BigDecimal billableUnitsBilled = charge.getBillableUnitsBilled();

        if(billableUnitsBilled == null)
            billableUnitsBilled = BigDecimal.ZERO;

        billableUnitsBilled = billableUnitsBilled.add(course.getBillableUnitDuration());
        charge.setBillableUnitsBilled(billableUnitsBilled);

        int numChargeLines = charge.getNumChargeLines() + 1;
        charge.setNumChargeLines(numChargeLines);
        chargeService.update(charge);

        chargeLineDAO.add(chargeLine);
    }

    @Override
    public void update(ChargeLine chargeLine){
        ChargeLine oldChargeLine = chargeLineDAO.get(chargeLine.getChargeLineID());
        BigDecimal oldChargeLineAmount = oldChargeLine.getTotalCharge();
        Charge charge = chargeService.get(chargeLine.getChargeID());
        Course course = courseService.get(charge.getCourseID());

        BigDecimal billableUnitsBilled = charge.getBillableUnitsBilled();
        if(billableUnitsBilled == null)
            billableUnitsBilled = BigDecimal.ZERO;
        BigDecimal oldHoursBilled = chargeLine.getBillableUnitsBilled();
        if(oldHoursBilled == null)
            oldHoursBilled = BigDecimal.ZERO;

        billableUnitsBilled = billableUnitsBilled.subtract(oldHoursBilled);
        billableUnitsBilled = billableUnitsBilled.add(course.getBillableUnitDuration());
        charge.setBillableUnitsBilled(billableUnitsBilled);

        if(billableUnitsBilled.equals(BigDecimal.ZERO))
            charge.setBillableUnitsBilled(BigDecimal.ZERO);

        BigDecimal chargeAmount = charge.getChargeAmount().subtract(oldChargeLineAmount).add(chargeLine.getTotalCharge());
        charge.setChargeAmount(chargeAmount);
        chargeService.update(charge);

        chargeLineDAO.update(chargeLine);
    }

    @Override
    public void remove(ChargeLine chargeLine){
        BigDecimal chargeLineAmount = chargeLineDAO.get(chargeLine.getChargeLineID()).getTotalCharge();
        Charge charge = chargeService.get(chargeLine.getChargeID());

        BigDecimal newChargeAmount = charge.getChargeAmount().subtract(chargeLineAmount);
        charge.setChargeAmount(newChargeAmount);

        BigDecimal billableUnitsBilled = charge.getBillableUnitsBilled();
        if(billableUnitsBilled == null)
            billableUnitsBilled = BigDecimal.ZERO;

        BigDecimal chargeLineBillableUnitsBilled = chargeLine.getBillableUnitsBilled();
        if(chargeLineBillableUnitsBilled == null)
            chargeLineBillableUnitsBilled = BigDecimal.ZERO;

        billableUnitsBilled = billableUnitsBilled.subtract(chargeLineBillableUnitsBilled);
        charge.setBillableUnitsBilled(billableUnitsBilled);

        int numChargeLines = charge.getNumChargeLines() - 1;
        charge.setNumChargeLines(numChargeLines);

        if(numChargeLines > 0){
            chargeService.update(charge);
            chargeLineDAO.remove(chargeLine);
        }

        /*
         * chargeLine entities are deleted when charges are deleted due to cascading settings
         */
        else
            chargeService.remove(charge);

    }

    @Override
    public void remove(List<ChargeLine> chargeLineList){
        for(ChargeLine chargeLine : chargeLineList)
            remove(chargeLine);
    }

    @Override
    public ChargeLine getChargeLineByAttendanceID(int attendanceID) {
        return chargeLineDAO.getChargeLineByAttendanceID(attendanceID);
    }

    @Override
    public List<ChargeLine> getChargeLinesByCharge(Charge charge){
        List<ChargeLine> chargeLineList = chargeLineDAO.getChargeLinesByCharge(charge);

        if(chargeLineList == null)
            chargeLineList = new ArrayList<>();

        return chargeLineList;
    }

    @Override
    public AIMEntityType getAIMEntityType(){
        return AIM_ENTITY_TYPE;
    }
}
