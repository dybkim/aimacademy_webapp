package com.aimacademyla.service.impl;

import com.aimacademyla.dao.*;
import com.aimacademyla.dao.flow.impl.ChargeDAOAccessFlow;
import com.aimacademyla.dao.flow.impl.MonthlyFinancesSummaryDAOAccessFlow;
import com.aimacademyla.dao.flow.impl.PaymentDAOAccessFlow;
import com.aimacademyla.model.*;
import com.aimacademyla.model.builder.entity.ChargeBuilder;
import com.aimacademyla.service.ChargeService;
import com.aimacademyla.service.MonthlyFinancesSummaryService;
import com.aimacademyla.service.PaymentService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by davidkim on 4/10/17.
 *
 * ChargeService handles modification of charge objects and applies business logic to modify other related objects (ie. MonthlyFinancesSummary).
 */

@Service
public class ChargeServiceImpl extends GenericServiceImpl<Charge, Integer> implements ChargeService{

    private static final Logger logger = LogManager.getLogger(ChargeServiceImpl.class.getName());

    private ChargeDAO chargeDAO;
    private ChargeLineDAO chargeLineDAO;
    private MonthlyFinancesSummaryService monthlyFinancesSummaryService;
    private MonthlyFinancesSummaryDAO monthlyFinancesSummaryDAO;
    private PaymentService paymentService;
    private PaymentDAO paymentDAO;

