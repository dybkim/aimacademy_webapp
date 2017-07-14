package com.aimacademyla.model.factory;

import com.aimacademyla.model.MemberCourseRegistration;
import com.aimacademyla.model.composite.MemberCourseRegistrationPK;

import java.util.Date;

/**
 * Created by davidkim on 7/5/17.
 */
public class MemberCourseRegistrationFactory {

    public static MemberCourseRegistration generate(int memberID, int courseID){
        MemberCourseRegistration memberCourseRegistration = new MemberCourseRegistration();
        memberCourseRegistration.setMemberCourseRegistrationPK(new MemberCourseRegistrationPK(memberID, courseID));
        memberCourseRegistration.setMemberID(memberID);
        memberCourseRegistration.setCourseID(courseID);
        memberCourseRegistration.setDateRegistered(new Date());
        memberCourseRegistration.setEnrolled(true);

        return memberCourseRegistration;
    }
}
