package com.aimacademyla.controller.course.rest;

import com.aimacademyla.controller.GenericResponse;
import com.aimacademyla.model.Course;
import com.aimacademyla.model.Member;
import com.aimacademyla.model.MemberCourseRegistration;
import com.aimacademyla.model.composite.MemberCourseRegistrationPK;
import com.aimacademyla.model.dto.CourseResourcesDTO;
import com.aimacademyla.model.wrapper.CourseRegistrationWrapper;
import com.aimacademyla.service.CourseService;
import com.aimacademyla.service.MemberCourseRegistrationService;
import com.aimacademyla.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by davidkim on 7/7/17.
 */
@Controller
@RequestMapping("/admin/courseList/rest/")
public class CourseResources {

    private MemberService memberService;
    private CourseService courseService;
    private MemberCourseRegistrationService memberCourseRegistrationService;

    @Autowired
    public CourseResources(MemberService memberService,
                           CourseService courseService,
                           MemberCourseRegistrationService memberCourseRegistrationService){
        this.memberService = memberService;
        this.courseService = courseService;
        this.memberCourseRegistrationService = memberCourseRegistrationService;
    }

    @RequestMapping(value="/{courseID}/validateAddCourseSession")
    public @ResponseBody
    ResponseEntity<GenericResponse> validateAddCourseSession(@PathVariable("courseID") int courseID){
        List<Member> memberList = memberService.getMembersByCourse(courseService.get(courseID));
        if(memberList.size() == 0)
            return new ResponseEntity<>(new GenericResponse("Unable to create course session", "Cannot add course sessions until members are registered to the course!",HttpStatus.INTERNAL_SERVER_ERROR.value()) {
            }, HttpStatus.INTERNAL_SERVER_ERROR);

        return new ResponseEntity<>(new GenericResponse("OK", "NO_ERROR", HttpStatus.OK.value()){},HttpStatus.OK);
    }

    @RequestMapping(value="/{courseID}/addStudentToCourse/{memberID}", method=RequestMethod.POST)
    public void addMemberToCourse(@PathVariable("courseID") int courseID, @PathVariable("memberID") int memberID, Model model){

        List<Member> memberList = memberService.getMemberList();
        Course course = courseService.get(courseID);
        model.addAttribute(memberList);
        model.addAttribute(course);
    }

    @RequestMapping(value="/{courseID}/fetchNonEnrolledMembers", method=RequestMethod.GET)
    @ResponseBody
    public List<Member> fetchNonEnrolledMembers (@PathVariable("courseID") int courseID, @RequestParam String memberName) {
        List<Member> memberList = memberService.getMembersByCourse(courseService.get(courseID));
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

}
