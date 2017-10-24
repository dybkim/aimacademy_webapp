package com.aimacademyla.model.builder.impl;

import com.aimacademyla.model.Charge;
import com.aimacademyla.model.ChargeLine;
import com.aimacademyla.model.Course;
import com.aimacademyla.model.Member;
import com.aimacademyla.model.builder.GenericBuilder;
import com.aimacademyla.model.enums.AIMEntityType;
import com.aimacademyla.model.enums.BillableUnitType;
import com.aimacademyla.model.reference.TemporalReference;
import com.aimacademyla.model.wrapper.MemberChargesFinancesWrapper;
import com.aimacademyla.service.ChargeLineService;
import com.aimacademyla.service.ChargeService;
import com.aimacademyla.service.CourseService;
import com.aimacademyla.service.factory.ServiceFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

public class MemberChargesFinancesWrapperBuilder extends GenericBuilderImpl<MemberChargesFinancesWrapper> implements GenericBuilder<MemberChargesFinancesWrapper>{

    private LocalDate selectedDate;
    private Member member;
    private ChargeService chargeService;
    private ChargeLineService chargeLineService;
    private CourseService courseService;

    public MemberChargesFinancesWrapperBuilder(ServiceFactory serviceFactory){
        super(serviceFactory);
        this.chargeService = (ChargeService) getServiceFactory().getService(AIMEntityType.CHARGE);
        this.chargeLineService = (ChargeLineService) getServiceFactory().getService(AIMEntityType.CHARGE_LINE);
        this.courseService = (CourseService) getServiceFactory().getService(AIMEntityType.COURSE);
    }

    public MemberChargesFinancesWrapperBuilder setSelectedDate(LocalDate selectedDate){
        this.selectedDate = selectedDate;
        return this;
    }

    public MemberChargesFinancesWrapperBuilder setMember(Member member){
        this.member = member;
        return this;
    }

    @Override
    public MemberChargesFinancesWrapper build() {
        MemberChargesFinancesWrapper memberChargesFinancesWrapper = new MemberChargesFinancesWrapper();
        List<Charge> chargeList = chargeService.getChargesByMemberByDate(member, selectedDate);

        HashMap<Integer, Charge> chargeHashMap = new HashMap<>();
        HashMap<Integer, List<ChargeLine>> chargeLineListHashMap = new HashMap<>();
        HashMap<Integer, Course> courseHashMap = new HashMap<>();
        HashMap<Integer, BigDecimal> billableUnitsBilledHashMap = new HashMap<>();
        BigDecimal hoursBilledTotal = BigDecimal.valueOf(0);
        BigDecimal sessionsBilledTotal = BigDecimal.valueOf(0);
        BigDecimal totalChargesAmount = BigDecimal.valueOf(0);
        BigDecimal totalDiscountAmount = BigDecimal.valueOf(0);

        for(Charge charge : chargeList){
            chargeHashMap.put(charge.getChargeID(), charge);
            List<ChargeLine> chargeLineList = chargeLineService.getChargeLinesByCharge(charge);
            chargeLineListHashMap.put(charge.getChargeID(), chargeLineList);
            Course course = courseService.get(charge.getCourseID());
            courseHashMap.put(charge.getChargeID(), course);
            billableUnitsBilledHashMap.put(charge.getChargeID(), charge.getBillableUnitsBilled());
            totalChargesAmount = totalChargesAmount.add(charge.getChargeAmount());
            totalDiscountAmount = totalDiscountAmount.add(charge.getDiscountAmount());


            if(charge.getBillableUnitType().equals(BillableUnitType.PER_HOUR.toString()))
                hoursBilledTotal = hoursBilledTotal.add(charge.getBillableUnitsBilled());

            else
                sessionsBilledTotal = sessionsBilledTotal.add(charge.getBillableUnitsBilled());
        }

        List<LocalDate> monthsList =  TemporalReference.getMonthList();
        Collections.reverse(monthsList);

        memberChargesFinancesWrapper.setMonthSelectedIndex(0);

        for(int count = 0; count < monthsList.size(); count++){
            LocalDate date = monthsList.get(count);

            if(date.getMonthValue() == selectedDate.getMonthValue() && date.getYear() == selectedDate.getYear()){
                memberChargesFinancesWrapper.setMonthSelectedIndex(count);
                break;
            }
        }
        memberChargesFinancesWrapper.setMember(member);
        memberChargesFinancesWrapper.setChargeHashMap(chargeHashMap);
        memberChargesFinancesWrapper.setChargeLineListHashMap(chargeLineListHashMap);
        memberChargesFinancesWrapper.setCourseHashMap(courseHashMap);
        memberChargesFinancesWrapper.setCycleStartDate(selectedDate);
        memberChargesFinancesWrapper.setBillableUnitsBilledHashMap(billableUnitsBilledHashMap);
        memberChargesFinancesWrapper.setMonthsList(monthsList);
        memberChargesFinancesWrapper.setHoursBilledTotal(hoursBilledTotal);
        memberChargesFinancesWrapper.setSessionsBilledTotal(sessionsBilledTotal);
        memberChargesFinancesWrapper.setTotalChargesAmount(totalChargesAmount);
        memberChargesFinancesWrapper.setTotalDiscountAmount(totalDiscountAmount);
        return memberChargesFinancesWrapper;
    }
}
