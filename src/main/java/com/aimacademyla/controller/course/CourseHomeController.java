package com.aimacademyla.controller.course;

import com.aimacademyla.model.*;
import com.aimacademyla.model.composite.MemberCourseRegistrationPK;
import com.aimacademyla.model.wrapper.CourseRegistrationWrapper;
import com.aimacademyla.model.wrapper.CourseRegistrationWrapperObject;
import com.aimacademyla.service.*;
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
import java.time.LocalDate;
import java.util.*;

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

    private SeasonService seasonService;

    private static final Logger logger = LogManager.getLogger(CourseHomeController.class);

    @Autowired
    public CourseHomeController(CourseService courseService,
                                AttendanceService attendanceService,
                                MemberService memberService,
                                CourseSessionService courseSessionService,
                                MemberCourseRegistrationService memberCourseRegistrationService,
                                SeasonService seasonService){
        this.courseService = courseService;
        this.attendanceService = attendanceService;
        this.memberService = memberService;
        this.courseSessionService = courseSessionService;
        this.memberCourseRegistrationService = memberCourseRegistrationService;
        this.seasonService = seasonService;
    }

    @RequestMapping
    public String getCourseList(Model model){
        List<Course> courseList = courseService.getActiveCourseList();
        List<Course> inactiveCourseList = new ArrayList<>();
        Iterator it = courseList.iterator();

        while(it.hasNext()){
            Course course = (Course)it.next();
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
        List<Member> activeMemberList = memberService.getActiveMembersByCourse(course);
        CourseRegistrationWrapperObject courseRegistrationWrapperObject;
        List<CourseRegistrationWrapperObject> courseRegistrationWrapperObjectList = new ArrayList<>();

        for (Member member : activeMemberList) {
            courseRegistrationWrapperObject = new CourseRegistrationWrapperObject();
            courseRegistrationWrapperObject.setMember(member);
            courseRegistrationWrapperObject.setIsDropped(false);
            courseRegistrationWrapperObjectList.add(courseRegistrationWrapperObject);
        }

        CourseRegistrationWrapper courseRegistrationWrapper = new CourseRegistrationWrapper();
        courseRegistrationWrapper.setCourse(course);
        courseRegistrationWrapper.setCourseRegistrationWrapperObjectList(courseRegistrationWrapperObjectList);

        model.addAttribute(courseRegistrationWrapper);

        return "/course/editCourse";
    }

    @RequestMapping(value="/editCourse/{courseID}", method = RequestMethod.POST)
    public String editCourse(@Valid @ModelAttribute("courseRegistrationWrapper") CourseRegistrationWrapper courseRegistrationWrapper, BindingResult result, @PathVariable("courseID") int courseID, Model model){

        if(result.hasErrors()) {
            List<FieldError> errors = result.getFieldErrors();
            model = checkDateErrors(errors, model);

            model.addAttribute(courseRegistrationWrapper);
            return "/course/editCourse";
        }

        Course course = courseRegistrationWrapper.getCourse();
        course.setSeasonID(seasonService.getSeason(course.getCourseStartDate()).getSeasonID());
        course.setNumEnrolled(courseRegistrationWrapper.getCourseRegistrationWrapperObjectList().size());
        courseService.update(course);

        updateMemberCourseRegistrationList(courseRegistrationWrapper);

        return "redirect:/admin/courseList/courseInfo/" + courseID;
    }

    @RequestMapping("/editCourse/{courseID}/addStudentToCourse")
    public String addStudentToCourse(@PathVariable("courseID") int courseID, Model model){
        Course course = courseService.get(courseID);
        List<Member> activeMembers = memberService.getActiveMembersByCourse(course);
        List<Member> allMembers = memberService.getMemberList();
        LinkedHashMap<Integer, String> unenrolledMembersMap = new LinkedHashMap<>();
        LinkedHashMap<Integer, String> allMembersMap = new LinkedHashMap<>();

        allMembers.sort((memberLHS, memberRHS) -> {
            int result = memberLHS.getMemberFirstName().compareTo(memberRHS.getMemberFirstName());

            if(result == 0)
                result = memberLHS.getMemberLastName().compareTo(memberRHS.getMemberLastName());

            return result;
        });

        for(Member member : allMembers) {
            String identifier = member.getMemberFirstName() + " " + member.getMemberLastName() + " " + member.getMemberID();
            if(member.getMemberIsActive()){
                allMembersMap.put(member.getMemberID(), identifier);
                unenrolledMembersMap.put(member.getMemberID(), identifier);
            }
        }

        for(Member member : activeMembers){
            unenrolledMembersMap.remove(member.getMemberID());
        }

        MemberCourseRegistration memberCourseRegistration = new MemberCourseRegistration();
        memberCourseRegistration.setCourseID(courseID);

        model.addAttribute("unenrolledMembersMap", unenrolledMembersMap);
        model.addAttribute("allMembersMap", allMembersMap);
        model.addAttribute("memberCourseRegistration", memberCourseRegistration);
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

        courseService.add(courseRegistrationWrapper.getCourse());

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
}
