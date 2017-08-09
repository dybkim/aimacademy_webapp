package com.aimacademyla.service.impl;

import com.aimacademyla.dao.ChargeDAO;
import com.aimacademyla.dao.ChargeLineDAO;
import com.aimacademyla.dao.GenericDAO;
import com.aimacademyla.model.Charge;
import com.aimacademyla.model.ChargeLine;
import com.aimacademyla.service.ChargeLineService;
import com.aimacademyla.service.ChargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Created by davidkim on 4/6/17.
 */

@Service
public class ChargeLineServiceImpl extends GenericServiceImpl<ChargeLine,Integer> implements ChargeLineService{

    private ChargeLineDAO chargeLineDAO;
    private ChargeService chargeService;

    @Autowired
    public ChargeLineServiceImpl(@Qualifier("chargeLineDAO") GenericDAO<ChargeLine, Integer> genericDAO, ChargeService chargeService){
        super(genericDAO);
        this.chargeLineDAO = (ChargeLineDAO) genericDAO;
        this.chargeService = chargeService;
    }

    @Override
    public void add(ChargeLine chargeLine){
        double chargeLineAmount = chargeLine.getTotalCharge();
        Charge charge = chargeService.get(chargeLine.getChargeID());
        double chargeAmount = charge.getChargeAmount() + chargeLineAmount;
        charge.setChargeAmount(chargeAmount);
        chargeService.update(charge);

        chargeLineDAO.add(chargeLine);
    }

    @Override
    public void update(ChargeLine chargeLine){
        ChargeLine oldChargeLine = chargeLineDAO.get(chargeLine.getChargeLineID());
        double oldChargeLineAmount = oldChargeLine.getTotalCharge();

        Charge charge = chargeService.get(chargeLine.getChargeID());
        double chargeAmount = charge.getChargeAmount() - oldChargeLineAmount + chargeLine.getTotalCharge();
        charge.setChargeAmount(chargeAmount);
        chargeService.update(charge);

        chargeLineDAO.update(chargeLine);
    }

    @Override
    public void remove(ChargeLine chargeLine){
        double chargeLineAmount = chargeLineDAO.get(chargeLine.getChargeID()).getTotalCharge();
        Charge charge = chargeService.get(chargeLine.getChargeID());
        double newChargeAmount = charge.getChargeAmount() - chargeLineAmount;
        charge.setChargeAmount(newChargeAmount);
        chargeService.update(charge);

        chargeLineDAO.remove(chargeLine);
    }

    @Override
    public ChargeLine getChargeLineByAttendanceID(int attendanceID) {
        return chargeLineDAO.getChargeLineByAttendanceID(attendanceID);
    }
}
