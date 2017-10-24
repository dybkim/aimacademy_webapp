package com.aimacademyla.model.initializer.impl;

import com.aimacademyla.dao.factory.DAOFactory;
import com.aimacademyla.model.MemberCourseRegistration;
import com.aimacademyla.model.initializer.GenericDefaultValueInitializer;
import com.aimacademyla.model.composite.MemberCourseRegistrationPK;
import com.aimacademyla.service.factory.ServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

public class MemberCourseRegistrationDefaultValueInitializer extends GenericDefaultValueInitializerImpl<MemberCourseRegistration>{

    private int memberID;
    private int courseID;

    public MemberCourseRegistrationDefaultValueInitializer(DAOFactory daoFactory){
        super(daoFactory);
    }

    public MemberCourseRegistration initialize() {
        MemberCourseRegistration memberCourseRegistration = new MemberCourseRegistration();
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
