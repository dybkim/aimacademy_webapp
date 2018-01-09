package com.aimacademyla.dao.impl;

import com.aimacademyla.dao.ChargeLineDAO;
import com.aimacademyla.model.Charge;
import com.aimacademyla.model.ChargeLine;
import com.aimacademyla.model.Member;
import com.aimacademyla.model.enums.AIMEntityType;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    public void removeList(List<ChargeLine> chargeLineList){
        Session session = currentSession();
        List<Integer> chargeLineIDList = new ArrayList<>();
        for(ChargeLine chargeLine : chargeLineList)
            chargeLineIDList.add(chargeLine.getChargeLineID());
        Query query = session.createQuery("DELETE FROM Charge_Line C WHERE C.charge IN :chargeLineIDList");
        query.setParameterList("chargeLineIDList", chargeLineIDList);
        query.executeUpdate();
    }

    @Override
    public List<ChargeLine> getList(Member member, LocalDate cycleStartDate, LocalDate cycleEndDate){
        Session session = currentSession();

        Query query = session.createQuery("FROM Charge_Line Cl WHERE (Cl.dateCharged BETWEEN :cycleStartDate AND :cycleEndDate) AND Cl.charge.member.memberID = :memberID")
                        .setParameter("memberID", member.getMemberID())
                        .setParameter("cycleStartDate", cycleStartDate)
                        .setParameter("cycleEndDate", cycleEndDate);

        List<ChargeLine> chargeLineList = query.getResultList();
        return chargeLineList;
    }
}
