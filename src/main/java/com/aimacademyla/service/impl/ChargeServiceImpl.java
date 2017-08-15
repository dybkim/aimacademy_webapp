package com.aimacademyla.service.impl;

import com.aimacademyla.dao.ChargeDAO;
import com.aimacademyla.dao.GenericDAO;
import com.aimacademyla.dao.MonthlyChargesSummaryDAO;
import com.aimacademyla.model.*;
import com.aimacademyla.service.ChargeService;
import com.aimacademyla.service.MonthlyChargesSummaryService;
import com.aimacademyla.service.SeasonService;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
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
    public List<Charge> getChargesByMember(Member member) {
        return chargeDAO.getChargesByMember(member);
    }

    @Override
    public List<Charge> getChargesByMemberForCourse(Member member, Course course) {
        return chargeDAO.getChargesByMemberForCourse(member, course);
    }

    @Override
    public List<Charge> getChargesByMemberByDate(Member member, LocalDate localDate) {
        return chargeDAO.getChargesByMemberByDate(member, localDate);
    }

    @Override
    public Charge getChargeByMemberForCourseByDate(Member member, Course course, LocalDate date){
        Charge charge = chargeDAO.getChargeByMemberForCourseByDate(member, course, date);

        if(charge == null)
            charge = generateCharge(member, course, date);

        return charge;
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
            if(monthlyChargesSummary != null) {
                charge.setMonthlyChargesSummaryID(monthlyChargesSummary.getMonthlyChargesSummaryID());
                charge.setSeasonID(monthlyChargesSummary.getSeasonID());
            }
        }

        chargeDAO.add(charge);
    }

    private Charge generateCharge(Member member, Course course, LocalDate date){
        Charge charge = new Charge();
        Season season = seasonService.getSeason(date);

        if(course.getCourseID() != Course.OTHER_ID)
            charge.setDescription(course.getCourseName() + " " + course.getCourseType());

        charge.setMemberID(member.getMemberID());
        charge.setCourseID(course.getCourseID());
        charge.setCycleStartDate(LocalDate.of(date.getYear(), date.getMonth(), 1));
        charge.setChargeAmount(0);
        charge.setSeasonID(season.getSeasonID());
        charge.setNumChargeLines(0);

        chargeDAO.add(charge);

        return charge;
    }
}
