package com.aimacademyla.controller.course.rest;

import com.aimacademyla.controller.GenericResponse;
import com.aimacademyla.model.*;
import com.aimacademyla.model.builder.impl.CourseRegistrationWrapperBuilder;
import com.aimacademyla.model.composite.MemberCourseRegistrationPK;
import com.aimacademyla.model.wrapper.CourseRegistrationWrapper;
import com.aimacademyla.service.*;
import com.aimacademyla.service.factory.ServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by davidkim on 7/7/17.
 */
@Controller
@RequestMapping("/admin/courseList/rest")
public class CourseResources {

    private ServiceFactory serviceFactory;

    private MemberService memberService;
    private CourseService courseService;
    private MemberCourseRegistrationService memberCourseRegistrationService;
    private AttendanceService attendanceService;

    @Autowired
    public CourseResources(ServiceFactory serviceFactory,
                           MemberService memberService,
                           CourseService courseService,
                           MemberCourseRegistrationService memberCourseRegistrationService,
                           AttendanceService attendanceService){
        this.serviceFactory = serviceFactory;
        this.memberService = memberService;
        this.courseService = courseService;
        this.memberCourseRegistrationService = memberCourseRegistrationService;
        this.attendanceService = attendanceService;
    }

    @RequestMapping(value="/{courseID}/getCourseRegistration")
    @ResponseBody
    public CourseRegistrationWrapper getCourseRegistrationWrapper(@PathVariable("courseID") int courseID){
        return new CourseRegistrationWrapperBuilder(serviceFactory).setCourseID(courseID).build();
    }

    @RequestMapping(value="/{courseID}/validateAddCourseSession")
    @ResponseBody
    public ResponseEntity<GenericResponse> validateAddCourseSession(@PathVariable("courseID") int courseID){
        List<Member> memberList = memberService.getActiveMembersByCourse(courseService.get(courseID));
        if(memberList.size() == 0)
            return new ResponseEntity<>(new GenericResponse("Unable to create course session", "Cannot add course sessions until members are registered to the course!",HttpStatus.INTERNAL_SERVER_ERROR.value()) {
            }, HttpStatus.INTERNAL_SERVER_ERROR);

        return new ResponseEntity<>(new GenericResponse("OK", "NO_ERROR", HttpStatus.OK.value()){},HttpStatus.OK);
    }

    @RequestMapping(value="/deleteCourse/{courseID}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<GenericResponse> deleteCourse(@PathVariable("courseID") int courseID){
        Course course = courseService.get(courseID);
        courseService.remove(course);

        return new ResponseEntity<>(new GenericResponse("OK", "NO_ERROR", HttpStatus.OK.value()){},HttpStatus.OK);
    }

    @RequestMapping(value="/{courseID}/fetchNonEnrolledMembers", method=RequestMethod.GET)
    @ResponseBody
    public List<Member> fetchNonEnrolledMembers (@PathVariable("courseID") int courseID, @RequestParam String memberName) {
        List<Member> memberList = memberService.getActiveMembersByCourse(courseService.get(courseID));
        List<Member> resultList = new ArrayList<>();
        Iterator it = memberList.iterator();

        while(it.hasNext()){
            Member member = (Member) it.next();
            MemberCourseRegistration memberCourseRegistration = memberCourseRegistrationService.get(new MemberCourseRegistrationPK(member.getMemberID(), courseID));

            if(memberCourseRegistration == null || !memberCourseRegistration.getIsEnrolled())
                continue;

            it.remove();
        }

        for(Member member : memberList){
            String memberFullName = member.getMemberID() + " " + member.getMemberFirstName() + " " + member.getMemberLastName();
            if(memberFullName.contains(memberName))
                resultList.add(member);
        }
        System.out.println("SIZE: " + resultList.size());
        return resultList;
    }

    @RequestMapping(value="/{courseID}/submitRegistrationList", method=RequestMethod.POST)
    public String submitRegistrationList(@PathVariable("courseID") int courseID, @RequestBody List<Integer> memberIDList){
        for(Integer memberID : memberIDList){
            MemberCourseRegistrationPK memberCourseRegistrationPK = new MemberCourseRegistrationPK(memberID, courseID);
            MemberCourseRegistration memberCourseRegistration = new MemberCourseRegistration();
            memberCourseRegistration.setMemberCourseRegistrationPK(memberCourseRegistrationPK);
            memberCourseRegistration.setMemberID(memberID);
            memberCourseRegistration.setCourseID(courseID);
            memberCourseRegistration.setIsEnrolled(true);
            memberCourseRegistration.setDateRegistered(LocalDate.now());
            memberCourseRegistrationService.update(memberCourseRegistration);
        }

        return "redirect:/admin/courseList/courseInfo/" + courseID;
    }

    @RequestMapping(value="/getCourseSession/{courseSessionID}")
    @ResponseBody
    public List<Member> getCourseSession(@PathVariable("courseSessionID") int courseSessionID) {
        List<Attendance> attendanceList = attendanceService.getAttendanceForCourseSession(courseSessionID);
        List<Member> memberList = new ArrayList<>();
        for(Attendance attendance : attendanceList){
            if(attendance.getWasPresent()) {
                Member member = memberService.get(attendance.getMemberID());
                memberList.add(member);
            }
        }

        return memberList;
    }
}
