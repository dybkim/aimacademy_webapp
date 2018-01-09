package com.aimacademyla.service;

import com.aimacademyla.model.Course;
import com.aimacademyla.model.Member;
import com.aimacademyla.model.MemberCourseRegistration;

import java.util.List;

/**
 * Created by davidkim on 2/9/17.
 */
public interface MemberCourseRegistrationService extends GenericService<MemberCourseRegistration, Integer>{

    MemberCourseRegistration getMemberCourseRegistration(Member member, Course course);
    void addMemberCourseRegistration(MemberCourseRegistration memberCourseRegistration);
    void updateMemberCourseRegistration(MemberCourseRegistration memberCourseRegistration);
    void removeMemberCourseRegistration(MemberCourseRegistration memberCourseRegistration);

}
