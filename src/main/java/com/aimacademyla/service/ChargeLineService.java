package com.aimacademyla.service;

import com.aimacademyla.model.ChargeLine;

import java.util.List;

/**
 * Created by davidkim on 4/6/17.
 */
public interface ChargeLineService extends GenericService<ChargeLine, Integer>{
    void addChargeLine(ChargeLine chargeLine);
    void updateChargeLine(ChargeLine chargeLine);
    void removeChargeLine(ChargeLine chargeLine);
}
