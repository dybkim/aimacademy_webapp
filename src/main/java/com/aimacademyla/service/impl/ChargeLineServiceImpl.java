package com.aimacademyla.service.impl;

import com.aimacademyla.dao.ChargeDAO;
import com.aimacademyla.dao.ChargeLineDAO;
import com.aimacademyla.dao.GenericDAO;
import com.aimacademyla.model.Charge;
import com.aimacademyla.model.ChargeLine;
import com.aimacademyla.service.ChargeLineService;
import com.aimacademyla.service.ChargeService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Created by davidkim on 4/6/17.
 *
 * NOTE: Modifying chargeLine instances also modifies the associated charge instances, specifically the totalAmount, numTotalChargeLines, and hoursBilled
 */

@Service
public class ChargeLineServiceImpl extends GenericServiceImpl<ChargeLine,Integer> implements ChargeLineService{

    private ChargeLineDAO chargeLineDAO;
    private ChargeService chargeService;
    private ChargeDAO chargeDAO;

    private static final Logger logger = LogManager.getLogger(ChargeLineServiceImpl.class.getName());

    @Autowired
    public ChargeLineServiceImpl(@Qualifier("chargeLineDAO") GenericDAO<ChargeLine, Integer> genericDAO,
                                 ChargeService chargeService,
                                 ChargeDAO chargeDAO){
        super(genericDAO);
        this.chargeLineDAO = (ChargeLineDAO) genericDAO;
        this.chargeService = chargeService;
        this.chargeDAO = chargeDAO;
    }

    @Override
    public void addChargeLine(ChargeLine chargeLine){
        Charge charge = chargeDAO.get(chargeLine.getCharge().getChargeID());
        charge = chargeDAO.loadCollections(charge);

        charge.addChargeLine(chargeLine);
        chargeService.updateCharge(charge);

        //Have to update chargeLine to cascade updates to associated Attendance entity
        chargeLineDAO.update(chargeLine);
    }

    @Override
    public void updateChargeLine(ChargeLine chargeLine){
        Charge charge = chargeService.get(chargeLine.getCharge().getChargeID());
        charge = chargeDAO.loadCollections(charge);

        charge.updateChargeLine(chargeLine);
        chargeService.updateCharge(charge);

        //Have to update chargeLine to cascade updates to associated Attendance entity
        chargeLineDAO.update(chargeLine);
    }

    @Override
    public void removeChargeLine(ChargeLine chargeLine){
        Charge charge = chargeService.get(chargeLine.getCharge().getChargeID());
        charge = chargeDAO.loadCollections(charge);

        charge.removeChargeLine(chargeLine);

        //Have to update chargeLine to cascade updates to associated Attendance entity
        chargeLineDAO.update(chargeLine);

        if(charge.getNumChargeLines() > 0){
            logger.debug("Updating Charge: " + charge.getChargeID() + " after removing chargeLine");
            chargeService.updateCharge(charge);
            return;
        }

        logger.debug("Removing Charge: " + charge.getChargeID());
        chargeService.removeCharge(charge);
    }
}
