package com.aimacademyla.model.builder.dto;

import com.aimacademyla.dao.factory.DAOFactory;
import com.aimacademyla.dao.flow.impl.MemberMonthlyRegistrationDAOAccessFlow;
import com.aimacademyla.model.Member;
import com.aimacademyla.model.MemberMonthlyRegistration;
import com.aimacademyla.model.builder.GenericBuilder;
import com.aimacademyla.model.dto.MemberListDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class MemberListDTOBuilder extends GenericDTOBuilderImpl<MemberListDTO> implements GenericBuilder<MemberListDTO> {

    private static final Logger logger = LogManager.getLogger(MemberListDTO.class.getName());

    private LocalDate cycleStartDate;

    public MemberListDTOBuilder(DAOFactory daoFactory) {
        super(daoFactory);
    }

    public MemberListDTOBuilder setCycleStartDate(LocalDate cycleStartDate){
        this.cycleStartDate = cycleStartDate;
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public MemberListDTO build() {
        MemberListDTO memberListDTO = new MemberListDTO();
        List<Member> activeMemberList = new ArrayList<>();
        List<Member> inactiveMemberList = getDAOFactory().getDAO(Member.class).getList();
        logger.debug("Fetching member list...");

        for(Member member : inactiveMemberList){
            logger.debug("Member info is: " + member.getMemberFirstName() + " " + member.getMemberLastName());
        }

        HashMap<Integer, Boolean> isActiveMemberHashMap = new HashMap<>();
        List<MemberMonthlyRegistration> memberMonthlyRegistrationList = new MemberMonthlyRegistrationDAOAccessFlow(getDAOFactory())
                                                                            .addQueryParameter(cycleStartDate)
                                                                            .getList();

        Iterator it = inactiveMemberList.iterator();

        /*
         * Since null Member has ID of 1, any entity less than or equal 1 is removed.
         */
        while(it.hasNext()){
            Member member = (Member) it.next();
            if(member.getMemberID() <= 1)
                it.remove();
        }

        for(Member member : inactiveMemberList)
            isActiveMemberHashMap.put(member.getMemberID(), false);

        for(MemberMonthlyRegistration memberMonthlyRegistration : memberMonthlyRegistrationList){
            Member member = memberMonthlyRegistration.getMember();
            activeMemberList.add(member);
            isActiveMemberHashMap.put(member.getMemberID(), true);

            it = inactiveMemberList.iterator();
            while(it.hasNext()){
                Member iteratedMember = (Member) it.next();
                if(member.getMemberID() == iteratedMember.getMemberID()){
                    it.remove();
                    break;
                }
            }
        }

        memberListDTO.setActiveMemberList(activeMemberList);
        memberListDTO.setInactiveMemberList(inactiveMemberList);
        memberListDTO.setIsActiveMemberHashMap(isActiveMemberHashMap);
        memberListDTO.setCycleStartDate(cycleStartDate);
        return memberListDTO;
    }
}
