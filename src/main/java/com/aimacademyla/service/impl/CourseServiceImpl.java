package com.aimacademyla.service.impl;

import com.aimacademyla.dao.CourseDAO;
import com.aimacademyla.dao.GenericDAO;
import com.aimacademyla.dao.MemberCourseRegistrationDAO;
import com.aimacademyla.dao.SeasonDAO;
import com.aimacademyla.dao.flow.impl.ChargeDAOAccessFlow;
import com.aimacademyla.model.*;
import com.aimacademyla.model.dto.CourseRegistrationDTO;
import com.aimacademyla.model.dto.CourseRegistrationDTOListItem;
import com.aimacademyla.model.enums.BillableUnitType;
import com.aimacademyla.model.id.IDGenerationStrategy;
import com.aimacademyla.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class CourseServiceImpl extends GenericServiceImpl<Course, Integer> implements CourseService {

    private CourseDAO courseDAO;
    private SeasonService seasonService;
    private ChargeService chargeService;
    private MemberCourseRegistrationService memberCourseRegistrationService;

    @Autowired
    public CourseServiceImpl(@Qualifier("courseDAO") GenericDAO<Course, Integer> genericDAO,
                             SeasonService seasonService,
                             ChargeService chargeService,
                             MemberCourseRegistrationService memberCourseRegistrationService){
        super(genericDAO);
        this.courseDAO = (CourseDAO) genericDAO;
        this.seasonService = seasonService;
        this.chargeService = chargeService;
        this.memberCourseRegistrationService = memberCourseRegistrationService;
    }

    @Override
    public List<Course> getList(){
        List<Course> courseList = super.getList();
        Iterator it = courseList.iterator();

        while(it.hasNext()){
            Course course = (Course) it.next();
                if(course.getCourseID() == Course.OTHER_ID)
                    it.remove();
        }
        return courseList;
    }

    @Override
    public void addCourse(Course course){
        if(course.getNonMemberPricePerBillableUnit() == null)
            course.setNonMemberPricePerBillableUnit(course.getMemberPricePerBillableUnit());

        course.setBillableUnitDuration();

        courseDAO.add(course);
    }

    @Override
    public void updateCourse(CourseRegistrationDTO courseRegistrationDTO){
        Course course = courseRegistrationDTO.getCourse();

        if(course.getCourseStartDate() != null){
            Season season = seasonService.getSeason(course.getCourseStartDate());
            course.setSeason(season);
        }

        int numEnrolled = 0;

        // Have to check for null list instead of empty list because JSP returns null if list is empty
        if(courseRegistrationDTO.getCourseRegistrationDTOListItems() != null){
            numEnrolled = courseRegistrationDTO.getNumEnrolled();
            updateMemberCourseRegistrationList(courseRegistrationDTO);
        }

        course.setNumEnrolled(numEnrolled);
        course.setBillableUnitDuration();

        if(course.getNonMemberPricePerBillableUnit() == null)
            course.setNonMemberPricePerBillableUnit(course.getMemberPricePerBillableUnit());

        courseDAO.update(course);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void removeCourse(Course course){
        List<Charge> chargeList = new ChargeDAOAccessFlow()
                                        .addQueryParameter(course)
                                        .getList();
        /*
         * When all Charges associated with current Course is removed, Payment and MonthlyFinancesSummary are update accordingly
         */
        for(Charge charge : chargeList){
            chargeService.removeCharge(charge);
        }
        /*
         * By cascade, related Charge, ChargeLines, CourseSessions, MemberCourseRegistration, Attendance, and InstructorRegistration
         * should be removed
         */
        courseDAO.remove(course);
    }

    private void updateMemberCourseRegistrationList(CourseRegistrationDTO courseRegistrationDTO){
        Course course = courseRegistrationDTO.getCourse();
        Member member;

        for(CourseRegistrationDTOListItem courseRegistrationWrapperObject : courseRegistrationDTO.getCourseRegistrationDTOListItems()){
            member = courseRegistrationWrapperObject.getMember();
            MemberCourseRegistration memberCourseRegistration = memberCourseRegistrationService.getMemberCourseRegistration(member, course);

            if(memberCourseRegistration == null)
                return;

            if(courseRegistrationWrapperObject.getIsDropped())
                memberCourseRegistration.setIsEnrolled(false);

            memberCourseRegistrationService.update(memberCourseRegistration);
        }
    }

    public List<Course> getActiveList(){
        List<Course> courseList = getList();
        Iterator it = courseList.iterator();
        while(it.hasNext()){
            Course course = (Course) it.next();

            if(!course.getIsActive())
                it.remove();
        }

        return courseList;
    }

    public List<Course> getInactiveList(){
        List<Course> courseList = getList();
        Iterator it = courseList.iterator();

        while(it.hasNext()){
            Course course = (Course) it.next();

            if(course.getIsActive())
                it.remove();
        }

        return courseList;
    }
}
