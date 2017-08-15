package com.aimacademyla.service;

import com.aimacademyla.model.Charge;
import com.aimacademyla.model.ChargeLine;

import java.util.List;

/**
 * Created by davidkim on 4/6/17.
 */
public interface ChargeLineService extends GenericService<ChargeLine, Integer>{
    ChargeLine getChargeLineByAttendanceID(int attendanceID);
    List<ChargeLine> getChargeLinesByCharge(Charge charge);
}
