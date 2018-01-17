package com.aimacademyla.model.initializer.impl;

import com.aimacademyla.model.Course;
import com.aimacademyla.model.Member;
import com.aimacademyla.model.MemberCourseRegistration;
import com.aimacademyla.model.builder.entity.MemberCourseRegistrationBuilder;

import java.time.LocalDate;

public class MemberCourseRegistrationDefaultValueInitializer extends GenericDefaultValueInitializerImpl<MemberCourseRegistration>{

    private Member member;
    private Course course;

    public MemberCourseRegistration initialize() {

        return new MemberCourseRegistrationBuilder()
                    .setMember(member)
                    .setCourse(course)
                    .setDateRegistered(LocalDate.now())
                    .setIsEnrolled(true)
                    .build();
    }

    public MemberCourseRegistrationDefaultValueInitializer setMember(Member member){
        this.member = member;
        return this;
    }

    public MemberCourseRegistrationDefaultValueInitializer setCourse(Course course){
        this.course = course;
        return this;
    }
}
