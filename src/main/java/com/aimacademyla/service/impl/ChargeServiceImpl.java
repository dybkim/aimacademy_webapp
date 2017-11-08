package com.aimacademyla.service.impl;

import com.aimacademyla.dao.ChargeDAO;
import com.aimacademyla.dao.CourseDAO;
import com.aimacademyla.dao.GenericDAO;
import com.aimacademyla.model.*;
import com.aimacademyla.model.initializer.impl.ChargeDefaultValueInitializer;
import com.aimacademyla.model.enums.AIMEntityType;
import com.aimacademyla.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by davidkim on 4/10/17.
 *
 * ChargeService handles modification of charge objects and applies business logic to modify other related objects (ie. MonthlyFinancesSummary).
 */

@Service
public class ChargeServiceImpl extends GenericServiceImpl<Charge, Integer> implements ChargeService{

    private ChargeDAO chargeDAO;
    private MonthlyFinancesSummaryService monthlyFinancesSummaryService;
    private ChargeLineService chargeLineService;
    private CourseService courseService;

    private final AIMEntityType AIM_ENTITY_TYPE = AIMEntityType.CHARGE;

    @Autowired
    public ChargeServiceImpl(@Qualifier("chargeDAO") GenericDAO<Charge, Integer> genericDAO,
                             CourseService courseService,
                             MonthlyFinancesSummaryService monthlyFinancesSummaryService,
                             ChargeLineService chargeLineService){
        super(genericDAO);
        this.chargeDAO = (ChargeDAO) genericDAO;
        this.courseService = courseService;
        this.monthlyFinancesSummaryService = monthlyFinancesSummaryService;
        this.chargeLineService = chargeLineService;
    }

    @Override
    public void add(Charge charge) {
        if(charge.getCycleStartDate() != null){
            MonthlyFinancesSummary monthlyFinancesSummary = monthlyFinancesSummaryService.getMonthlyFinancesSummary(charge.getCycleStartDate());

            if(charge.getMonthlyFinancesSummaryID() == null)
                charge.setMonthlyFinancesSummaryID(monthlyFinancesSummary.getMonthlyFinancesSummaryID());

            monthlyFinancesSummary.addCharge(charge);
            monthlyFinancesSummaryService.update(monthlyFinancesSummary);
        }

        chargeDAO.add(charge);
    }

    @Override
    public void update(Charge charge){
        Charge previousCharge = chargeDAO.get(charge.getChargeID());

        if(charge.getCycleStartDate() != null) {
            MonthlyFinancesSummary monthlyFinancesSummary = monthlyFinancesSummaryService.getMonthlyFinancesSummary(charge.getCycleStartDate());

            if(charge.getMonthlyFinancesSummaryID() == null)
                charge.setMonthlyFinancesSummaryID(monthlyFinancesSummary.getMonthlyFinancesSummaryID());

            if(previousCharge == null){
                monthlyFinancesSummary.addCharge(charge);
                monthlyFinancesSummaryService.update(monthlyFinancesSummary);

                chargeDAO.update(charge);
                return;
            }

            monthlyFinancesSummary.updateCharge(previousCharge, charge);
            monthlyFinancesSummaryService.update(monthlyFinancesSummary);
        }

        chargeDAO.update(charge);
    }

