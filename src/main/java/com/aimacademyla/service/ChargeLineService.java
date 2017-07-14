package com.aimacademyla.service;

import com.aimacademyla.model.ChargeLine;

/**
 * Created by davidkim on 4/6/17.
 */
public interface ChargeLineService extends GenericService<ChargeLine, Integer>{
    ChargeLine getChargeLineByAttendanceID(int attendanceID);
}
