package com.aimacademyla.model.builder.impl;

import com.aimacademyla.model.Course;
import com.aimacademyla.model.Member;
import com.aimacademyla.model.MemberCourseRegistration;
import com.aimacademyla.model.builder.GenericBuilder;
import com.aimacademyla.model.wrapper.CourseRegistrationWrapper;
import com.aimacademyla.model.wrapper.CourseRegistrationWrapperObject;
import com.aimacademyla.service.CourseService;
import com.aimacademyla.service.MemberCourseRegistrationService;
import com.aimacademyla.service.MemberService;
import org.springframework.beans.factory.annotation.Configurable;

import java.util.ArrayList;
import java.util.List;

public class CourseRegistrationWrapperBuilder implements GenericBuilder<CourseRegistrationWrapper> {

    private MemberService memberService;
    private MemberCourseRegistrationService memberCourseRegistrationService;
    private CourseService courseService;

    private int courseID;

    private CourseRegistrationWrapper courseRegistrationWrapper;

    private CourseRegistrationWrapperBuilder(){}

    public CourseRegistrationWrapperBuilder(MemberService memberService, MemberCourseRegistrationService memberCourseRegistrationService, CourseService courseService){
        this.memberService = memberService;
        this.memberCourseRegistrationService = memberCourseRegistrationService;
        this.courseService = courseService;

        courseRegistrationWrapper = new CourseRegistrationWrapper();
    }

    public CourseRegistrationWrapperBuilder setCourseID(int courseID){
        this.courseID = courseID;
        return this;
    }

    @Override
    public CourseRegistrationWrapper build() {
        Course course = courseService.get(courseID);
        List<CourseRegistrationWrapperObject> courseRegistrationWrapperObjectList = new ArrayList<>();
        List<MemberCourseRegistration> memberCourseRegistrationList = memberCourseRegistrationService.getMemberCourseRegistrationListForCourse(course);

        for(MemberCourseRegistration memberCourseRegistration : memberCourseRegistrationList){
            if(!memberCourseRegistration.getIsEnrolled())
                continue;

            Member member = memberService.get(memberCourseRegistration.getMemberID());
            CourseRegistrationWrapperObject courseRegistrationWrapperObject = new CourseRegistrationWrapperObject();
            courseRegistrationWrapperObject.setIsDropped(false);
            courseRegistrationWrapperObject.setMember(member);
            courseRegistrationWrapperObjectList.add(courseRegistrationWrapperObject);
        }

        CourseRegistrationWrapper courseRegistrationWrapper = new CourseRegistrationWrapper();
        courseRegistrationWrapper.setCourse(course);
        courseRegistrationWrapper.setCourseRegistrationWrapperObjectList(courseRegistrationWrapperObjectList);

        return courseRegistrationWrapper;
    }
}
