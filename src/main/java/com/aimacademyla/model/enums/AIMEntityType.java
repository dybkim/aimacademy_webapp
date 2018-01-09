package com.aimacademyla.model.enums;

import com.aimacademyla.model.*;

public enum AIMEntityType {
    ATTENDANCE,
    CHARGE_LINE,
    CHARGE,
    COURSE,
    COURSE_SESSION,
    EMPLOYEE,
    MEMBER_COURSE_REGISTRATION,
    MEMBER_MONTHLY_REGISTRATION,
    MEMBER,
    MONTHLY_FINANCES_SUMMARY,
    PAYMENT,
    SEASON,
    USER,
    AUTHORITY,
    INSTRUCTOR_COURSE_REGISTRATION,
    ENTITY_NOT_FOUND;

    AIMEntityType getAIMEntityType(Object object){
        if(object instanceof Attendance)
            return ATTENDANCE;

        else if(object instanceof ChargeLine)
            return CHARGE_LINE;

        else if(object instanceof Charge)
            return CHARGE;

        else if(object instanceof CourseSession)
            return COURSE_SESSION;

        else if(object instanceof Course)
            return COURSE;

        else if(object instanceof Employee)
            return EMPLOYEE;

        else if(object instanceof MemberCourseRegistration)
            return MEMBER_COURSE_REGISTRATION;

        else if(object instanceof MemberMonthlyRegistration)
            return MEMBER_MONTHLY_REGISTRATION;

        else if(object instanceof Member)
            return MEMBER;

        else if(object instanceof MonthlyFinancesSummary)
            return MONTHLY_FINANCES_SUMMARY;

        else if(object instanceof Payment)
            return PAYMENT;

        else if(object instanceof Season)
            return SEASON;

        else if(object instanceof User)
            return USER;

        else if(object instanceof Authority)
            return AUTHORITY;

        else if(object instanceof InstructorCourseRegistration)
            return INSTRUCTOR_COURSE_REGISTRATION;

        return ENTITY_NOT_FOUND;
    }
}
