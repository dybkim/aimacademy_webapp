package com.aimacademyla.api.slack.service;

import com.aimacademyla.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ServiceFactory {

    @Autowired
    private static MemberService memberService;

    @Autowired
    private static CourseService courseService;

    @Autowired
    private static CourseSessionService courseSessionService;

    @Autowired
    private static EmployeeService employeeService;

    @Autowired
    private static ChargeService chargeService;

    public enum AIMEntity{
        MEMBER,
        COURSE,
        COURSE_SESSION,
        EMPLOYEE,
        CHARGE
    }

    public static GenericService getService(AIMEntity entity){

        switch(entity){
            case MEMBER:
                return memberService;
            case COURSE:
                return courseService;
            case COURSE_SESSION:
                return courseSessionService;
            case EMPLOYEE:
                return employeeService;
            case CHARGE:
                return chargeService;
            default:
                break;
        }

        return null;
    }

    public static void setMemberService(MemberService memberService) {
        ServiceFactory.memberService = memberService;
    }

    public static void setCourseService(CourseService courseService) {
        ServiceFactory.courseService = courseService;
    }

    public static void setCourseSessionService(CourseSessionService courseSessionService) {
        ServiceFactory.courseSessionService = courseSessionService;
    }

    public static void setEmployeeService(EmployeeService employeeService) {
        ServiceFactory.employeeService = employeeService;
    }

    public static void setChargeService(ChargeService chargeService) {
        ServiceFactory.chargeService = chargeService;
    }
}
