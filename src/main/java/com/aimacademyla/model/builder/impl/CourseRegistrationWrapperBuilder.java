package com.aimacademyla.model.builder.impl;

import com.aimacademyla.model.Course;
import com.aimacademyla.model.Member;
import com.aimacademyla.model.MemberCourseRegistration;
import com.aimacademyla.model.builder.GenericBuilder;
import com.aimacademyla.model.enums.AIMEntityType;
import com.aimacademyla.model.wrapper.CourseRegistrationWrapper;
import com.aimacademyla.model.wrapper.CourseRegistrationWrapperListItem;
import com.aimacademyla.service.CourseService;
import com.aimacademyla.service.MemberCourseRegistrationService;
import com.aimacademyla.service.MemberService;
import com.aimacademyla.service.factory.ServiceFactory;

import java.util.ArrayList;
import java.util.List;

public class CourseRegistrationWrapperBuilder extends GenericBuilderImpl<CourseRegistrationWrapper> implements GenericBuilder<CourseRegistrationWrapper> {

    private MemberService memberService;
    private MemberCourseRegistrationService memberCourseRegistrationService;
    private CourseService courseService;

    private int courseID;

    public CourseRegistrationWrapperBuilder(ServiceFactory serviceFactory){
        super(serviceFactory);
        this.memberService = (MemberService) getServiceFactory().getService(AIMEntityType.MEMBER);
        this.memberCourseRegistrationService = (MemberCourseRegistrationService) getServiceFactory().getService(AIMEntityType.MEMBER_COURSE_REGISTRATION);
        this.courseService = (CourseService) getServiceFactory().getService(AIMEntityType.COURSE);
    }

    public CourseRegistrationWrapperBuilder setCourseID(int courseID){
        this.courseID = courseID;
        return this;
    }

    @Override
    public CourseRegistrationWrapper build() {
        CourseRegistrationWrapper courseRegistrationWrapper = new CourseRegistrationWrapper();
        Course course = courseService.get(courseID);
        List<CourseRegistrationWrapperListItem> courseRegistrationWrapperObjectList = new ArrayList<>();
        List<MemberCourseRegistration> memberCourseRegistrationList = memberCourseRegistrationService.getMemberCourseRegistrationListForCourse(course);

        for(MemberCourseRegistration memberCourseRegistration : memberCourseRegistrationList){
            if(!memberCourseRegistration.getIsEnrolled())
                continue;

            Member member = memberService.get(memberCourseRegistration.getMemberID());
            CourseRegistrationWrapperListItem courseRegistrationWrapperObject = new CourseRegistrationWrapperListItem();
            courseRegistrationWrapperObject.setIsDropped(false);
            courseRegistrationWrapperObject.setMember(member);
            courseRegistrationWrapperObjectList.add(courseRegistrationWrapperObject);
        }

        courseRegistrationWrapper.setCourse(course);
        courseRegistrationWrapper.setCourseRegistrationWrapperObjectList(courseRegistrationWrapperObjectList);

        return courseRegistrationWrapper;
    }
}
