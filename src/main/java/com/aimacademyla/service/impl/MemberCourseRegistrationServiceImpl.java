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
    public List<MemberCourseRegistration> getMemberCourseRegistrationsForCourse(Course course) {
        return memberCourseRegistrationDAO.getMemberCourseRegistrationListForCourse(course);
    }

    @Override
    public void update(List<MemberCourseRegistration> memberCourseRegistrationList){

    }

}
