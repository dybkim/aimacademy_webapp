package com.aimacademyla.controller.course;

import com.aimacademyla.dao.factory.DAOFactory;
import com.aimacademyla.model.*;
import com.aimacademyla.model.builder.dto.CourseRegistrationDTOBuilder;
import com.aimacademyla.model.enums.BillableUnitType;
import com.aimacademyla.model.dto.CourseRegistrationDTO;
import com.aimacademyla.service.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

/**
 * Created by davidkim on 2/2/17.
 *
 * CourseHomeController handles all requests regarding adding/updating/deleting new courses, as well as displaying info on all courses
 */

@Controller
@RequestMapping("/admin/courseList")
public class CourseHomeController {

    private DAOFactory daoFactory;
    private CourseService courseService;

    private static final Logger logger = LogManager.getLogger(CourseHomeController.class);

    @Autowired
    public CourseHomeController(DAOFactory daoFactory,
                                CourseService courseService){
        this.daoFactory = daoFactory;
        this.courseService = courseService;
    }

    @RequestMapping
    public String getCourseList(Model model){
        List<Course> courseList = courseService.getActiveList();
        List<Course> inactiveCourseList = courseService.getInactiveList();

        model.addAttribute("inactiveCourseList", inactiveCourseList);
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
        List<FieldError> errorList = result.getFieldErrors();

        if(hasErrors(errorList, course)){
            addErrorMessages(errorList, model, course);
            logger.error("ERROR: CourseHomeController.addCourse - Formatting error!");
            model.addAttribute("course", course);
            return "/course/addCourse";
        }

        courseService.addCourse(course);
        logger.info("INFO: CourseHomeController.addCourse - Successfully added new course: " + course.getCourseName());
        return "redirect:/admin/courseList";
    }

    @RequestMapping("/editCourse/{courseID}")
    public String editCourse(@PathVariable("courseID") int courseID, Model model){
        Course course = courseService.get(courseID);
        CourseRegistrationDTO courseRegistrationDTO = new CourseRegistrationDTOBuilder(daoFactory).setCourse(course).build();
        model.addAttribute("courseRegistrationDTO", courseRegistrationDTO);

        return "/course/editCourse";
    }

    @RequestMapping(value="/editCourse/{courseID}", method = RequestMethod.POST)
    public String editCourse(@Valid @ModelAttribute("courseRegistrationDTO") CourseRegistrationDTO courseRegistrationDTO, BindingResult result, @PathVariable("courseID") int courseID, Model model){
        List<FieldError> errorList = result.getFieldErrors();
        Course course = courseRegistrationDTO.getCourse();

        if(hasErrors(errorList, course)){
            addErrorMessages(errorList, model, course);
            logger.error("ERROR: CourseHomeController.addCourse - Formatting error!");
            model.addAttribute("course", course);
            return "/course/editCourse";
        }

        courseService.updateCourse(courseRegistrationDTO);
        logger.info("INFO: CourseHomeController.addCourse - Successfully edited course: " + course.getCourseName());
        return "redirect:/admin/courseList/courseInfo/" + courseID;
    }

    @RequestMapping("/editCourse/{courseID}/addStudentToCourse")
    public String addStudentToCourse(@PathVariable("courseID") int courseID, Model model){
        Course course = courseService.get(courseID);
        model.addAttribute("course", course);

        return "/course/addStudentToCourse";
    }

    private boolean hasErrors(List<FieldError> errorList, Course course){
        return (hasDateErrors(errorList) || hasClassDurationFieldError(course));
    }

    private boolean hasDateErrors(List<FieldError> errorList){
        for (FieldError error : errorList) {
            if(error.getField().equals("course.courseStartDate") || error.getField().equals("course.courseEndDate"))
                return true;
        }

        return false;
    }

    private boolean hasClassDurationFieldError(Course course){
        return BillableUnitType.PER_HOUR.toString().equals(course.getBillableUnitType()) && course.getClassDuration() == null;
    }

    private Model addErrorMessages(List<FieldError> errorList, Model model, Course course){
        if(hasDateErrors(errorList))
            model = addDateErrorMessages(errorList, model);

        if(hasClassDurationFieldError(course))
            model = addClassDurationFieldErrorMessages(model);

        return model;
    }

    private Model addClassDurationFieldErrorMessages(Model model){
        model.addAttribute("billableUnitTypeError", "Class Duration cannot be empty if billing type is 'per hour'!");
        return model;
    }

    private Model addDateErrorMessages(List<FieldError> errorList, Model model){
        for (FieldError error : errorList) {
            if(error.getField().equals("course.courseStartDate"))
                model.addAttribute("startDateErrorMessage", "Date must be in MM/DD/YYYY format");

            else if(error.getField().equals("course.courseEndDate"))
                model.addAttribute("endDateErrorMessage", "Date must be in MM/DD/YYYY format");
        }

        return model;
    }




}
