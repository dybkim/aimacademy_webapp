package com.aimacademyla.model.builder.dto;

import com.aimacademyla.dao.MemberDAO;
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
    private List<Member> activeMemberList;
    private List<Member> inactiveMemberList;
    private HashMap<Integer, Boolean> isActiveMemberHashMap;

    public MemberListDTOBuilder setCycleStartDate(LocalDate cycleStartDate){
        this.cycleStartDate = cycleStartDate;
        this.activeMemberList = new ArrayList<>();
        this.inactiveMemberList = ((MemberDAO)getDAOFactory().getDAO(Member.class)).getList();
        this.isActiveMemberHashMap = new HashMap<>();
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public MemberListDTO build() {
        MemberListDTO memberListDTO = new MemberListDTO();

        List<MemberMonthlyRegistration> memberMonthlyRegistrationList = new MemberMonthlyRegistrationDAOAccessFlow()
                                                                            .addQueryParameter(cycleStartDate)
                                                                            .getList();

        generateIsActiveHashMap();
        populateMemberLists(memberMonthlyRegistrationList);

        memberListDTO.setActiveMemberList(activeMemberList);
        memberListDTO.setInactiveMemberList(inactiveMemberList);
        memberListDTO.setIsActiveMemberHashMap(isActiveMemberHashMap);
        memberListDTO.setCycleStartDate(cycleStartDate);
        return memberListDTO;
    }

    private void generateIsActiveHashMap(){
        for(Member member : inactiveMemberList)
            isActiveMemberHashMap.put(member.getMemberID(), false);
    }

    private void populateMemberLists(List<MemberMonthlyRegistration> memberMonthlyRegistrationList){
        Iterator it = inactiveMemberList.iterator();

        /*
         * Since null Member has ID of 1, any entity less than or equal 1 is removed.
         */
        while(it.hasNext()){
            Member member = (Member) it.next();
            if(member.getMemberID() <= 1)
                it.remove();
        }

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
    }

}
