package com.aimacademyla.model.builder.dto;

import com.aimacademyla.dao.ChargeDAO;
import com.aimacademyla.dao.factory.DAOFactory;
import com.aimacademyla.dao.flow.impl.ChargeDAOAccessFlow;
import com.aimacademyla.dao.flow.impl.PaymentDAOAccessFlow;
import com.aimacademyla.model.Charge;
import com.aimacademyla.model.Member;
import com.aimacademyla.model.Payment;
import com.aimacademyla.model.builder.GenericBuilder;
import com.aimacademyla.model.dto.MemberCourseFinancesDTO;
import com.aimacademyla.model.enums.AIMEntityType;
import com.aimacademyla.service.ChargeService;
import com.aimacademyla.service.PaymentService;
import com.aimacademyla.service.factory.ServiceFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;

public class MemberCourseFinancesDTOBuilder extends GenericDTOBuilderImpl<MemberCourseFinancesDTO> implements GenericBuilder<MemberCourseFinancesDTO>{
    private LocalDate cycleStartDate;
    private Member member;

    public MemberCourseFinancesDTOBuilder(DAOFactory daoFactory){
        super(daoFactory);
    }

    public MemberCourseFinancesDTOBuilder setCycleStartDate(LocalDate cycleStartDate) {
        this.cycleStartDate = cycleStartDate;
        return this;
    }

    public MemberCourseFinancesDTOBuilder setMember(Member member) {
        this.member = member;
        return this;
    }

    @SuppressWarnings("unchecked")
    public MemberCourseFinancesDTO build(){
        MemberCourseFinancesDTO memberCourseFinancesDTO = new MemberCourseFinancesDTO();

        BigDecimal totalChargeAmount = BigDecimal.valueOf(0);

        List<Charge> chargeList = new ChargeDAOAccessFlow(getDAOFactory())
                                                .addQueryParameter(member)
                                                .addQueryParameter(cycleStartDate)
                                                .getList();

        BigDecimal totalPaymentAmount = BigDecimal.ZERO;

        if(chargeList.size() > 0){
            totalPaymentAmount = ((Payment) new PaymentDAOAccessFlow(getDAOFactory())
                    .addQueryParameter(member)
                    .addQueryParameter(cycleStartDate)
                    .get()).getPaymentAmount();
        }

        //Remove charges from charge list that have an amount of 0 dollars
        Iterator it = chargeList.iterator();

        while(it.hasNext()){
            Charge charge = (Charge) it.next();

            if(charge.getChargeAmount().compareTo(BigDecimal.ZERO) <= 0){
                it.remove();
                continue;
            }

            totalChargeAmount = totalChargeAmount.add((charge.getChargeAmount().subtract(charge.getDiscountAmount())));
        }

        memberCourseFinancesDTO.setChargeList(chargeList);
        memberCourseFinancesDTO.setDate(cycleStartDate);
        memberCourseFinancesDTO.setTotalChargeAmount(totalChargeAmount);
        memberCourseFinancesDTO.setTotalPaymentAmount(totalPaymentAmount);

        return memberCourseFinancesDTO;
    }
}
