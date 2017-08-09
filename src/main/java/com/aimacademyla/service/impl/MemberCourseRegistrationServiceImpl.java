package com.aimacademyla.service.impl;

import com.aimacademyla.dao.CourseDAO;
import com.aimacademyla.dao.GenericDAO;
import com.aimacademyla.dao.MemberCourseRegistrationDAO;
import com.aimacademyla.model.Course;
import com.aimacademyla.model.Member;
import com.aimacademyla.model.MemberCourseRegistration;
import com.aimacademyla.model.composite.MemberCourseRegistrationPK;
import com.aimacademyla.service.MemberCourseRegistrationService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
    public MemberCourseRegistration get(MemberCourseRegistrationPK memberCourseRegistrationPK){
        MemberCourseRegistration memberCourseRegistration = memberCourseRegistrationDAO.get(memberCourseRegistrationPK);

        if(memberCourseRegistration == null)
            memberCourseRegistration = generateMemberCourseRegistration(memberCourseRegistrationPK.getMemberID(), memberCourseRegistrationPK.getCourseID());

        return memberCourseRegistration;
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
        MemberCourseRegistration memberCourseRegistration = memberCourseRegistrationDAO.get(memberID, courseID);

        if(memberCourseRegistration == null)
            memberCourseRegistration = generateMemberCourseRegistration(memberID, courseID);

        return memberCourseRegistration;
    }

    @Override
    public List<MemberCourseRegistration> getMemberCourseRegistrationListForMember(Member member){
        return memberCourseRegistrationDAO.getMemberCourseRegistrationListForMember(member);
    }

    @Override
    public List<MemberCourseRegistration> getMemberCourseRegistrationListForCourse(Course course) {
        return memberCourseRegistrationDAO.getMemberCourseRegistrationListForCourse(course);
    }

    @Override
    public void update(List<MemberCourseRegistration> memberCourseRegistrationList){
        for(MemberCourseRegistration memberCourseRegistration : memberCourseRegistrationList)
            memberCourseRegistrationDAO.update(memberCourseRegistration);
    }

    private MemberCourseRegistration generateMemberCourseRegistration(int memberID, int courseID){
        MemberCourseRegistration memberCourseRegistration = new MemberCourseRegistration();
        memberCourseRegistration.setMemberCourseRegistrationPK(new MemberCourseRegistrationPK(memberID, courseID));
        memberCourseRegistration.setMemberID(memberID);
        memberCourseRegistration.setCourseID(courseID);
        memberCourseRegistration.setDateRegistered(LocalDate.now());
        memberCourseRegistration.setIsEnrolled(true);

        return memberCourseRegistration;
    }

}
