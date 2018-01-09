package com.aimacademyla.model.initializer.impl;

import com.aimacademyla.dao.factory.DAOFactory;
import com.aimacademyla.model.Course;
import com.aimacademyla.model.Member;
import com.aimacademyla.model.MemberCourseRegistration;
import java.time.LocalDate;

public class MemberCourseRegistrationDefaultValueInitializer extends GenericDefaultValueInitializerImpl<MemberCourseRegistration>{

    private Member member;
    private Course course;

    public MemberCourseRegistrationDefaultValueInitializer(DAOFactory daoFactory){
        super(daoFactory);
    }

    public MemberCourseRegistration initialize() {
        MemberCourseRegistration memberCourseRegistration = new MemberCourseRegistration();
        memberCourseRegistration.setMember(member);
        memberCourseRegistration.setCourse(course);
        memberCourseRegistration.setDateRegistered(LocalDate.now());
        memberCourseRegistration.setIsEnrolled(true);
        return memberCourseRegistration;
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
