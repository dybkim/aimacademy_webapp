package com.aimacademyla.model.builder.dto;

import com.aimacademyla.dao.ChargeDAO;
import com.aimacademyla.dao.flow.impl.AbstractDAOAccessFlowImpl;
import com.aimacademyla.dao.flow.impl.ChargeDAOAccessFlow;
import com.aimacademyla.dao.flow.impl.PaymentDAOAccessFlow;
import com.aimacademyla.model.Charge;
import com.aimacademyla.model.ChargeLine;
import com.aimacademyla.model.Member;
import com.aimacademyla.model.Payment;
import com.aimacademyla.model.builder.GenericBuilder;
import com.aimacademyla.model.dto.OutstandingChargesPaymentDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class OutstandingChargesPaymentDTOBuilder extends GenericDTOBuilderImpl<OutstandingChargesPaymentDTO> implements GenericBuilder<OutstandingChargesPaymentDTO>{

    private LocalDate cycleStartDate;
    private LocalDate cycleEndDate;
    private ChargeDAO chargeDAO;

    private HashMap<Integer, BigDecimal> allMembersChargesHashMap;
    private HashMap<Integer, BigDecimal> allMembersPaymentHashMap;

    private List<Member> outstandingBalanceMemberList;
    private List<Member> paidBalanceMemberList;

    private HashMap<Integer, Member> allMemberHashMap;
    private HashMap<Integer, BigDecimal> balanceAmountHashMap;
    private HashMap<Integer, List<Charge>> chargeListHashMap;

    public OutstandingChargesPaymentDTOBuilder(){
        this.chargeDAO = (ChargeDAO) getDAOFactory().getDAO(Charge.class);
        this.allMembersChargesHashMap = new HashMap<>();
        this.allMembersPaymentHashMap = new HashMap<>();
        this.outstandingBalanceMemberList = new ArrayList<>();
        this.paidBalanceMemberList = new ArrayList<>();
        this.allMemberHashMap = new HashMap<>();
        this.balanceAmountHashMap = new HashMap<>();
        this.chargeListHashMap = new HashMap<>();
    }

    public OutstandingChargesPaymentDTOBuilder setCycleStartDate(LocalDate cycleStartDate) {
        this.cycleStartDate = cycleStartDate;
        return this;
    }

    public OutstandingChargesPaymentDTOBuilder setCycleEndDate(LocalDate cycleEndDate){
        this.cycleEndDate = cycleEndDate;
        return this;
    }

    @Override
    public OutstandingChargesPaymentDTO build() {
        OutstandingChargesPaymentDTO outstandingChargesPaymentDTO = new OutstandingChargesPaymentDTO();

        List<Charge> chargeList = fetchChargeList();

        /*
         * Create separate method for this
         */
        Iterator it = chargeList.iterator();
        while(it.hasNext()){
            Charge charge = (Charge) it.next();
            Member member = charge.getMember();
            allMemberHashMap.put(member.getMemberID(), member);
            balanceAmountHashMap.put(member.getMemberID(), BigDecimal.ZERO);

            /*
             * Remove ChargeLines in Charge.ChargeLineSet if ChargeLine's dateCharged is not within the cycle period date range
             * If Charge has no more ChargeLines, then remove Charge from list
             */
            charge = chargeDAO.loadCollections(charge);
            removeInvalidChargeLines(charge);

            if(charge.getChargeLineSet().isEmpty()) {
                it.remove();
                continue;
            }

            populateFinancesHashMaps(charge);
        }

        populateBalancesCollections();

        for(Charge charge : chargeList)
            chargeListHashMap.get(charge.getMember().getMemberID()).add(charge);

        outstandingChargesPaymentDTO.setCycleStartDate(cycleStartDate);
        outstandingChargesPaymentDTO.setPaidBalanceMemberList(paidBalanceMemberList);
        outstandingChargesPaymentDTO.setOutstandingBalanceMemberList(outstandingBalanceMemberList);
        outstandingChargesPaymentDTO.setChargesAmountHashMap(allMembersChargesHashMap);
        outstandingChargesPaymentDTO.setPaymentAmountHashMap(allMembersPaymentHashMap);
        outstandingChargesPaymentDTO.setBalanceAmountHashMap(balanceAmountHashMap);
        outstandingChargesPaymentDTO.setChargeListHashMap(chargeListHashMap);

        return outstandingChargesPaymentDTO;
    }

    @SuppressWarnings("unchecked")
    private List<Charge> fetchChargeList(){
        AbstractDAOAccessFlowImpl.CyclePeriod cyclePeriod = new AbstractDAOAccessFlowImpl.CyclePeriod();
        cyclePeriod.setCycleStartDate(LocalDate.of(cycleStartDate.getYear(), cycleStartDate.getMonthValue(), 1)).setCycleEndDate(cycleEndDate);

        return new ChargeDAOAccessFlow()
                .addQueryParameter(cyclePeriod)
                .getList();
    }

    private void populateFinancesHashMaps(Charge charge){
        addCharge(charge);
        addPayment(charge);
    }

    private void addCharge(Charge charge){
        Member member = charge.getMember();
        BigDecimal chargeAmount = charge.getChargeAmount();

        if(allMembersChargesHashMap.containsKey(member.getMemberID())){
            chargeAmount = allMembersChargesHashMap.get(member.getMemberID()).add(chargeAmount);
            allMembersChargesHashMap.put(member.getMemberID(), chargeAmount);
        }

        else
            allMembersChargesHashMap.put(member.getMemberID(), chargeAmount);
    }

    private void addPayment(Charge charge){
        Member member = charge.getMember();
        if(!allMembersPaymentHashMap.containsKey(member.getMemberID())){
            Payment payment = (Payment) new PaymentDAOAccessFlow()
                    .addQueryParameter(member)
                    .addQueryParameter(cycleStartDate)
                    .get();

            allMembersPaymentHashMap.put(member.getMemberID(), payment.getPaymentAmount());
        }
    }

    private void populateBalancesCollections(){
        for(Integer memberID : allMembersChargesHashMap.keySet()){
            chargeListHashMap.put(memberID, new ArrayList<>());
            BigDecimal outstandingBalance = allMembersChargesHashMap.get(memberID).subtract(allMembersPaymentHashMap.get(memberID));

            if(outstandingBalance.compareTo(BigDecimal.ZERO) <= 0) {
                paidBalanceMemberList.add(allMemberHashMap.get(memberID));
                balanceAmountHashMap.put(memberID, outstandingBalance);
            }

            else{
                outstandingBalanceMemberList.add(allMemberHashMap.get(memberID));
                balanceAmountHashMap.put(memberID, outstandingBalance);
            }
        }
    }

    private void removeInvalidChargeLines(Charge charge){
        Iterator it = charge.getChargeLineSet().iterator();

        while(it.hasNext()){
            ChargeLine chargeLine = (ChargeLine) it.next();
                /*
                 * Can't call Charge.removeChargeLine() because of concurrent modification, have to call all required methods externally
                 */
            if(!((chargeLine.getDateCharged().isAfter(cycleStartDate) ||  chargeLine.getDateCharged().isEqual(cycleStartDate)) && (chargeLine.getDateCharged().isBefore(cycleEndDate) ||  chargeLine.getDateCharged().isEqual(cycleEndDate)))){
                BigDecimal chargeAmount = charge.getChargeAmount();
                BigDecimal billableUnitsBilled = charge.getBillableUnitsBilled().subtract(chargeLine.getBillableUnitsBilled());
                int numChargeLines = charge.getNumChargeLines();

                chargeAmount = chargeAmount.subtract(chargeLine.getChargeAmount());
                numChargeLines--;

                charge.setChargeAmount(chargeAmount);
                charge.setBillableUnitsBilled(billableUnitsBilled);
                charge.setNumChargeLines(numChargeLines);

                it.remove();
            }
        }
    }
}
