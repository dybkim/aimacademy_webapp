package com.aimacademyla.service.impl;

import com.aimacademyla.dao.GenericDAO;
import com.aimacademyla.dao.MemberDAO;
import com.aimacademyla.dao.MemberMonthlyRegistrationDAO;
import com.aimacademyla.model.*;
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
    private MonthlyFinancesSummaryService monthlyFinancesSummaryService;
    private MemberDAO memberDAO;

    @Autowired
    public MemberMonthlyRegistrationServiceImpl(@Qualifier("memberMonthlyRegistrationDAO") GenericDAO<MemberMonthlyRegistration, Integer> genericDAO,
                                                SeasonService seasonService,
                                                ChargeService chargeService,
                                                CourseService courseService,
                                                MonthlyFinancesSummaryService monthlyFinancesSummaryService,
                                                MemberDAO memberDAO){
        super(genericDAO);
        this.memberMonthlyRegistrationDAO = (MemberMonthlyRegistrationDAO) genericDAO;
        this.seasonService = seasonService;
        this.chargeService = chargeService;
        this.courseService = courseService;
        this.monthlyFinancesSummaryService = monthlyFinancesSummaryService;
        this.memberDAO = memberDAO;
    }

    @Override
    public void add(MemberMonthlyRegistration entity) {
        super.add(entity);
        Charge charge = chargeService.getChargeByMemberForCourseByDate(entity.getMemberID(), Course.OPEN_STUDY_ID, entity.getCycleStartDate());

        if(charge == null){
            charge = generateChargeForMemberMonthlyRegistration(entity);
            chargeService.add(charge);
        }
    }

    @Override
    public void update(MemberMonthlyRegistration entity) {
        super.update(entity);
        /**
         * Charge is generated if none exists
         * chargeAmount for openstudy is set as a constant value since it is not calculated per hour
         *
         * IMPORTANT: AN OPEN STUDY COURSE ENTITY MUST EXIST ALREADY IN THE DATABASE, ELSE NO ENTITY WILL BE RETURNED
         * TODO: MUST IMPLEMENT A CHECK TO SEE IF OPEN STUDY COURSE ENTITY EXISTS
         */
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


    private MemberMonthlyRegistration generateMemberMonthlyRegistrationForMonth(int memberID, LocalDate date){
        LocalDate cycleStartDate = LocalDate.of(date.getYear(), date.getMonthValue(), 1);
        Season season = seasonService.getSeason(cycleStartDate);
        MemberMonthlyRegistration memberMonthlyRegistration = new MemberMonthlyRegistration();
        memberMonthlyRegistration.setCycleStartDate(cycleStartDate);
        memberMonthlyRegistration.setMemberID(memberID);
        memberMonthlyRegistration.setSeasonID(season.getSeasonID());

        return memberMonthlyRegistration;
    }

    private List<MemberMonthlyRegistration> generateMemberMonthlyRegistrationListForMonth(List<Integer> memberIDList, LocalDate date){
        List<MemberMonthlyRegistration> memberMonthlyRegistrationList = new ArrayList<>();

        for(int memberID :  memberIDList)
            memberMonthlyRegistrationList.add(generateMemberMonthlyRegistrationForMonth(memberID, date));

        return memberMonthlyRegistrationList;
    }

    private Charge generateChargeForMemberMonthlyRegistration(MemberMonthlyRegistration memberMonthlyRegistration){
        LocalDate cycleStartDate = memberMonthlyRegistration.getCycleStartDate();
        Course openStudy = courseService.get(Course.OPEN_STUDY_ID);
        MonthlyFinancesSummary monthlyFinancesSummary = monthlyFinancesSummaryService.getMonthlyFinancesSummary(cycleStartDate);

        Charge charge = new Charge();
        charge.setDiscountAmount(BigDecimal.valueOf(0));
        charge.setChargeAmount(openStudy.getPricePerHour());
        charge.setCycleStartDate(cycleStartDate);
        charge.setMemberID(memberMonthlyRegistration.getMemberID());
        charge.setDescription(openStudy.getCourseName() + " (" + openStudy.getCourseType() + ")");
        charge.setCourseID(openStudy.getCourseID());
        charge.setMonthlyFinancesSummaryID(monthlyFinancesSummary.getMonthlyFinancesSummaryID());

        chargeService.add(charge);

        return charge;
    }
}
