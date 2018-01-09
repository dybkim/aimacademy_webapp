package com.aimacademyla.controller.course.rest;

import com.aimacademyla.exception.NullEntityCollectionException;
import com.aimacademyla.model.*;
import com.aimacademyla.model.builder.entity.MemberCourseRegistrationBuilder;
import com.aimacademyla.model.response.GenericResponse;
import com.aimacademyla.service.CourseService;
import com.aimacademyla.service.CourseSessionService;
import com.aimacademyla.service.MemberCourseRegistrationService;
import com.aimacademyla.service.MemberService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by davidkim on 7/7/17.
 */
@Controller
@RequestMapping("/admin/courseList/rest")
public class CourseResources {

    private MemberService memberService;
    private CourseSessionService courseSessionService;
    private CourseService courseService;
    private MemberCourseRegistrationService memberCourseRegistrationService;

    private static final Logger logger = LogManager.getLogger(CourseResources.class.getName());

    @Autowired
    public CourseResources(MemberService memberService,
                           CourseService courseService,
                           CourseSessionService courseSessionService,
                           MemberCourseRegistrationService memberCourseRegistrationService){
        this.memberService = memberService;
        this.courseService = courseService;
        this.courseSessionService = courseSessionService;
        this.memberCourseRegistrationService = memberCourseRegistrationService;
    }

    @RequestMapping(value="/{courseID}/validateAddCourseSession")
    @ResponseBody
    public ResponseEntity<GenericResponse> validateAddCourseSession(@PathVariable("courseID") int courseID){
        Course course = courseService.get(courseID);
        course = courseService.loadCollections(course);
        List<MemberCourseRegistration> memberCourseRegistrationList = new ArrayList<>(course.getMemberCourseRegistrationSet());

        if(memberCourseRegistrationList.size() == 0)
            return new ResponseEntity<>(new GenericResponse("Unable to create course session", "Cannot add course sessions until members are registered to the course!",HttpStatus.INTERNAL_SERVER_ERROR.value()) {
            }, HttpStatus.INTERNAL_SERVER_ERROR);

        return new ResponseEntity<>(new GenericResponse("OK", "NO_ERROR", HttpStatus.OK.value()){},HttpStatus.OK);
    }

    @RequestMapping(value="/deleteCourse/{courseID}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<GenericResponse> deleteCourse(@PathVariable("courseID") int courseID){
        Course course = courseService.get(courseID);
        courseService.removeCourse(course);

        return new ResponseEntity<>(new GenericResponse("OK", "NO_ERROR", HttpStatus.OK.value()){},HttpStatus.OK);
    }

    @RequestMapping(value="/{courseID}/submitRegistrationList", method=RequestMethod.POST)
    public String submitRegistrationList(@PathVariable("courseID") int courseID, @RequestBody List<Integer> memberIDList){
        Course course = courseService.get(courseID);

        for(Integer memberID : memberIDList){
            Member member = memberService.get(memberID);
            MemberCourseRegistration memberCourseRegistration = new MemberCourseRegistrationBuilder()
                                                                    .setMember(member)
                                                                    .setCourse(course)
                                                                    .setIsEnrolled(true)
                                                                    .setDateRegistered(LocalDate.now())
                                                                    .build();

            memberCourseRegistrationService.addMemberCourseRegistration(memberCourseRegistration);
        }

        return "redirect:/admin/courseList/courseInfo/" + courseID;
    }

    @RequestMapping(value="/getCourseSession/{courseSessionID}")
    @ResponseBody
    public List<Member> getCourseSession(@PathVariable("courseSessionID") int courseSessionID) {
        CourseSession courseSession = courseSessionService.get(courseSessionID);
        courseSession = courseSessionService.loadCollections(courseSession);
        List<Member> memberList = new ArrayList<>();

        for(Attendance attendance : courseSession.getAttendanceMap().values()){
            if(attendance.getWasPresent()) {
                Member member = attendance.getMember();
                memberList.add(member);
            }
        }

        return memberList;
    }
}
