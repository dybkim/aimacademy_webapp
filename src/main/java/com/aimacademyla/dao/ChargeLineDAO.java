package com.aimacademyla.dao;

import com.aimacademyla.model.ChargeLine;

/**
 * Created by davidkim on 3/21/17.
 */
public interface ChargeLineDAO {
    ChargeLine getChargeLineByID(int chargeLineID);
    ChargeLine getChargeLineByAttendanceID(int attendanceID);
    void addChargeLine(ChargeLine chargeLine);
    void editChargeLine(ChargeLine chargeLine);
    void deleteChargeLine(ChargeLine chargeLine);
}
