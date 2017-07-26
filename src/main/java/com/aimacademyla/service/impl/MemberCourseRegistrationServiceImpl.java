package com.aimacademyla.service.impl;

import com.aimacademyla.dao.CourseDAO;
import com.aimacademyla.dao.GenericDAO;
import com.aimacademyla.dao.MemberCourseRegistrationDAO;
import com.aimacademyla.model.Course;
import com.aimacademyla.model.MemberCourseRegistration;
import com.aimacademyla.model.composite.MemberCourseRegistrationPK;
import com.aimacademyla.service.MemberCourseRegistrationService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by davidkim on 2/13/17.
 */

@Service
public class MemberCourseRegistrationServiceImpl extends GenericServiceImpl<MemberCourseRegistration, MemberCourseRegistrationPK> implements MemberCourseRegistrationService {

    private MemberCourseRegistrationDAO memberCourseRegistrationDAO;
    private CourseDAO courseDAO;

    public MemberCourseRegistrationServiceImpl(@Qualifier("memberCourseRegistrationDAO") GenericDAO<MemberCourseRegistration, MemberCourseRegistrationPK> genericDAO,
                                               CourseDAO courseDAO){
        super(genericDAO);
        this.memberCourseRegistrationDAO = (MemberCourseRegistrationDAO) genericDAO;
        this.courseDAO = courseDAO;
    }

    @Override
    public void add(MemberCourseRegistration memberCourseRegistration){
        memberCourseRegistrationDAO.add(memberCourseRegistration);
        Course course = courseDAO.get(memberCourseRegistration.getCourseID());
        course.setNumEnrolled(course.getNumEnrolled() + 1);
        courseDAO.update(course);
    }

    @Override
    public void update(MemberCourseRegistration memberCourseRegistration){
        Course course = courseDAO.get(memberCourseRegistration.getCourseID());
        int numEnrolled = course.getNumEnrolled();
        MemberCourseRegistration persistedMemberCourseRegistration = memberCourseRegistrationDAO.get(memberCourseRegistration.getMemberID(), memberCourseRegistration.getCourseID());

        if(memberCourseRegistration.getIsEnrolled() && !persistedMemberCourseRegistration.getIsEnrolled())
            course.setNumEnrolled(numEnrolled + 1);

        else if(!memberCourseRegistration.getIsEnrolled() && persistedMemberCourseRegistration.getIsEnrolled())
            course.setNumEnrolled(numEnrolled - 1);

        memberCourseRegistrationDAO.update(memberCourseRegistration);
        courseDAO.update(course);
    }

    @Override
    public void remove(MemberCourseRegistration memberCourseRegistration){
        memberCourseRegistrationDAO.remove(memberCourseRegistration);
        Course course = courseDAO.get(memberCourseRegistration.getCourseID());
        course.setNumEnrolled(course.getNumEnrolled() - 1);
        courseDAO.update(course);
    }

    @Override
    public MemberCourseRegistration get(int memberID, int courseID){
        return memberCourseRegistrationDAO.get(memberID, courseID);
    }

    @Override
    public List<MemberCourseRegistration> getMemberCourseRegistrationsForCourse(Course course) {
        return memberCourseRegistrationDAO.getMemberCourseRegistrationListForCourse(course);
    }

    @Override
    public void update(List<MemberCourseRegistration> memberCourseRegistrationList){
        for(MemberCourseRegistration memberCourseRegistration : memberCourseRegistrationList)
            memberCourseRegistrationDAO.update(memberCourseRegistration);
    }

}
