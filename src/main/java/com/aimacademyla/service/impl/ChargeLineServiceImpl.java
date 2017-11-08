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
    private CourseService courseService;

    private final AIMEntityType AIM_ENTITY_TYPE = AIMEntityType.CHARGE_LINE;

    @Autowired
    public ChargeLineServiceImpl(@Qualifier("chargeLineDAO") GenericDAO<ChargeLine, Integer> genericDAO, CourseService courseService){
        super(genericDAO);
        this.chargeLineDAO = (ChargeLineDAO) genericDAO;
        this.courseService = courseService;
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
