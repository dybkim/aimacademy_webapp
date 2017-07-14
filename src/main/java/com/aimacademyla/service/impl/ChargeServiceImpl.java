package com.aimacademyla.service.impl;

import com.aimacademyla.dao.ChargeDAO;
import com.aimacademyla.dao.GenericDAO;
import com.aimacademyla.dao.MonthlyChargesSummaryDAO;
import com.aimacademyla.model.*;
import com.aimacademyla.service.ChargeService;
import com.aimacademyla.service.MonthlyChargesSummaryService;
import com.aimacademyla.service.SeasonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by davidkim on 4/10/17.
 */

@Service
public class ChargeServiceImpl extends GenericServiceImpl<Charge, Integer> implements ChargeService{

    private ChargeDAO chargeDAO;
    private MonthlyChargesSummaryService monthlyChargesSummaryService;
    private SeasonService seasonService;

    @Autowired
    public ChargeServiceImpl(@Qualifier("chargeDAO") GenericDAO<Charge, Integer> genericDAO,
                             MonthlyChargesSummaryService monthlyChargesSummaryService,
                             SeasonService seasonService){
        super(genericDAO);
        this.chargeDAO = (ChargeDAO) genericDAO;
        this.monthlyChargesSummaryService = monthlyChargesSummaryService;
        this.seasonService = seasonService;
    }

    @Override
    public List<Charge> getChargesByMember(int memberID) {
        return chargeDAO.getChargesByMember(memberID);
    }

    @Override
    public List<Charge> getChargesByMember(Member member) {
        return chargeDAO.getChargesByMember(member);
    }

    @Override
    public List<Charge> getChargesByCourse(int courseID) {
        return chargeDAO.getChargesByCourse(courseID);
    }

    @Override
    public List<Charge> getChargesByCourse(Course course) {
        return chargeDAO.getChargesByCourse(course);
    }

    @Override
    public void add(Charge charge) {
        if(charge.getMonthlyChargesSummaryID() == null){
            MonthlyChargesSummary monthlyChargesSummary = monthlyChargesSummaryService.getMonthlyChargesSummary(charge.getCycleStartDate());
            charge.setMonthlyChargesSummaryID(monthlyChargesSummary.getMonthlyChargesSummaryID());
            charge.setSeasonID(monthlyChargesSummary.getSeasonID());
        }

        chargeDAO.add(charge);
    }
}
