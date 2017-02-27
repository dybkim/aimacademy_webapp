package com.aimacademyla.controller;

import com.aimacademyla.model.Course;
import com.aimacademyla.model.Member;
import com.aimacademyla.model.StudentRegistration;
import com.aimacademyla.service.CourseService;
import com.aimacademyla.service.MemberService;
import com.aimacademyla.service.StudentRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by davidkim on 2/2/17.
 */

@Controller
@RequestMapping("/admin/courseList")
public class CourseController {

    private CourseService courseService;

    private StudentRegistrationService studentRegistrationService;

    private MemberService memberService;

    @Autowired
    public CourseController(CourseService courseService, StudentRegistrationService studentRegistrationService, MemberService memberService){
        this.courseService = courseService;
        this.studentRegistrationService = studentRegistrationService;
        this.memberService = memberService;
    }

    @RequestMapping
    public String getCourseList(Model model){
        List<Course> courseList = courseService.getActiveCourseList();
        model.addAttribute("courseList", courseList);
        return "/course/courseList";
    }

    @RequestMapping("/viewEnrollment/{courseID}")
    public String viewEnrollment(@PathVariable ("courseID") int courseID, Model model){
        Course course = courseService.getCourseByID(courseID);
        List<Member> studentList = memberService.getMembersByCourse(course);

        model.addAttribute("studentList", studentList);
        model.addAttribute(course);
        return "/course/viewEnrollment";
    }

    @RequestMapping("/viewEnrollment/{courseID}/addStudentToCourse")
    public String addStudentToCourse(@PathVariable("courseID") int courseID, Model model){

        List<Member> memberList = memberService.getMemberList();
        Course course = courseService.getCourseByID(courseID);
        model.addAttribute(memberList);
        model.addAttribute(course);

        return "/course/addStudentToCourse";
    }

    @RequestMapping(value = "/viewEnrollment/{courseID}/addStudentToCourse", method = RequestMethod.POST)
    public String addStudentToCourse(@PathVariable("courseID") int courseID, @ModelAttribute("studentMember") Member studentMember){
        return null;
    }

    @RequestMapping("/addCourse")
    public String addCourse(Model model){
        Course course = new Course();
        model.addAttribute(course);

        return "/course/addCourse";
    }

    @RequestMapping(value = "/addCourse", method = RequestMethod.POST)
    public String addCourse(@Valid @ModelAttribute("course") Course course, BindingResult result){
        if(result.hasErrors())
            return "/course/addCourse";

        courseService.addCourse(course);

        return "redirect:/course/courseList";
    }

    @RequestMapping("/editCourse/{courseID}")
    public String editCourse(@PathVariable("courseID") int courseID, Model model){
        Course course = courseService.getCourseByID(courseID);
        model.addAttribute(course);

        return "/course/editCourse";
    }

    @RequestMapping(value="/editCourse/{courseID}", method = RequestMethod.POST)
    public String editCourse(@Valid @ModelAttribute("course") Course course, BindingResult result){
        if(result.hasErrors())
            return "/course/editCourse";

        courseService.editCourse(course);

        return "redirect:/course/courseList";
    }
}
