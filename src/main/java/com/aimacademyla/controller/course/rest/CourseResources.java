package com.aimacademyla.controller.course.rest;

import com.aimacademyla.controller.GenericResponse;
import com.aimacademyla.model.Course;
import com.aimacademyla.model.Member;
import com.aimacademyla.service.CourseService;
import com.aimacademyla.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by davidkim on 7/7/17.
 */
@Controller
@RequestMapping("/admin/courseList/rest/")
public class CourseResources {

    private MemberService memberService;
    private CourseService courseService;

    @Autowired
    public CourseResources(MemberService memberService,
                           CourseService courseService){
        this.memberService = memberService;
        this.courseService = courseService;
    }

    @RequestMapping(value="/{courseID}/validateAddCourseSession")
    public @ResponseBody
    ResponseEntity<GenericResponse> validateAddCourseSession(@PathVariable("courseID") int courseID){
        List<Member> memberList = memberService.getMembersByCourse(courseService.get(courseID));
        if(memberList.size() == 0)
            return new ResponseEntity<>(new GenericResponse("Unable to create course session", "Cannot add course sessions until members are registered to the course!",HttpStatus.INTERNAL_SERVER_ERROR.value()) {
            }, HttpStatus.INTERNAL_SERVER_ERROR);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value="/{courseID}/addStudentToCourse", method=RequestMethod.POST)
    public void addStudentToCourse(@PathVariable("courseID") int courseID, Model model){

        List<Member> memberList = memberService.getMemberList();
        Course course = courseService.get(courseID);
        model.addAttribute(memberList);
        model.addAttribute(course);
    }

}
