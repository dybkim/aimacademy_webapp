package com.aimacademyla.controller.course;

import com.aimacademyla.model.*;
import com.aimacademyla.model.builder.impl.CourseRegistrationWrapperBuilder;
import com.aimacademyla.model.composite.MemberCourseRegistrationPK;
import com.aimacademyla.model.enums.BillableUnitType;
import com.aimacademyla.model.wrapper.CourseRegistrationWrapper;
import com.aimacademyla.model.wrapper.CourseRegistrationWrapperObject;
import com.aimacademyla.service.*;
import com.aimacademyla.service.factory.ServiceFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

/**
 * Created by davidkim on 2/2/17.
 *
 * CourseHomeController handles all requests regarding adding/updating/deleting new courses, as well as displaying info on all courses
 */

@Controller
@RequestMapping("/admin/courseList")
public class CourseHomeController {

    private ServiceFactory serviceFactory;

    private CourseService courseService;
    private MemberCourseRegistrationService memberCourseRegistrationService;
    private SeasonService seasonService;

    private static final Logger logger = LogManager.getLogger(CourseHomeController.class);

    @Autowired
    public CourseHomeController(ServiceFactory serviceFactory,
                                CourseService courseService,
                                MemberCourseRegistrationService memberCourseRegistrationService,
                                SeasonService seasonService){
        this.serviceFactory = serviceFactory;
        this.courseService = courseService;
        this.memberCourseRegistrationService = memberCourseRegistrationService;
        this.seasonService = seasonService;
    }

    @RequestMapping
    public String getCourseList(Model model){
        List<Course> courseList = courseService.getList();
        List<Course> inactiveCourseList = new ArrayList<>();
        Iterator it = courseList.iterator();

        while(it.hasNext()){
            Course course = (Course)it.next();

            if(course.getCourseID() == Course.OPEN_STUDY_ID || course.getCourseID() == Course.OTHER_ID){
                it.remove();
                continue;
            }

            if(!course.getIsActive()) {
                inactiveCourseList.add(course);
                it.remove();
            }
        }

        model.addAttribute("inactiveCourseList", inactiveCourseList);
        model.addAttribute("courseList", courseList);
        return "/course/courseList";
    }

    @RequestMapping("/addCourse")
    public String addCourse(Model model){
        Course course = new Course();
        CourseRegistrationWrapper courseRegistrationWrapper = new CourseRegistrationWrapper();
        courseRegistrationWrapper.setCourse(course);

        model.addAttribute(courseRegistrationWrapper);

        return "/course/addCourse";
    }


    @RequestMapping(value = "/addCourse", method = RequestMethod.POST)
    public String addCourse(@Valid @ModelAttribute("courseRegistrationWrapper") CourseRegistrationWrapper courseRegistrationWrapper, BindingResult result, Model model){

        if(result.hasErrors()) {
            List<FieldError> errors = result.getFieldErrors();
            checkDateErrors(errors, model);

            return "/course/addCourse";
        }

        Course course = courseRegistrationWrapper.getCourse();

        setBillableUnitDuration(course);

        courseService.add(courseRegistrationWrapper.getCourse());
        return "redirect:/admin/courseList";
    }

    @RequestMapping("/editCourse/{courseID}")
    public String editCourse(@PathVariable("courseID") int courseID, Model model){
        CourseRegistrationWrapper courseRegistrationWrapper = new CourseRegistrationWrapperBuilder(serviceFactory).setCourseID(courseID).build();
        model.addAttribute("courseRegistrationWrapper", courseRegistrationWrapper);

        return "/course/editCourse";
    }

    @RequestMapping(value="/editCourse/{courseID}", method = RequestMethod.PUT)
    public String editCourse(@Valid @ModelAttribute("courseRegistrationWrapper") CourseRegistrationWrapper courseRegistrationWrapper, BindingResult result, @PathVariable("courseID") int courseID, Model model){

        if(result.hasErrors()) {
            List<FieldError> errors = result.getFieldErrors();
            model = checkDateErrors(errors, model);

            model.addAttribute(courseRegistrationWrapper);
            return "/course/editCourse";
        }

        Course course = courseRegistrationWrapper.getCourse();

        if(course.getCourseStartDate() != null)
            course.setSeasonID(seasonService.getSeason(course.getCourseStartDate()).getSeasonID());

        int numEnrolled = 0;

        // Have to check for null list instead of empty list because JSP returns null list if list is empty
        if(courseRegistrationWrapper.getCourseRegistrationWrapperObjectList() != null){
            for(CourseRegistrationWrapperObject courseRegistrationWrapperObject : courseRegistrationWrapper.getCourseRegistrationWrapperObjectList())
                if(!courseRegistrationWrapperObject.getIsDropped())
                    numEnrolled++;

            updateMemberCourseRegistrationList(courseRegistrationWrapper);
        }
        
        course.setNumEnrolled(numEnrolled);

        setBillableUnitDuration(course);

        courseService.update(course);

        return "redirect:/admin/courseList/courseInfo/" + courseID;
    }

