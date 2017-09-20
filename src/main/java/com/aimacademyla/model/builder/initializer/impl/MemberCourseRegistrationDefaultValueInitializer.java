package com.aimacademyla.model.builder.initializer.impl;

import com.aimacademyla.model.MemberCourseRegistration;
import com.aimacademyla.model.builder.initializer.GenericDefaultValueInitializer;
import com.aimacademyla.model.composite.MemberCourseRegistrationPK;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

public class MemberCourseRegistrationDefaultValueInitializer implements GenericDefaultValueInitializer<MemberCourseRegistration>{

    private MemberCourseRegistration memberCourseRegistration;

    private int memberID;
    private int courseID;
    
    public MemberCourseRegistrationDefaultValueInitializer(){
        memberCourseRegistration = new MemberCourseRegistration();
    }

    @Override
    public MemberCourseRegistration initialize() {
        memberCourseRegistration.setMemberCourseRegistrationPK(new MemberCourseRegistrationPK(memberID, courseID));
        memberCourseRegistration.setMemberID(memberID);
        memberCourseRegistration.setCourseID(courseID);
        memberCourseRegistration.setDateRegistered(LocalDate.now());
        memberCourseRegistration.setIsEnrolled(true);
        return memberCourseRegistration;
    }

    public MemberCourseRegistrationDefaultValueInitializer setMemberID(int memberID) {
        this.memberID = memberID;
        return this;
    }

    public MemberCourseRegistrationDefaultValueInitializer setCourseID(int courseID) {
        this.courseID = courseID;
        return this;
    }
}
