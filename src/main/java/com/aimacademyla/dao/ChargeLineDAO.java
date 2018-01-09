package com.aimacademyla.dao;

import com.aimacademyla.model.Charge;
import com.aimacademyla.model.ChargeLine;
import com.aimacademyla.model.Member;

import java.time.LocalDate;
import java.util.List;

/**
 * ChargeLineDAO interface for accessing Charge_Line entity instances in the RDB
 *
 * Created by davidkim on 3/21/17.
 */
public interface ChargeLineDAO extends GenericDAO<ChargeLine,Integer>{

    List<ChargeLine> getList(Member member, LocalDate cycleStartDate, LocalDate cycleEndDate);
}