    @Autowired
    public ChargeServiceImpl(@Qualifier("chargeDAO") GenericDAO<Charge, Integer> genericDAO,
                             ChargeLineDAO chargeLineDAO,
                             MonthlyFinancesSummaryService monthlyFinancesSummaryService,
                             MonthlyFinancesSummaryDAO monthlyFinancesSummaryDAO,
                             PaymentService paymentService,
                             PaymentDAO paymentDAO){
        super(genericDAO);
        this.chargeDAO = (ChargeDAO) genericDAO;
        this.chargeLineDAO = chargeLineDAO;
        this.monthlyFinancesSummaryService = monthlyFinancesSummaryService;
        this.monthlyFinancesSummaryDAO = monthlyFinancesSummaryDAO;
        this.paymentService = paymentService;
        this.paymentDAO = paymentDAO;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Charge> getList(Member member, LocalDate date){
        return new ChargeDAOAccessFlow()
                        .addQueryParameter(member)
                        .addQueryParameter(date)
                        .getList();
    }

    /*
     * getTransientChargeList method implementation combines Charges that are of the same Course into one Charge
     */
    @Override
    public List<Charge> getTransientChargeList(Member member, LocalDate cycleStartDate, LocalDate cycleEndDate){
        Map<Integer, Charge> chargeMap = new HashMap<>();
        List<ChargeLine> chargeLineList = chargeLineDAO.getList(member, cycleStartDate, cycleEndDate);

        for(ChargeLine chargeLine : chargeLineList){
            Course course = chargeLine.getCharge().getCourse();
            int courseID = course.getCourseID();
            Charge persistedCharge = chargeLine.getCharge();
            Charge transientCharge = chargeMap.get(courseID);
            if(transientCharge == null){
                transientCharge = new ChargeBuilder()
                                .setNumChargeLines(0)
                                .setDescription(persistedCharge.getDescription())
                                .setBillableUnitsBilled(BigDecimal.ZERO)
                                .setBillableUnitsType(persistedCharge.getBillableUnitType())
                                .setChargeAmount(BigDecimal.ZERO)
                                .setCourse(course)
                                .build();
                chargeMap.put(courseID, transientCharge);
            }
            transientCharge.addChargeLine(chargeLine);
        }

        /*
         * Something wrong with hashCode implementation
         */
//        for(Charge charge : chargeMap.values())
//            charge.sortChargeLineSetByDate();

        return new ArrayList<>(chargeMap.values());
    }

    @Override
    public  void addCharge(Charge charge){
        Payment payment = getPayment(charge);
        payment.addCharge(charge);

        //Need to update monthlyFinancesSummary again to add Charge and update Payment via cascade
        MonthlyFinancesSummary monthlyFinancesSummary = getMonthlyFinancesSummary(charge);
        logger.debug("Adding Charge: " + charge.getChargeID() + " for MonthlyFinancesSummary: " + monthlyFinancesSummary.getMonthlyFinancesSummaryID());
        monthlyFinancesSummary.addCharge(charge);
        monthlyFinancesSummary.updatePayment(payment);
        monthlyFinancesSummaryService.update(monthlyFinancesSummary);
        logger.debug("Added Charge for MonthlyFinancesSummary: "+ monthlyFinancesSummary.getMonthlyFinancesSummaryID());

        //Need to add charge to persist chargeLines
        chargeDAO.add(charge);
    }

    @Override
    public void updateCharge(Charge charge){
        Payment payment = getPayment(charge);
        payment.updateCharge(charge);

        //Need to update monthlyFinancesSummary again to update Charge and update Payment via cascade
        MonthlyFinancesSummary monthlyFinancesSummary = getMonthlyFinancesSummary(charge);
        logger.debug("Updating Charge: " + charge.getChargeID() + " for MonthlyFinancesSummary: " + monthlyFinancesSummary.getMonthlyFinancesSummaryID());
        monthlyFinancesSummary.updateCharge(charge);
        monthlyFinancesSummary.updatePayment(payment);
        monthlyFinancesSummaryService.update(monthlyFinancesSummary);
        logger.debug("Updated Charge for MonthlyFinancesSummary: "+ monthlyFinancesSummary.getMonthlyFinancesSummaryID());

        //Need to update charge to cascade update detached chargeLines
        chargeDAO.update(charge);
    }

    @Override
    public void removeCharge(Charge charge){
        Payment payment = getPayment(charge);
        payment.removeCharge(charge);

        //Need to update monthlyFinancesSummary to remove Charge and update Payment via cascade
        MonthlyFinancesSummary monthlyFinancesSummary = getMonthlyFinancesSummary(charge);
        monthlyFinancesSummary.removeCharge(charge);
        monthlyFinancesSummary.updatePayment(payment);
        monthlyFinancesSummaryService.update(monthlyFinancesSummary);
        logger.debug("Removed Charge from MonthlyFinancesSummary");
    }

    private Payment getPayment(Charge charge){
        Payment payment = charge.getPayment();
        if(payment != null) {
            payment = paymentService.get(payment.getPaymentID());
            if(payment != null){
                payment = paymentDAO.loadCollections(payment);
                return payment;
            }
        }

        payment = (Payment) new PaymentDAOAccessFlow()
                .addQueryParameter(charge.getCycleStartDate())
                .addQueryParameter(charge.getMember())
                .get();

        payment = paymentDAO.loadCollections(payment);
        charge.setPayment(payment);
        return payment;
    }

    private MonthlyFinancesSummary getMonthlyFinancesSummary(Charge charge){
        MonthlyFinancesSummary monthlyFinancesSummary = charge.getMonthlyFinancesSummary();
        if(monthlyFinancesSummary != null){
            monthlyFinancesSummary = monthlyFinancesSummaryService.get(monthlyFinancesSummary.getMonthlyFinancesSummaryID());
            if(monthlyFinancesSummary != null){
                monthlyFinancesSummary = monthlyFinancesSummaryDAO.loadCollections(monthlyFinancesSummary);
                return monthlyFinancesSummary;
            }
        }

        monthlyFinancesSummary = (MonthlyFinancesSummary) new MonthlyFinancesSummaryDAOAccessFlow()
                .addQueryParameter(charge.getCycleStartDate())
                .get();

        monthlyFinancesSummary = monthlyFinancesSummaryDAO.loadCollections(monthlyFinancesSummary);
        charge.setMonthlyFinancesSummary(monthlyFinancesSummary);

        return monthlyFinancesSummary;
    }
}
