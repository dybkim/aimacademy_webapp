package com.aimacademyla.model.builder.dto;

import com.aimacademyla.dao.ChargeDAO;
import com.aimacademyla.dao.flow.impl.AbstractDAOAccessFlowImpl;
import com.aimacademyla.dao.flow.impl.ChargeDAOAccessFlow;
import com.aimacademyla.model.Charge;
import com.aimacademyla.model.ChargeLine;
import com.aimacademyla.model.Course;
import com.aimacademyla.model.Member;
import com.aimacademyla.model.builder.GenericBuilder;
import com.aimacademyla.model.dto.MemberChargesFinancesDTO;
import com.aimacademyla.model.enums.BillableUnitType;
import com.aimacademyla.model.reference.TemporalReference;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

public class MemberChargesFinancesDTOBuilder extends GenericDTOBuilderImpl<MemberChargesFinancesDTO> implements GenericBuilder<MemberChargesFinancesDTO>{

    private LocalDate cycleStartDate;
    private LocalDate cycleEndDate;
    private Member member;
    private ChargeDAO chargeDAO;

    private HashMap<Integer, Charge> chargeHashMap;
    private HashMap<Integer, List<ChargeLine>> chargeLineListHashMap;
    private HashMap<Integer, Course> courseHashMap;
    private HashMap<Integer, BigDecimal> billableUnitsBilledHashMap;
    private BigDecimal hoursBilledTotal;
    private BigDecimal sessionsBilledTotal;
    private BigDecimal totalChargesAmount;
    private BigDecimal totalDiscountAmount;
    private List<LocalDate> monthsList;

    public MemberChargesFinancesDTOBuilder(){
        chargeDAO = (ChargeDAO) getDAOFactory().getDAO(Charge.class);
        chargeHashMap = new HashMap<>();
        chargeLineListHashMap = new HashMap<>();
        courseHashMap = new HashMap<>();
        billableUnitsBilledHashMap = new HashMap<>();
        hoursBilledTotal = BigDecimal.valueOf(0);
        sessionsBilledTotal = BigDecimal.valueOf(0);
        totalChargesAmount = BigDecimal.valueOf(0);
        totalDiscountAmount = BigDecimal.valueOf(0);
        monthsList =  TemporalReference.getMonthList();
        Collections.reverse(monthsList);
    }

    @Override
    public MemberChargesFinancesDTO build() {
        MemberChargesFinancesDTO memberChargesFinancesDTO = new MemberChargesFinancesDTO();

        populateValues();

        setMonthSelectedIndex(memberChargesFinancesDTO);
        memberChargesFinancesDTO.setMember(member);
        memberChargesFinancesDTO.setChargeHashMap(chargeHashMap);
        memberChargesFinancesDTO.setChargeLineListHashMap(chargeLineListHashMap);
        memberChargesFinancesDTO.setCourseHashMap(courseHashMap);
        memberChargesFinancesDTO.setCycleStartDate(cycleStartDate);
        memberChargesFinancesDTO.setBillableUnitsBilledHashMap(billableUnitsBilledHashMap);
        memberChargesFinancesDTO.setMonthsList(monthsList);
        memberChargesFinancesDTO.setHoursBilledTotal(hoursBilledTotal);
        memberChargesFinancesDTO.setSessionsBilledTotal(sessionsBilledTotal);
        memberChargesFinancesDTO.setTotalChargesAmount(totalChargesAmount);
        memberChargesFinancesDTO.setTotalDiscountAmount(totalDiscountAmount);
        return memberChargesFinancesDTO;
    }

    @SuppressWarnings("unchecked")
    private void populateValues(){
        AbstractDAOAccessFlowImpl.CyclePeriod cyclePeriod = new AbstractDAOAccessFlowImpl.CyclePeriod()
                                                                    .setCycleStartDate(cycleStartDate)
                                                                    .setCycleEndDate(cycleEndDate);

        List<Charge> chargeList = new ChargeDAOAccessFlow()
                .addQueryParameter(member)
                .addQueryParameter(cyclePeriod)
                .getList();

        chargeList = new ArrayList<>(chargeDAO.loadCollections(chargeList));

        for(Charge charge : chargeList){
            Course course = charge.getCourse();
            List<ChargeLine> chargeLineList = new ArrayList<>(charge.getChargeLineSet());

            chargeHashMap.put(charge.getChargeID(), charge);
            chargeLineListHashMap.put(charge.getChargeID(), chargeLineList);
            courseHashMap.put(charge.getChargeID(), course);
            billableUnitsBilledHashMap.put(charge.getChargeID(), charge.getBillableUnitsBilled());
            totalChargesAmount = totalChargesAmount.add(charge.getChargeAmount());
            totalDiscountAmount = totalDiscountAmount.add(charge.getDiscountAmount());

            if(charge.getBillableUnitType().equals(BillableUnitType.PER_HOUR.toString()))
                hoursBilledTotal = hoursBilledTotal.add(charge.getBillableUnitsBilled());

            else
                sessionsBilledTotal = sessionsBilledTotal.add(charge.getBillableUnitsBilled());
        }
    }

    private void setMonthSelectedIndex(MemberChargesFinancesDTO memberChargesFinancesDTO){
        memberChargesFinancesDTO.setMonthSelectedIndex(0);

        for(int count = 0; count < monthsList.size(); count++){
            LocalDate date = monthsList.get(count);

            if(date.getMonthValue() == cycleStartDate.getMonthValue() && date.getYear() == cycleStartDate.getYear()){
                memberChargesFinancesDTO.setMonthSelectedIndex(count);
                break;
            }
        }
    }


    public MemberChargesFinancesDTOBuilder setCycleStartDate(LocalDate cycleStartDate){
        this.cycleStartDate = cycleStartDate;
        return this;
    }

    public MemberChargesFinancesDTOBuilder setCycleEndDate(LocalDate cycleEndDate) {
        this.cycleEndDate = cycleEndDate;
        return this;
    }

    public MemberChargesFinancesDTOBuilder setMember(Member member){
        this.member = member;
        return this;
    }
}
