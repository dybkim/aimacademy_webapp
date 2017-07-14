package com.aimacademyla.dao.impl;

import com.aimacademyla.dao.ChargeLineDAO;
import com.aimacademyla.model.ChargeLine;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by davidkim on 4/6/17.
 */

@Repository("chargeLineDAO")
@Transactional
public class ChargeLineDAOImpl extends GenericDAOImpl<ChargeLine, Integer> implements ChargeLineDAO {

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
        Query query = session.createQuery("FROM Charge_Line WHERE AttendanceID = :attendanceID");
        query.setParameter("attendanceID", attendanceID);
        ChargeLine chargeLine = (ChargeLine) query.uniqueResult();
        session.flush();

        return chargeLine;
    }
}
