package com.aimacademyla.model.builder.impl;

import com.aimacademyla.model.Course;
import com.aimacademyla.model.Member;
import com.aimacademyla.model.builder.GenericBuilder;
import com.aimacademyla.model.wrapper.CourseRegistrationWrapper;
import com.aimacademyla.model.wrapper.CourseRegistrationWrapperObject;
import com.aimacademyla.service.CourseService;
import com.aimacademyla.service.MemberService;

import java.util.ArrayList;
import java.util.List;

public class CourseRegistrationWrapperBuilder implements GenericBuilder<CourseRegistrationWrapper> {

    private MemberService memberService;
    private CourseService courseService;

    private int courseID;

    private CourseRegistrationWrapper courseRegistrationWrapper;

    private CourseRegistrationWrapperBuilder(){}

    public CourseRegistrationWrapperBuilder(MemberService memberService, CourseService courseService){
        this.memberService = memberService;
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
        List<Member> activeMemberList = memberService.getActiveMembersByCourse(course);
        CourseRegistrationWrapperObject courseRegistrationWrapperObject;
        List<CourseRegistrationWrapperObject> courseRegistrationWrapperObjectList = new ArrayList<>();

        for (Member member : activeMemberList) {
            courseRegistrationWrapperObject = new CourseRegistrationWrapperObject();
            courseRegistrationWrapperObject.setMember(member);
            courseRegistrationWrapperObject.setIsDropped(false);
            courseRegistrationWrapperObjectList.add(courseRegistrationWrapperObject);
        }

        CourseRegistrationWrapper courseRegistrationWrapper = new CourseRegistrationWrapper();
        courseRegistrationWrapper.setCourse(course);
        courseRegistrationWrapper.setCourseRegistrationWrapperObjectList(courseRegistrationWrapperObjectList);

        return courseRegistrationWrapper;
    }
}
