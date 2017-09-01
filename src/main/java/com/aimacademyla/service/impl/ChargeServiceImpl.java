package com.aimacademyla.service.impl;

import com.aimacademyla.dao.ChargeDAO;
import com.aimacademyla.dao.CourseDAO;
import com.aimacademyla.dao.GenericDAO;
import com.aimacademyla.model.*;
import com.aimacademyla.service.ChargeService;
import com.aimacademyla.service.MonthlyFinancesSummaryService;
import com.aimacademyla.service.SeasonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by davidkim on 4/10/17.
 */

@Service
public class ChargeServiceImpl extends GenericServiceImpl<Charge, Integer> implements ChargeService{

    private ChargeDAO chargeDAO;
    private MonthlyFinancesSummaryService monthlyFinancesSummaryService;
    private SeasonService seasonService;
    private CourseDAO courseDAO;

    @Autowired
    public ChargeServiceImpl(@Qualifier("chargeDAO") GenericDAO<Charge, Integer> genericDAO,
                             MonthlyFinancesSummaryService monthlyFinancesSummaryService,
                             SeasonService seasonService,
                             CourseDAO courseDAO){
        super(genericDAO);
        this.chargeDAO = (ChargeDAO) genericDAO;
        this.monthlyFinancesSummaryService = monthlyFinancesSummaryService;
        this.seasonService = seasonService;
        this.courseDAO = courseDAO;
    }

    @Override
    public void add(Charge charge) {
        MonthlyFinancesSummary monthlyFinancesSummary = monthlyFinancesSummaryService.getMonthlyFinancesSummary(charge.getCycleStartDate());

        if(charge.getMonthlyFinancesSummaryID() == null)
            charge.setMonthlyFinancesSummaryID(monthlyFinancesSummary.getMonthlyFinancesSummaryID());

        int numChargesTotal = monthlyFinancesSummary.getNumChargesTotal();
        BigDecimal totalChargeAmount = monthlyFinancesSummary.getTotalChargeAmount().add(charge.getChargeAmount());
        monthlyFinancesSummary.setNumChargesTotal(numChargesTotal + 1);
        monthlyFinancesSummary.setTotalChargeAmount(totalChargeAmount);
        monthlyFinancesSummaryService.update(monthlyFinancesSummary);

        chargeDAO.add(charge);
    }

    @Override
    public void update(Charge charge){
        Charge previousCharge = chargeDAO.get(charge.getChargeID());
        MonthlyFinancesSummary monthlyFinancesSummary = monthlyFinancesSummaryService.getMonthlyFinancesSummary(charge.getCycleStartDate());

        if(charge.getMonthlyFinancesSummaryID() == null)
            charge.setMonthlyFinancesSummaryID(monthlyFinancesSummary.getMonthlyFinancesSummaryID());

        int numChargesTotal = monthlyFinancesSummary.getNumChargesTotal();

        if(previousCharge != null){
            BigDecimal totalChargeAmount = monthlyFinancesSummary.getTotalChargeAmount().add(charge.getChargeAmount());
            monthlyFinancesSummary.setNumChargesTotal(numChargesTotal + 1);
            monthlyFinancesSummary.setTotalChargeAmount(totalChargeAmount);
            monthlyFinancesSummaryService.update(monthlyFinancesSummary);

            chargeDAO.update(charge);
            return;
        }

        BigDecimal totalChargeAmount = monthlyFinancesSummary.getTotalChargeAmount().subtract(previousCharge.getChargeAmount()).add(charge.getChargeAmount());
        monthlyFinancesSummary.setTotalChargeAmount(totalChargeAmount);
        monthlyFinancesSummaryService.update(monthlyFinancesSummary);

        chargeDAO.update(charge);
    }

    @Override
    public void remove(Charge charge){
        charge = chargeDAO.get(charge.getChargeID());
        MonthlyFinancesSummary monthlyFinancesSummary = monthlyFinancesSummaryService.getMonthlyFinancesSummary(charge.getCycleStartDate());
        if(monthlyFinancesSummary.getNumChargesTotal() <= 0){
            chargeDAO.remove(charge);
            return;
        }

        int numCharges = monthlyFinancesSummary.getNumChargesTotal();
        BigDecimal totalChargeAmount = monthlyFinancesSummary.getTotalChargeAmount().subtract(charge.getChargeAmount());
        monthlyFinancesSummary.setTotalChargeAmount(totalChargeAmount);
        monthlyFinancesSummary.setNumChargesTotal(numCharges - 1);
        monthlyFinancesSummaryService.update(monthlyFinancesSummary);
        chargeDAO.remove(charge);
    }

    @Override
    public List<Charge> getChargesByMember(Member member) {
        return getChargesByMember(member.getMemberID());
    }

    @Override
    public List<Charge> getChargesByMember(int memberID){
        return chargeDAO.getChargesByMember(memberID);
    }

    @Override
    public List<Charge> getChargesByMemberForCourse(Member member, Course course) {
        return getChargesByMemberForCourse(member.getMemberID(), course.getCourseID());
    }

    @Override
    public List<Charge> getChargesByMemberForCourse(int memberID, int courseID){
        return chargeDAO.getChargesByMemberForCourse(memberID, courseID);
    }

    @Override
    public List<Charge> getChargesByMemberByDate(Member member, LocalDate localDate) {
        return getChargesByMemberByDate(member.getMemberID(), localDate);
    }

    @Override
    public List<Charge> getChargesByMemberByDate(int memberID, LocalDate localDate){
        return chargeDAO.getChargesByMemberByDate(memberID, localDate);
    }

    @Override
    public Charge getChargeByMemberForCourseByDate(Member member, Course course, LocalDate date){
        return getChargeByMemberForCourseByDate(member.getMemberID(), course.getCourseID(), date);
    }

    @Override
    public Charge getChargeByMemberForCourseByDate(int memberID, int courseID, LocalDate date){
        Charge charge = chargeDAO.getChargeByMemberForCourseByDate(memberID, courseID, date);

        if(charge == null)
            charge = generateCharge(memberID, courseID, date);

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
    public List<Charge> getChargesByDate(LocalDate localDate){
        return chargeDAO.getChargesByDate(localDate);
    }

    @Override
    public void remove(List<Charge> chargeList){
        for(Charge charge : chargeList)
            remove(charge);
    }

    private Charge generateCharge(int memberID, int courseID, LocalDate date){
        Charge charge = new Charge();
        Season season = seasonService.getSeason(date);
        Course course = courseDAO.get(courseID);

        if(course.getCourseID() != Course.OTHER_ID)
            charge.setDescription(course.getCourseName() + " (" + course.getCourseType()+")");

        charge.setMemberID(memberID);
        charge.setCourseID(courseID);
        charge.setCycleStartDate(LocalDate.of(date.getYear(), date.getMonth(), 1));
        charge.setChargeAmount(BigDecimal.valueOf(0));
        charge.setSeasonID(season.getSeasonID());
        charge.setNumChargeLines(0);
        charge.setDiscountAmount(BigDecimal.valueOf(0));

        add(charge);

        return charge;
    }
}
