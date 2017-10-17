package com.aimacademyla.service.impl;

import com.aimacademyla.dao.CourseDAO;
import com.aimacademyla.dao.GenericDAO;
import com.aimacademyla.dao.MemberDAO;
import com.aimacademyla.dao.MemberMonthlyRegistrationDAO;
import com.aimacademyla.model.*;
import com.aimacademyla.model.builder.initializer.impl.ChargeDefaultValueInitializer;
import com.aimacademyla.model.builder.initializer.impl.MemberMonthlyRegistrationDefaultValueInitializer;
import com.aimacademyla.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by davidkim on 6/19/17.
 */

@Service
public class MemberMonthlyRegistrationServiceImpl extends GenericServiceImpl<MemberMonthlyRegistration, Integer> implements MemberMonthlyRegistrationService {

    private MemberMonthlyRegistrationDAO memberMonthlyRegistrationDAO;
    private SeasonService seasonService;
    private ChargeService chargeService;
    private CourseService courseService;
    private CourseDAO courseDAO;
    private MonthlyFinancesSummaryService monthlyFinancesSummaryService;
    private MemberDAO memberDAO;

    private final AIMEntityType AIM_ENTITY_TYPE = AIMEntityType.MEMBER_MONTHLY_REGISTRATION;

    @Autowired
    public MemberMonthlyRegistrationServiceImpl(@Qualifier("memberMonthlyRegistrationDAO") GenericDAO<MemberMonthlyRegistration, Integer> genericDAO,
                                                SeasonService seasonService,
                                                ChargeService chargeService,
                                                CourseService courseService,
                                                CourseDAO courseDAO,
                                                MonthlyFinancesSummaryService monthlyFinancesSummaryService,
                                                MemberDAO memberDAO){
        super(genericDAO);
        this.memberMonthlyRegistrationDAO = (MemberMonthlyRegistrationDAO) genericDAO;
        this.seasonService = seasonService;
        this.chargeService = chargeService;
        this.courseService = courseService;
        this.courseDAO = courseDAO;
        this.monthlyFinancesSummaryService = monthlyFinancesSummaryService;
        this.memberDAO = memberDAO;
    }

    @Override
    public void add(MemberMonthlyRegistration entity) {
        super.add(entity);
        Charge charge = chargeService.getChargeByMemberForCourseByDate(entity.getMemberID(), Course.OPEN_STUDY_ID, entity.getCycleStartDate());
        Course openStudy = courseService.get(Course.OPEN_STUDY_ID);
        charge.setChargeAmount(openStudy.getPricePerHour());
        chargeService.add(charge);
    }

    /**
     * Charge is generated if none exists
     * chargeAmount for openstudy is set as a constant value since it is not calculated per hour
     *
     * IMPORTANT: AN OPEN STUDY COURSE ENTITY MUST EXIST ALREADY IN THE DATABASE, ELSE NO ENTITY WILL BE RETURNED
     * TODO: MUST IMPLEMENT A CHECK TO SEE IF OPEN STUDY COURSE ENTITY EXISTS
     */
    @Override
    public void update(MemberMonthlyRegistration entity) {
        super.update(entity);

        Course course = courseService.get(Course.OPEN_STUDY_ID);
        Charge charge = chargeService.getChargeByMemberForCourseByDate(entity.getMemberID(), Course.OPEN_STUDY_ID, entity.getCycleStartDate());
        charge.setChargeAmount(course.getPricePerHour());
        chargeService.update(charge);
    }

    @Override
    public void remove(MemberMonthlyRegistration entity) {
        super.remove(entity);
        Charge charge = chargeService.getChargeByMemberForCourseByDate(entity.getMemberID(), Course.OPEN_STUDY_ID, entity.getCycleStartDate());

        if(charge != null)
            chargeService.remove(charge);
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
            memberMonthlyRegistrationList.add(new MemberMonthlyRegistrationDefaultValueInitializer(seasonService).setMemberID(memberID).setLocalDate(date).initialize());

        return memberMonthlyRegistrationList;
    }

    @Override
    public AIMEntityType getAIMEntityType(){
        return AIM_ENTITY_TYPE;
    }
}