    @RequestMapping("/editCourse/{courseID}/addStudentToCourse")
    public String addStudentToCourse(@PathVariable("courseID") int courseID, Model model){
        Course course = courseService.get(courseID);
        model.addAttribute("course", course);

        return "/course/addStudentToCourse";
    }

    @RequestMapping(value="/editCourse/{courseID}/addStudentToCourse", method=RequestMethod.POST)
    public String addStudentToCourse(@PathVariable("courseID") int courseID, @ModelAttribute("memberCourseRegistration") MemberCourseRegistration memberCourseRegistration, BindingResult result, final RedirectAttributes redirectAttributes){
        if(result.hasErrors())
        {
            List<FieldError> errors = result.getFieldErrors();

            for (FieldError error : errors ) {
                logger.error(error.getDefaultMessage());
                if(error.getField().equals("memberCourseRegistration.dateEnrolled"))
                    redirectAttributes.addFlashAttribute("dateEnrolledErrorMessage", "Date must be in MM/DD/YYYY format");
            }
            return "redirect:/admin/courseList/editCourse/" + courseID + "/addStudentToCourse";
        }

        MemberCourseRegistrationPK memberCourseRegistrationPK = new MemberCourseRegistrationPK(memberCourseRegistration.getMemberID(), memberCourseRegistration.getCourseID());
        memberCourseRegistration.setMemberCourseRegistrationPK(memberCourseRegistrationPK);
        memberCourseRegistrationService.add(memberCourseRegistration);

        return "redirect:/admin/courseList/editCourse/" + courseID;
    }

    private Model checkDateErrors(List<FieldError> errorList, Model model){
        for (FieldError error : errorList) {
            if(error.getField().equals("course.courseStartDate"))
                model.addAttribute("startDateErrorMessage", "Date must be in MM/DD/YYYY format");

            else if(error.getField().equals("course.courseEndDate"))
                model.addAttribute("endDateErrorMessage", "Date must be in MM/DD/YYYY format");
        }

        return model;
    }

    private void addMemberCourseRegistrationList(CourseRegistrationWrapper courseRegistrationWrapper){
        List<MemberCourseRegistration> memberCourseRegistrationList = new ArrayList<>();
        int courseID = courseRegistrationWrapper.getCourse().getCourseID();
        Member member;

        for(CourseRegistrationWrapperObject courseRegistrationWrapperObject : courseRegistrationWrapper.getCourseRegistrationWrapperObjectList()){
            member = courseRegistrationWrapperObject.getMember();
            MemberCourseRegistration memberCourseRegistration = new MemberCourseRegistration();
            memberCourseRegistration.setMemberID(member.getMemberID());
            memberCourseRegistration.setCourseID(courseID);
            memberCourseRegistration.setIsEnrolled(true);
            memberCourseRegistration.setDateRegistered(LocalDate.now());
            memberCourseRegistration.setMemberCourseRegistrationPK(new MemberCourseRegistrationPK(member.getMemberID(), courseID));
            memberCourseRegistrationList.add(memberCourseRegistration);
        }

        memberCourseRegistrationService.update(memberCourseRegistrationList);
    }

    private void updateMemberCourseRegistrationList(CourseRegistrationWrapper courseRegistrationWrapper){
        int courseID = courseRegistrationWrapper.getCourse().getCourseID();
        Member member;

        for(CourseRegistrationWrapperObject courseRegistrationWrapperObject : courseRegistrationWrapper.getCourseRegistrationWrapperObjectList()){
            member = courseRegistrationWrapperObject.getMember();
            MemberCourseRegistration memberCourseRegistration = memberCourseRegistrationService.get(member.getMemberID(), courseID);

            if(courseRegistrationWrapperObject.getIsDropped())
                memberCourseRegistration.setIsEnrolled(false);

            memberCourseRegistrationService.update(memberCourseRegistration);
        }
    }

    private void setBillableUnitDuration(Course course){
        switch(BillableUnitType.parseString(course.getBillableUnitType())){
            case PER_HOUR:
                course.setBillableUnitDuration(course.getClassDuration());
            case PER_SESSION:
                course.setBillableUnitDuration(BigDecimal.ONE);
            default:
                course.setBillableUnitDuration(BigDecimal.ZERO);
        }
    }
}
