package com.aimacademyla.dao.impl;

import com.aimacademyla.dao.ChargeLineDAO;
import com.aimacademyla.model.Charge;
import com.aimacademyla.model.ChargeLine;
import com.aimacademyla.model.enums.AIMEntityType;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by davidkim on 4/6/17.
 */

@Repository("chargeLineDAO")
@Transactional
public class ChargeLineDAOImpl extends GenericDAOImpl<ChargeLine, Integer> implements ChargeLineDAO {

    private final AIMEntityType AIM_ENTITY_TYPE = AIMEntityType.CHARGE_LINE;

    public ChargeLineDAOImpl(){
        super(ChargeLine.class);
    }

    @Override
    public ChargeLine getChargeLineByID(int chargeLineID) {
        Session session = currentSession();
        ChargeLine chargeLine = session.get(ChargeLine.class, chargeLineID);
        session.flush();
        return chargeLine;
    }

    @Override
    public ChargeLine getChargeLineByAttendanceID(int attendanceID) {
        Session session = currentSession();
        Query query = session.createQuery("FROM Charge_Line WHERE attendanceID = :attendanceID");
        query.setParameter("attendanceID", attendanceID);
        ChargeLine chargeLine = (ChargeLine) query.uniqueResult();
        session.flush();

        return chargeLine;
    }

    @Override
    public List<ChargeLine> getChargeLinesByCharge(Charge charge){
        Session session = currentSession();
        Query query = session.createQuery("FROM Charge_Line WHERE chargeID = :chargeID");
        query.setParameter("chargeID", charge.getChargeID());
        List<ChargeLine> chargeLineList = query.getResultList();
        session.flush();

        return chargeLineList;
    }

    @Override
    public AIMEntityType getAIMEntityType() {
        return AIM_ENTITY_TYPE;
    }
}
