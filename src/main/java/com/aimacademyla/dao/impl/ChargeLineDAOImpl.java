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
public class ChargeLineDAOImpl implements ChargeLineDAO {

    private SessionFactory sessionFactory;

    @Autowired
    public ChargeLineDAOImpl(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    @Override
    public ChargeLine getChargeLineByID(int chargeLineID) {
        Session session = sessionFactory.getCurrentSession();
        ChargeLine chargeLine = session.get(ChargeLine.class, chargeLineID);
        session.flush();
        return chargeLine;
    }

    @Override
    public ChargeLine getChargeLineByAttendanceID(int attendanceID) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("FROM Charge_Line WHERE AttendanceID = :attendanceID");
        query.setParameter("attendanceID", attendanceID);
        ChargeLine chargeLine = (ChargeLine) query.uniqueResult();
        session.flush();

        return chargeLine;
    }

    @Override
    public void addChargeLine(ChargeLine chargeLine) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(chargeLine);
        session.flush();
    }

    @Override
    public void editChargeLine(ChargeLine chargeLine) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(chargeLine);
        session.flush();
    }

    @Override
    public void deleteChargeLine(ChargeLine chargeLine) {
        Session session = sessionFactory.getCurrentSession();
        session.remove(chargeLine);
        session.flush();
    }
}
