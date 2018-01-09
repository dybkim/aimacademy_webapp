package com.aimacademyla.model.builder.dto;

import com.aimacademyla.dao.ChargeDAO;
import com.aimacademyla.dao.factory.DAOFactory;
import com.aimacademyla.dao.flow.impl.ChargeDAOAccessFlow;
import com.aimacademyla.model.Charge;
import com.aimacademyla.model.ChargeLine;
import com.aimacademyla.model.Course;
import com.aimacademyla.model.Member;
import com.aimacademyla.model.builder.GenericBuilder;
import com.aimacademyla.model.dto.MemberChargesFinancesDTO;
import com.aimacademyla.model.enums.AIMEntityType;
import com.aimacademyla.model.enums.BillableUnitType;
import com.aimacademyla.model.reference.TemporalReference;
import com.aimacademyla.service.ChargeLineService;
import com.aimacademyla.service.ChargeService;
import com.aimacademyla.service.CourseService;
import com.aimacademyla.service.factory.ServiceFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

public class MemberChargesFinancesDTOBuilder extends GenericDTOBuilderImpl<MemberChargesFinancesDTO> implements GenericBuilder<MemberChargesFinancesDTO>{

    private LocalDate selectedDate;
    private Member member;
    private ChargeDAO chargeDAO;

    public MemberChargesFinancesDTOBuilder(DAOFactory daoFactory){
        super(daoFactory);
        chargeDAO = (ChargeDAO) daoFactory.getDAO(Charge.class);
    }

    public MemberChargesFinancesDTOBuilder setSelectedDate(LocalDate selectedDate){
        this.selectedDate = selectedDate;
        return this;
    }

    public MemberChargesFinancesDTOBuilder setMember(Member member){
        this.member = member;
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public MemberChargesFinancesDTO build() {
        MemberChargesFinancesDTO memberChargesFinancesDTO = new MemberChargesFinancesDTO();
        List<Charge> chargeList = new ChargeDAOAccessFlow(getDAOFactory())
                                        .addQueryParameter(member)
                                        .addQueryParameter(selectedDate)
                                        .getList();
        chargeList = new ArrayList<>(chargeDAO.loadCollections(chargeList));

        HashMap<Integer, Charge> chargeHashMap = new HashMap<>();
        HashMap<Integer, List<ChargeLine>> chargeLineListHashMap = new HashMap<>();
        HashMap<Integer, Course> courseHashMap = new HashMap<>();
        HashMap<Integer, BigDecimal> billableUnitsBilledHashMap = new HashMap<>();
        BigDecimal hoursBilledTotal = BigDecimal.valueOf(0);
        BigDecimal sessionsBilledTotal = BigDecimal.valueOf(0);
        BigDecimal totalChargesAmount = BigDecimal.valueOf(0);
        BigDecimal totalDiscountAmount = BigDecimal.valueOf(0);

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

        List<LocalDate> monthsList =  TemporalReference.getMonthList();
        Collections.reverse(monthsList);

        memberChargesFinancesDTO.setMonthSelectedIndex(0);

        for(int count = 0; count < monthsList.size(); count++){
            LocalDate date = monthsList.get(count);

            if(date.getMonthValue() == selectedDate.getMonthValue() && date.getYear() == selectedDate.getYear()){
                memberChargesFinancesDTO.setMonthSelectedIndex(count);
                break;
            }
        }

        memberChargesFinancesDTO.setMember(member);
        memberChargesFinancesDTO.setChargeHashMap(chargeHashMap);
        memberChargesFinancesDTO.setChargeLineListHashMap(chargeLineListHashMap);
        memberChargesFinancesDTO.setCourseHashMap(courseHashMap);
        memberChargesFinancesDTO.setCycleStartDate(selectedDate);
        memberChargesFinancesDTO.setBillableUnitsBilledHashMap(billableUnitsBilledHashMap);
        memberChargesFinancesDTO.setMonthsList(monthsList);
        memberChargesFinancesDTO.setHoursBilledTotal(hoursBilledTotal);
        memberChargesFinancesDTO.setSessionsBilledTotal(sessionsBilledTotal);
        memberChargesFinancesDTO.setTotalChargesAmount(totalChargesAmount);
        memberChargesFinancesDTO.setTotalDiscountAmount(totalDiscountAmount);
        return memberChargesFinancesDTO;
    }
}
