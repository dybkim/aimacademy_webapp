package com.aimacademyla.service.impl;

import com.aimacademyla.dao.GenericDAO;
import com.aimacademyla.dao.MemberDAO;
import com.aimacademyla.dao.MemberMonthlyRegistrationDAO;
import com.aimacademyla.dao.factory.DAOFactory;
import com.aimacademyla.model.*;
import com.aimacademyla.model.enums.AIMEntityType;
import com.aimacademyla.model.initializer.impl.MemberMonthlyRegistrationDefaultValueInitializer;
import com.aimacademyla.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by davidkim on 6/19/17.
 */

@Service
public class MemberMonthlyRegistrationServiceImpl extends GenericServiceImpl<MemberMonthlyRegistration, Integer> implements MemberMonthlyRegistrationService {

    private MemberMonthlyRegistrationDAO memberMonthlyRegistrationDAO;
    private ChargeService chargeService;
    private CourseService courseService;
    private MemberDAO memberDAO;

    private final AIMEntityType AIM_ENTITY_TYPE = AIMEntityType.MEMBER_MONTHLY_REGISTRATION;

    @Autowired
    public MemberMonthlyRegistrationServiceImpl(@Qualifier("memberMonthlyRegistrationDAO") GenericDAO<MemberMonthlyRegistration, Integer> genericDAO,
                                                ChargeService chargeService,
                                                CourseService courseService,
                                                MemberDAO memberDAO){
        super(genericDAO);
        this.memberMonthlyRegistrationDAO = (MemberMonthlyRegistrationDAO) genericDAO;
        this.chargeService = chargeService;
        this.courseService = courseService;
        this.memberDAO = memberDAO;
    }

    /**
     * Charge is generated if none exists
     * chargeAmount for OpenStudy is set as a constant value since it is not calculated per hour
     *
     * IMPORTANT: AN OPEN STUDY COURSE ENTITY MUST EXIST ALREADY IN THE DATABASE, ELSE NO ENTITY WILL BE RETURNED
     * TODO: MUST IMPLEMENT A CHECK TO SEE IF OPEN STUDY COURSE ENTITY EXISTS
     */
    @Override
    public void add(MemberMonthlyRegistration memberMonthlyRegistration) {
        super.add(memberMonthlyRegistration);

        Charge charge = chargeService.getChargeByMemberForCourseByDate(memberMonthlyRegistration.getMemberID(), Course.OPEN_STUDY_ID, memberMonthlyRegistration.getCycleStartDate());
        charge.setChargeAmount(memberMonthlyRegistration.getMembershipCharge());
        charge.setBillableUnitsBilled(BigDecimal.ONE);
        chargeService.add(charge);
    }

    @Override
    public void update(MemberMonthlyRegistration memberMonthlyRegistration) {
        super.update(memberMonthlyRegistration);

        Charge charge = chargeService.getChargeByMemberForCourseByDate(memberMonthlyRegistration.getMemberID(), Course.OPEN_STUDY_ID, memberMonthlyRegistration.getCycleStartDate());
        charge.setChargeAmount(memberMonthlyRegistration.getMembershipCharge());
        charge.setBillableUnitsBilled(BigDecimal.ONE);
        chargeService.update(charge);
    }

    @Override
    public void remove(MemberMonthlyRegistration memberMonthlyRegistration) {
        Charge charge = chargeService.getChargeByMemberForCourseByDate(memberMonthlyRegistration.getMemberID(), Course.OPEN_STUDY_ID, memberMonthlyRegistration.getCycleStartDate());

        if(charge != null)
            chargeService.remove(charge);

        super.remove(memberMonthlyRegistration);
    }

    @Override
    public MemberMonthlyRegistration getMemberMonthlyRegistrationForMemberByDate(Member member, LocalDate date) {
        return memberMonthlyRegistrationDAO.getMemberMonthlyRegistrationForMemberByDate(member, date);
    }

    @Override
    public List<MemberMonthlyRegistration> getMemberMonthlyRegistrationList(LocalDate date) {
        return memberMonthlyRegistrationDAO.getMemberMonthlyRegistrationList(date);
    }

    @Override
    public void addMemberMonthlyRegistrationList(List<MemberMonthlyRegistration> memberMonthlyRegistrationList){
        for(MemberMonthlyRegistration memberMonthlyRegistration : memberMonthlyRegistrationList)
            add(memberMonthlyRegistration);
    }

    @Override
    public void updateMemberMonthlyRegistrationList(List<MemberMonthlyRegistration> memberMonthlyRegistrationList){
        for(MemberMonthlyRegistration memberMonthlyRegistration : memberMonthlyRegistrationList)
            update(memberMonthlyRegistration);
    }

    @Override
    public void removeMemberMonthlyRegistrationList(List<MemberMonthlyRegistration> memberMonthlyRegistrationList){
        for(MemberMonthlyRegistration memberMonthlyRegistration : memberMonthlyRegistrationList)
            remove(memberMonthlyRegistration);
    }

    @Override
    public List<Member> getActiveMembers() {return getActiveMembersForMonth(LocalDate.now());}

    @Override
    public List<Member> getActiveMembersForMonth(LocalDate date){
        List<MemberMonthlyRegistration> memberMonthlyRegistrationList = getMemberMonthlyRegistrationList(date);
        List<Member> memberList = new ArrayList<>();

        for(MemberMonthlyRegistration memberMonthlyRegistration : memberMonthlyRegistrationList){
            memberList.add(memberDAO.get(memberMonthlyRegistration.getMemberID()));
        }

        return memberList;
    }

    private List<MemberMonthlyRegistration> generateMemberMonthlyRegistrationListForMonth(List<Integer> memberIDList, LocalDate date){
        List<MemberMonthlyRegistration> memberMonthlyRegistrationList = new ArrayList<>();

        for(int memberID :  memberIDList)
            memberMonthlyRegistrationList.add(new MemberMonthlyRegistrationDefaultValueInitializer(getDaoFactory()).setMemberID(memberID).setLocalDate(date).initialize());

        return memberMonthlyRegistrationList;
    }

    @Override
    public AIMEntityType getAIMEntityType(){
        return AIM_ENTITY_TYPE;
    }
}
