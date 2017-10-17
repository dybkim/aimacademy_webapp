package com.aimacademyla.service.impl;

import com.aimacademyla.dao.ChargeDAO;
import com.aimacademyla.dao.ChargeLineDAO;
import com.aimacademyla.dao.GenericDAO;
import com.aimacademyla.model.AIMEntityType;
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

        BigDecimal hoursBilled = charge.getHoursBilled();
        if(hoursBilled == null)
            hoursBilled = BigDecimal.ZERO;

        hoursBilled = hoursBilled.add(course.getClassDuration());
        charge.setHoursBilled(hoursBilled);

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

        BigDecimal hoursBilled = charge.getHoursBilled();
        if(hoursBilled == null)
            hoursBilled = BigDecimal.ZERO;
        BigDecimal oldHoursBilled = chargeLine.getHoursBilled();
        if(oldHoursBilled == null)
            oldHoursBilled = BigDecimal.ZERO;

        hoursBilled = hoursBilled.subtract(oldHoursBilled);
        hoursBilled = hoursBilled.add(course.getClassDuration());
        charge.setHoursBilled(hoursBilled);

        if(hoursBilled.equals(BigDecimal.ZERO))
            charge.setHoursBilled(BigDecimal.ZERO);

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

        BigDecimal hoursBilled = charge.getHoursBilled();
        if(hoursBilled == null)
            hoursBilled = BigDecimal.ZERO;

        BigDecimal chargeLineHoursBilled = chargeLine.getHoursBilled();
        if(chargeLineHoursBilled == null)
            chargeLineHoursBilled = BigDecimal.ZERO;

        hoursBilled = hoursBilled.subtract(chargeLineHoursBilled);
        charge.setHoursBilled(hoursBilled);

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
