package com.aimacademyla.controller.course;

import com.aimacademyla.model.*;
import com.aimacademyla.model.wrapper.CourseSessionAttendanceListWrapper;
import com.aimacademyla.service.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by davidkim on 2/2/17.
 */

@Controller
@RequestMapping("/admin/courseList")
public class CourseHomeController {

    private CourseService courseService;

    private AttendanceService attendanceService;

    private MemberService memberService;

    private CourseSessionService courseSessionService;

    private static final Logger logger = LogManager.getLogger(CourseHomeController.class);

    @Autowired
    public CourseHomeController(CourseService courseService, AttendanceService attendanceService, MemberService memberService, CourseSessionService courseSessionService){
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
            model.addAttribute(course);
            return "/course/editCourse";
        }

        courseService.editCourse(course);

        return "redirect:/admin/courseList";
    }



}
