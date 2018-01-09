package com.aimacademyla.model.builder.dto;

import com.aimacademyla.dao.ChargeDAO;
import com.aimacademyla.dao.ChargeLineDAO;
import com.aimacademyla.dao.MemberDAO;
import com.aimacademyla.dao.PaymentDAO;
import com.aimacademyla.dao.factory.DAOFactory;
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
import java.util.List;

public class OutstandingChargesPaymentDTOBuilder extends GenericDTOBuilderImpl<OutstandingChargesPaymentDTO> implements GenericBuilder<OutstandingChargesPaymentDTO>{


    private LocalDate cycleStartDate;
    private LocalDate cycleEndDate;
    private ChargeLineDAO chargeLineDAO;

    public OutstandingChargesPaymentDTOBuilder(DAOFactory daoFactory){
        super(daoFactory);
        this.chargeLineDAO = (ChargeLineDAO)daoFactory.getDAO(ChargeLine.class);
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
        List<Member> outstandingBalanceMemberList = new ArrayList<>();
        List<Member> paidBalanceMemberList = new ArrayList<>();

        HashMap<Integer, Member> allMemberHashMap = new HashMap<>();
        HashMap<Integer, BigDecimal> allMembersPaymentHashMap = new HashMap<>();
        HashMap<Integer, BigDecimal> allMembersChargesHashMap = new HashMap<>();
        HashMap<Integer, BigDecimal> balanceAmountHashMap = new HashMap<>();
        HashMap<Integer, List<Charge>> chargeListHashMap = new HashMap<>();

        AbstractDAOAccessFlowImpl.CyclePeriod cyclePeriod = new AbstractDAOAccessFlowImpl.CyclePeriod();
        cyclePeriod.setCycleStartDate(cycleStartDate).setCycleEndDate(cycleEndDate);

        @SuppressWarnings("unchecked")
        List<Charge> chargeList = new ChargeDAOAccessFlow(getDAOFactory())
                                            .addQueryParameter(cyclePeriod)
                                            .getList();

        for(Charge charge : chargeList){
            Member member = charge.getMember();
            allMemberHashMap.put(member.getMemberID(), member);
            balanceAmountHashMap.put(member.getMemberID(), BigDecimal.ZERO);

            BigDecimal chargeAmount = charge.getChargeAmount();

            if(allMembersChargesHashMap.containsKey(member.getMemberID())){
                chargeAmount = allMembersChargesHashMap.get(member.getMemberID()).add(chargeAmount);
                allMembersChargesHashMap.put(member.getMemberID(), chargeAmount);
            }

            else
                allMembersChargesHashMap.put(member.getMemberID(), chargeAmount);

            if(!allMembersPaymentHashMap.containsKey(member.getMemberID())){
                Payment payment = (Payment) new PaymentDAOAccessFlow(getDAOFactory())
                                                .addQueryParameter(member)
                                                .addQueryParameter(cycleStartDate)
                                                .get();

                allMembersPaymentHashMap.put(member.getMemberID(), payment.getPaymentAmount());
            }
        }

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
}
