package com.aimacademyla.service.impl;

import com.aimacademyla.dao.ChargeLineDAO;
import com.aimacademyla.model.ChargeLine;
import com.aimacademyla.service.ChargeLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by davidkim on 4/6/17.
 */

@Service
public class ChargeLineServiceImpl implements ChargeLineService{

    private ChargeLineDAO chargeLineDAO;

    @Autowired
    public ChargeLineServiceImpl(ChargeLineDAO chargeLineDAO){
        this.chargeLineDAO = chargeLineDAO;
    }

    @Override
    public ChargeLine getChargeLineByID(int chargeLineID) {
        return chargeLineDAO.getChargeLineByID(chargeLineID);
    }

    @Override
    public ChargeLine getChargeLineByAttendanceID(int attendanceID) {
        return chargeLineDAO.getChargeLineByAttendanceID(attendanceID);
    }

    @Override
    public void addChargeLine(ChargeLine chargeLine) {
        chargeLineDAO.addChargeLine(chargeLine);
    }

    @Override
    public void editChargeLine(ChargeLine chargeLine) {
        chargeLineDAO.editChargeLine(chargeLine);
    }

    @Override
    public void deleteChargeLine(ChargeLine chargeLine) {
        chargeLineDAO.deleteChargeLine(chargeLine);
    }
}
