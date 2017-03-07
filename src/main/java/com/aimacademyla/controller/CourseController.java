package com.aimacademyla.controller;

import com.aimacademyla.model.*;
import com.aimacademyla.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
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

    private AttendanceService attendanceService;

    private MemberService memberService;

    private CourseSessionService courseSessionService;

    @Autowired
    public CourseController(CourseService courseService, AttendanceService attendanceService, MemberService memberService, CourseSessionService courseSessionService){
        this.courseService = courseService;
        this.attendanceService = attendanceService;
        this.memberService = memberService;
        this.courseSessionService = courseSessionService;
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
        List<CourseSession> courseSessionList = courseSessionService.getCourseSessionsForCourse(course);
        List<List<Attendance>> attendanceListList = attendanceService.getAttendanceListForCourseSessionList(courseSessionList);
        List<List<Member>> memberListFromAttendanceList = new ArrayList<>();

        for(List<Attendance> attendanceList : attendanceListList)
            memberListFromAttendanceList.add(memberService.getPresentMemberListFromAttendanceList(attendanceList));

        model.addAttribute("studentList", studentList);
        model.addAttribute(course);
        model.addAttribute("courseSessionList", courseSessionList);
        model.addAttribute("attendanceListList", attendanceListList);
        model.addAttribute("memberListFromAttendanceList", memberListFromAttendanceList);

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
    public String addCourse(@Valid @ModelAttribute("course") Course course, BindingResult result, Model model){
        if(result.hasErrors()) {
            List<FieldError> errors = result.getFieldErrors();
            for (FieldError error : errors ) {
                if(error.getField().equals("courseStartDate"))
                    model.addAttribute("startDateErrorMsg", "Date must be in MM/DD/YYYY format");

                else if(error.getField().equals("courseEndDate"))
                    model.addAttribute("endDateErrorMsg", "Date must be in MM/DD/YYYY format");
            }
            return "/course/addCourse";
        }

        courseService.addCourse(course);

        return "redirect:/admin/courseList";
    }

    @RequestMapping("/editCourse/{courseID}")
    public String editCourse(@PathVariable("courseID") int courseID, Model model){
        Course course = courseService.getCourseByID(courseID);
        model.addAttribute(course);

        return "/course/editCourse";
    }

    @RequestMapping(value="/editCourse", method = RequestMethod.POST)
    public String editCourse(@Valid @ModelAttribute("course") Course course, BindingResult result, Model model){
        if(result.hasErrors()) {
            List<FieldError> errors = result.getFieldErrors();
            for (FieldError error : errors ) {
                if(error.getField().equals("courseStartDate"))
                    model.addAttribute("startDateErrorMsg", "Date must be in MM/DD/YYYY format");

                else if(error.getField().equals("courseEndDate"))
                    model.addAttribute("endDateErrorMsg", "Date must be in MM/DD/YYYY format");
            }
            return "/course/editCourse";
        }

        courseService.editCourse(course);

        return "redirect:/admin/courseList";
    }
}
