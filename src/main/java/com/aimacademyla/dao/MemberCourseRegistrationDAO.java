package com.aimacademyla.dao;

import com.aimacademyla.model.Course;
import com.aimacademyla.model.Member;
import com.aimacademyla.model.MemberCourseRegistration;
import com.aimacademyla.model.composite.MemberCourseRegistrationPK;

import java.util.List;

/**
 * Created by davidkim on 2/9/17.
 */
public interface MemberCourseRegistrationDAO extends GenericDAO<MemberCourseRegistration, MemberCourseRegistrationPK>{

    MemberCourseRegistration get(int memberID, int courseID);

    List<MemberCourseRegistration> getMemberCourseRegistrationListForMember(Member member);

    List<MemberCourseRegistration> getMemberCourseRegistrationListForCourse(Course course);
}
