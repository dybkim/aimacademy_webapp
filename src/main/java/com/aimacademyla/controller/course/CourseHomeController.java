package com.aimacademyla.controller.course;

import com.aimacademyla.model.*;
import com.aimacademyla.model.composite.MemberCourseRegistrationPK;
import com.aimacademyla.model.factory.MemberCourseRegistrationFactory;
import com.aimacademyla.model.wrapper.CourseRegistrationWrapper;
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
import java.util.ArrayList;
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

    private MemberCourseRegistrationService memberCourseRegistrationService;

    private static final Logger logger = LogManager.getLogger(CourseHomeController.class);

    @Autowired
    public CourseHomeController(CourseService courseService,
                                AttendanceService attendanceService,
                                MemberService memberService,
                                CourseSessionService courseSessionService,
                                MemberCourseRegistrationService memberCourseRegistrationService){
        this.courseService = courseService;
        this.attendanceService = attendanceService;
        this.memberService = memberService;
        this.courseSessionService = courseSessionService;
        this.memberCourseRegistrationService = memberCourseRegistrationService;
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
        CourseRegistrationWrapper courseRegistrationWrapper = new CourseRegistrationWrapper();
        courseRegistrationWrapper.setCourse(course);

        List<Member> activeMemberList = memberService.getActiveMembers();
        model.addAttribute(activeMemberList);
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

        addMemberCourseRegistrationList(courseRegistrationWrapper);

        return "redirect:/admin/courseList";
    }

    @RequestMapping("/editCourse/{courseID}")
    public String editCourse(@PathVariable("courseID") int courseID, Model model){
        Course course = courseService.get(courseID);
        List<Member> memberList = memberService.getMembersByCourse(course);
        List<Member> dropMemberList = new ArrayList<>();

        CourseRegistrationWrapper courseRegistrationWrapper = new CourseRegistrationWrapper();
        courseRegistrationWrapper.setCourse(course);
        courseRegistrationWrapper.setMemberList(memberList);

        model.addAttribute(courseRegistrationWrapper);
        model.addAttribute(dropMemberList);

        return "/course/editCourse";
    }

    @RequestMapping(value="/editCourse/{courseID}", method = RequestMethod.POST)
    public String editCourse(@Valid @ModelAttribute("courseRegistrationWrapper") CourseRegistrationWrapper courseRegistrationWrapper, @ModelAttribute("dropMemberList") List<Member> dropMemberList, @PathVariable("courseID") int courseID, BindingResult result, Model model){
        if(result.hasErrors()) {
            List<FieldError> errors = result.getFieldErrors();
            checkDateErrors(errors, model);

            model.addAttribute(courseRegistrationWrapper);
            return "/course/editCourse";
        }

        updateMemberCourseRegistrationList(courseRegistrationWrapper);

        return "redirect:/admin/courseList";
    }

    private void checkDateErrors(List<FieldError> errorList, Model model){
        for (FieldError error : errorList) {
            if(error.getField().equals("courseStartDate"))
                model.addAttribute("startDateErrorMessage", "Date must be in MM/DD/YYYY format");

            else if(error.getField().equals("courseEndDate"))
                model.addAttribute("endDateErrorMessage", "Date must be in MM/DD/YYYY format");
        }
    }

    private void addMemberCourseRegistrationList(CourseRegistrationWrapper courseRegistrationWrapper){
        List<MemberCourseRegistration> memberCourseRegistrationList = new ArrayList<>();
        int courseID = courseRegistrationWrapper.getCourse().getCourseID();

        for(Member member : courseRegistrationWrapper.getMemberList()){
            MemberCourseRegistration memberCourseRegistration = MemberCourseRegistrationFactory.generate(member.getMemberID(), courseID);
            memberCourseRegistrationList.add(memberCourseRegistration);
        }

        courseService.add(courseRegistrationWrapper.getCourse());

        memberCourseRegistrationService.update(memberCourseRegistrationList);
    }

    private void updateMemberCourseRegistrationList(CourseRegistrationWrapper courseRegistrationWrapper){
        int courseID = courseRegistrationWrapper.getCourse().getCourseID();
        List<MemberCourseRegistration> memberCourseRegistrationList = memberCourseRegistrationService.getMemberCourseRegistrationsForCourse(courseRegistrationWrapper.getCourse());

        for(Member member : courseRegistrationWrapper.getMemberList()){
            MemberCourseRegistrationPK memberCourseRegistrationPK = new MemberCourseRegistrationPK(member.getMemberID(),courseID);
            MemberCourseRegistration memberCourseRegistration = memberCourseRegistrationService.get(memberCourseRegistrationPK);

            if(memberCourseRegistration == null){
                memberCourseRegistration = MemberCourseRegistrationFactory.generate(member.getMemberID(),courseID);
                memberCourseRegistrationService.add(memberCourseRegistration);
                continue;
            }

            memberCourseRegistrationService.update(memberCourseRegistration);
            memberCourseRegistrationList.remove(memberCourseRegistration);
        }

        for(MemberCourseRegistration memberCourseRegistration : memberCourseRegistrationList){
            memberCourseRegistration.setEnrolled(false);
            memberCourseRegistrationService.update(memberCourseRegistration);
        }

    }


}
