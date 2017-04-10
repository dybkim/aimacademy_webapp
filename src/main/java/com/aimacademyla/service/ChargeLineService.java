package com.aimacademyla.service;

import com.aimacademyla.model.ChargeLine;

/**
 * Created by davidkim on 4/6/17.
 */
public interface ChargeLineService {
    ChargeLine getChargeLineByID(int chargeLineID);
    ChargeLine getChargeLineByAttendanceID(int attendanceID);
    void addChargeLine(ChargeLine chargeLine);
    void editChargeLine(ChargeLine chargeLine);
    void deleteChargeLine(ChargeLine chargeLine);
}
