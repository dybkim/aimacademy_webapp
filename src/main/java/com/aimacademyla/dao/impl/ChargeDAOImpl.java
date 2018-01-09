package com.aimacademyla.dao.impl;

import com.aimacademyla.dao.ChargeDAO;
import com.aimacademyla.model.Attendance;
import com.aimacademyla.model.Charge;
import com.aimacademyla.model.Course;
import com.aimacademyla.model.Member;
import com.aimacademyla.model.enums.AIMEntityType;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by davidkim on 4/10/17.
 */

@Repository("chargeDAO")
@Transactional
public class ChargeDAOImpl extends GenericDAOImpl<Charge,Integer> implements ChargeDAO {

    public ChargeDAOImpl(){
        super(Charge.class);
    }

    @Override
    public void removeList(List<Charge> chargeList){
        Session session = currentSession();
        List<Integer> chargeIDList = new ArrayList<>();
        for(Charge charge : chargeList)
            chargeIDList.add(charge.getChargeID());
        Query query = session.createQuery("DELETE FROM Charge C WHERE C.chargeID in :chargeIDList");
        query.setParameterList("chargeIDList", chargeIDList);
        query.executeUpdate();
    }

    @Override
    public Charge getEager(Integer chargeID){
        return loadCollections(get(chargeID));
    }

    @Override
    public Charge loadCollections(Charge charge){
        Session session = currentSession();
        charge = session.get(Charge.class, charge.getChargeID());
        Hibernate.initialize(charge.getChargeLineSet());
        session.flush();
        return charge;
    }

    @Override
    public Collection<Charge> loadCollections(Collection<Charge> chargeList){
        List<Charge> loadedChargeList = new ArrayList<>();
        for(Charge charge : chargeList) {
            loadedChargeList.add(loadCollections(charge));
        }
        return loadedChargeList;
    }
}
