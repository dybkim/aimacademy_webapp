package com.aimacademyla.dao;

import com.aimacademyla.model.Charge;
import com.aimacademyla.model.ChargeLine;

import java.util.List;

/**
 * ChargeLineDAO interface for accessing Charge_Line entity instances in the RDB
 *
 * Created by davidkim on 3/21/17.
 */
public interface ChargeLineDAO extends GenericDAO<ChargeLine,Integer>{
    ChargeLine getChargeLineByID(int chargeLineID);
    ChargeLine getChargeLineByAttendanceID(int attendanceID);
    List<ChargeLine> getChargeLinesByCharge(Charge charge);
}
