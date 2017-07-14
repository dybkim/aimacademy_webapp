package com.aimacademyla.dao;

import com.aimacademyla.model.ChargeLine;

/**
 * ChargeLineDAO interface for accessing Charge_Line entity instances in the RDB
 *
 * Created by davidkim on 3/21/17.
 */
public interface ChargeLineDAO extends GenericDAO<ChargeLine,Integer>{
    ChargeLine getChargeLineByID(int chargeLineID);
    ChargeLine getChargeLineByAttendanceID(int attendanceID);
}
