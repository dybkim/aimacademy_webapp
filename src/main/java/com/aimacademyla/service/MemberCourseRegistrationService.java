package com.aimacademyla.service;

import com.aimacademyla.model.Course;
import com.aimacademyla.model.Member;
import com.aimacademyla.model.MemberCourseRegistration;
import com.aimacademyla.model.composite.MemberCourseRegistrationPK;

import java.util.List;

/**
 * Created by davidkim on 2/9/17.
 */
public interface MemberCourseRegistrationService extends GenericService<MemberCourseRegistration, MemberCourseRegistrationPK>{

    MemberCourseRegistration get(int memberID, int courseID);

    List<MemberCourseRegistration> getMemberCourseRegistrationListForMember(Member member);

    List<MemberCourseRegistration> getMemberCourseRegistrationListForCourse(Course course);

    void update(List<MemberCourseRegistration> memberCourseRegistrationList);

}