    @Override
    public void remove(Charge charge){
        charge = chargeDAO.get(charge.getChargeID());
        MonthlyFinancesSummary monthlyFinancesSummary = monthlyFinancesSummaryService.getMonthlyFinancesSummary(charge.getCycleStartDate());
        if(monthlyFinancesSummary.getNumTotalCharges() <= 0){
            chargeDAO.remove(charge);
            return;
        }

        monthlyFinancesSummary.removeCharge(charge);
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

        if(charge == null){
            charge = new ChargeDefaultValueInitializer(getDaoFactory()).setCourseID(courseID).setMemberID(memberID).setLocalDate(date).initialize();
            add(charge);
        }

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

    @Override
    public void addChargeLine(ChargeLine chargeLine){
        BigDecimal chargeLineAmount = chargeLine.getTotalCharge();
        Charge charge = chargeDAO.get(chargeLine.getChargeID());

        Course course = courseService.get(charge.getCourseID());

        BigDecimal chargeAmount = charge.getChargeAmount().add(chargeLineAmount);
        charge.setChargeAmount(chargeAmount);

        BigDecimal billableUnitsBilled = charge.getBillableUnitsBilled();

        if(billableUnitsBilled == null)
            billableUnitsBilled = BigDecimal.ZERO;

        billableUnitsBilled = billableUnitsBilled.add(course.getBillableUnitDuration());
        charge.setBillableUnitsBilled(billableUnitsBilled);

        int numChargeLines = charge.getNumChargeLines() + 1;
        charge.setNumChargeLines(numChargeLines);
        update(charge);

        chargeLineService.add(chargeLine);
    }

    @Override
    public void updateChargeLine(ChargeLine chargeLine){
        ChargeLine oldChargeLine = chargeLineService.get(chargeLine.getChargeLineID());
        BigDecimal oldChargeLineAmount = oldChargeLine.getTotalCharge();
        Charge charge = get(chargeLine.getChargeID());
        Course course = courseService.get(charge.getCourseID());

        BigDecimal billableUnitsBilled = charge.getBillableUnitsBilled();
        if(billableUnitsBilled == null)
            billableUnitsBilled = BigDecimal.ZERO;
        BigDecimal oldHoursBilled = chargeLine.getBillableUnitsBilled();
        if(oldHoursBilled == null)
            oldHoursBilled = BigDecimal.ZERO;

        billableUnitsBilled = billableUnitsBilled.subtract(oldHoursBilled);
        billableUnitsBilled = billableUnitsBilled.add(course.getBillableUnitDuration());
        charge.setBillableUnitsBilled(billableUnitsBilled);

        if(billableUnitsBilled.equals(BigDecimal.ZERO))
            charge.setBillableUnitsBilled(BigDecimal.ZERO);

        BigDecimal chargeAmount = charge.getChargeAmount().subtract(oldChargeLineAmount).add(chargeLine.getTotalCharge());
        charge.setChargeAmount(chargeAmount);
        update(charge);
        chargeLineService.update(chargeLine);
    }

    @Override
    public void removeChargeLine(ChargeLine chargeLine){
        BigDecimal chargeLineAmount = chargeLineService.get(chargeLine.getChargeLineID()).getTotalCharge();
        Charge charge = get(chargeLine.getChargeID());

        BigDecimal newChargeAmount = charge.getChargeAmount().subtract(chargeLineAmount);
        charge.setChargeAmount(newChargeAmount);

        BigDecimal billableUnitsBilled = charge.getBillableUnitsBilled();
        if(billableUnitsBilled == null)
            billableUnitsBilled = BigDecimal.ZERO;

        BigDecimal chargeLineBillableUnitsBilled = chargeLine.getBillableUnitsBilled();
        if(chargeLineBillableUnitsBilled == null)
            chargeLineBillableUnitsBilled = BigDecimal.ZERO;

        billableUnitsBilled = billableUnitsBilled.subtract(chargeLineBillableUnitsBilled);
        charge.setBillableUnitsBilled(billableUnitsBilled);

        int numChargeLines = charge.getNumChargeLines() - 1;
        charge.setNumChargeLines(numChargeLines);

        if(numChargeLines > 0){
            update(charge);
            chargeLineService.remove(chargeLine);
        }

        /*
         * chargeLine entities are deleted when charges are deleted due to cascading settings
         */
        else
           remove(charge);
    }

    @Override
    public AIMEntityType getAIMEntityType(){
        return AIM_ENTITY_TYPE;
    }
}
