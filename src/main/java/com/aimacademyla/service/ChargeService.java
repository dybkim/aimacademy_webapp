package com.aimacademyla.service;

import com.aimacademyla.model.Charge;
import com.aimacademyla.model.Member;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by davidkim on 4/10/17.
 */
public interface ChargeService extends GenericService<Charge, Integer>{
    List<Charge> getList(Member member, LocalDate date);

    /*
     * getTransientChargeList fetches Charges in a certain LocalDate range, but does not persist the Charges that are
     * fetch, nor does it persist any associated entities for the transient Charges
     */
    List<Charge> getTransientChargeList(Member member, LocalDate cycleStartDate, LocalDate cycleEndDate);
    void addCharge(Charge charge);
    void updateCharge(Charge charge);
    void removeCharge(Charge charge);
}
