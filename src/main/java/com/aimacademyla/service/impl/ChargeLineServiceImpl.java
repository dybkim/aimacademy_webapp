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

import java.math.BigDecimal;
import java.util.List;

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
        BigDecimal chargeLineAmount = chargeLine.getTotalCharge();
        Charge charge = chargeService.get(chargeLine.getChargeID());

        BigDecimal chargeAmount = charge.getChargeAmount().add(chargeLineAmount);
        charge.setChargeAmount(chargeAmount);

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

        int numChargeLines = charge.getNumChargeLines() - 1;
        charge.setNumChargeLines(numChargeLines);

        if(numChargeLines > 0)
            chargeService.update(charge);

        else
            chargeService.remove(charge);

        chargeLineDAO.remove(chargeLine);
    }

    @Override
    public ChargeLine getChargeLineByAttendanceID(int attendanceID) {
        return chargeLineDAO.getChargeLineByAttendanceID(attendanceID);
    }

    @Override
    public List<ChargeLine> getChargeLinesByCharge(Charge charge){
        return chargeLineDAO.getChargeLinesByCharge(charge);
    }
}
