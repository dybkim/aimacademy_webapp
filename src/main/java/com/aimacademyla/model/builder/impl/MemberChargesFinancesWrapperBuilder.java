package com.aimacademyla.model.builder.impl;

import com.aimacademyla.formatter.LocalDateFormatter;
import com.aimacademyla.model.Charge;
import com.aimacademyla.model.ChargeLine;
import com.aimacademyla.model.Course;
import com.aimacademyla.model.Member;
import com.aimacademyla.model.builder.GenericBuilder;
import com.aimacademyla.model.reference.TemporalReference;
import com.aimacademyla.model.wrapper.MemberChargesFinancesWrapper;
import com.aimacademyla.service.ChargeLineService;
import com.aimacademyla.service.ChargeService;
import com.aimacademyla.service.CourseService;

import java.time.LocalDate;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MemberChargesFinancesWrapperBuilder implements GenericBuilder<MemberChargesFinancesWrapper>{

    private MemberChargesFinancesWrapper memberChargesFinancesWrapper;
    private LocalDate selectedDate;
    private Member member;
    private ChargeService chargeService;
    private ChargeLineService chargeLineService;
    private CourseService courseService;

    private MemberChargesFinancesWrapperBuilder(){}

    public MemberChargesFinancesWrapperBuilder(ChargeService chargeService,
                                               ChargeLineService chargeLineService,
                                               CourseService courseService){
        memberChargesFinancesWrapper = new MemberChargesFinancesWrapper();
        this.chargeService = chargeService;
        this.chargeLineService = chargeLineService;
        this.courseService = courseService;
    }

    public MemberChargesFinancesWrapperBuilder setSelectedDate(LocalDate selectedDate){
        this.selectedDate = selectedDate;
        return this;
    }

    public MemberChargesFinancesWrapperBuilder setMember(Member member){
        this.member = member;
        return this;
    }

    public MemberChargesFinancesWrapperBuilder setChargeService(ChargeService chargeService){
        this.chargeService = chargeService;
        return this;
    }

    public MemberChargesFinancesWrapperBuilder setChargeLineService(ChargeLineService chargeLineService){
        this.chargeLineService = chargeLineService;
        return this;
    }

    public MemberChargesFinancesWrapperBuilder setCourseService(CourseService courseService){
        this.courseService = courseService;
        return this;
    }

    @Override
    public MemberChargesFinancesWrapper build() {
        List<Charge> chargeList = chargeService.getChargesByMemberByDate(member, selectedDate);

        HashMap<Integer, Charge> chargeHashMap = new HashMap<>();
        HashMap<Integer, List<ChargeLine>> chargeLineListHashMap = new HashMap<>();
        HashMap<Integer, Course> courseHashMap = new HashMap<>();
        HashMap<Integer, Double> hoursBilledHashMap = new HashMap<>();
        List<LocalDate> monthsList = new ArrayList<>();
        double hoursBilledTotal = 0;
        double totalChargesAmount = 0;
        double totalDiscountAmount = 0;

        for(Charge charge : chargeList){
            chargeHashMap.put(charge.getChargeID(), charge);
            List<ChargeLine> chargeLineList = chargeLineService.getChargeLinesByCharge(charge);
            chargeLineListHashMap.put(charge.getChargeID(), chargeLineList);
            Course course = courseService.get(charge.getCourseID());
            courseHashMap.put(charge.getChargeID(), course);
            hoursBilledHashMap.put(charge.getChargeID(), course.getClassDuration() * chargeLineList.size());
            hoursBilledTotal += course.getClassDuration() * chargeLineList.size();
            totalChargesAmount += charge.getChargeAmount();
            totalDiscountAmount += charge.getDiscountAmount();
        }

//        List<Charge> allChargesList = chargeService.getChargesByMember(member);

//        outer:
//        for(Charge charge : allChargesList){
//            if(charge.getChargeAmount() > 0){
//                for(LocalDate date : monthsList){
//                    if(date.getMonthValue() == charge.getCycleStartDate().getMonthValue() && date.getYear() == charge.getCycleStartDate().getYear())
//                        continue outer;
//                }
//                monthsList.add(charge.getCycleStartDate());
//            }
//        }

        monthsList =  TemporalReference.getMonthList();

        memberChargesFinancesWrapper.setMonthSelectedIndex(0);

        for(int count = 0; count < monthsList.size(); count++){
            LocalDate date = monthsList.get(count);

            if(date.getMonthValue() == selectedDate.getMonthValue() && date.getYear() == selectedDate.getYear()){
                memberChargesFinancesWrapper.setMonthSelectedIndex(count);
                break;
            }
        }

        memberChargesFinancesWrapper.setChargeHashMap(chargeHashMap);
        memberChargesFinancesWrapper.setChargeLineListHashMap(chargeLineListHashMap);
        memberChargesFinancesWrapper.setCourseHashMap(courseHashMap);
        memberChargesFinancesWrapper.setCycleStartDate(selectedDate);
        memberChargesFinancesWrapper.setHoursBilledHashMap(hoursBilledHashMap);
        memberChargesFinancesWrapper.setMonthsList(monthsList);
        memberChargesFinancesWrapper.setHoursBilledTotal(hoursBilledTotal);
        memberChargesFinancesWrapper.setTotalChargesAmount(totalChargesAmount);
        memberChargesFinancesWrapper.setTotalDiscountAmount(totalDiscountAmount);
        return memberChargesFinancesWrapper;
    }
}
