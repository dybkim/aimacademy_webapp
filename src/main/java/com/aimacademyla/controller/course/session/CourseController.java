package com.aimacademyla.controller.course.session;

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

import java.util.*;

/**
 * Created by davidkim on 3/17/17.
 */

@Controller
@RequestMapping("/admin/courseList/viewEnrollment")
public class CourseController {

    private CourseService courseService;

    private MemberService memberService;

    private CourseSessionService courseSessionService;

    private AttendanceService attendanceService;

    private ChargeLineService chargeLineService;

    private static Logger logger = LogManager.getLogger(CourseController.class);

    @Autowired
    public CourseController(CourseService courseService, MemberService memberService, CourseSessionService courseSessionService, AttendanceService attendanceService, ChargeLineService chargeLineService){
        this.courseService = courseService;
        this.memberService = memberService;
        this.courseSessionService = courseSessionService;
        this.attendanceService = attendanceService;
        this.chargeLineService = chargeLineService;
    }

    @RequestMapping("/{courseID}")
    public String viewEnrollment(@PathVariable("courseID") int courseID, Model model){
        Course course = courseService.getCourseByID(courseID);
        List<Member> studentList = memberService.getMembersByCourse(course);
        List<CourseSession> courseSessionList = courseSessionService.getCourseSessionsForCourse(course);
        int numEnrolled = courseService.getNumEnrolled(courseID);

        model.addAttribute("studentList", studentList);
        model.addAttribute("course", course);
        model.addAttribute("courseSessionList", courseSessionList);
        model.addAttribute("numEnrolled", numEnrolled);

        return "/course/viewEnrollment";
    }

    @RequestMapping("/{courseID}/addStudentToCourse")
    public String addStudentToCourse(@PathVariable("courseID") int courseID, Model model){

        List<Member> memberList = memberService.getMemberList();
        Course course = courseService.getCourseByID(courseID);
        model.addAttribute(memberList);
        model.addAttribute(course);

        return "/course/addStudentToCourse";
    }

    /**
     * NOTE: When adding a CourseSession, a CourseSession is preemptively instantiated in the RDB. In the case that the query is cancelled,
     * the CourseSession instance is deleted.
     */
    @RequestMapping(value="/{courseID}/addCourseSession")
    public String addCourseSession(@PathVariable("courseID") int courseID, Model model){
        Course course = courseService.getCourseByID(courseID);
        CourseSession courseSession = new CourseSession();
        courseSession.setCourseID(course.getCourseID());
        courseSession.setCourseSessionID(courseSessionService.generateCourseSessionIDAfterSave(courseSession));
        List<Member> memberList = memberService.getMembersByCourse(course);
        HashMap<Integer, Attendance> memberAttendanceHashMap = new HashMap<>();

        for(Member member : memberList)
            memberAttendanceHashMap.put(member.getMemberID(), new Attendance());

        CourseSessionAttendanceListWrapper courseSessionAttendanceListWrapper = new CourseSessionAttendanceListWrapper(courseSession, memberAttendanceHashMap);

        model.addAttribute("courseSessionAttendanceListWrapper", courseSessionAttendanceListWrapper);
        model.addAttribute("memberList", memberList);

        return "/course/addCourseSession";
    }

    @RequestMapping(value="/{courseID}/addCourseSession", method = RequestMethod.POST)
    public String addCourseSession(@PathVariable("courseID") int courseID, @ModelAttribute("courseSessionAttendanceListWrapper") CourseSessionAttendanceListWrapper courseSessionAttendanceListWrapper, BindingResult result, final RedirectAttributes redirectAttributes){

        if(result.hasErrors())
        {
            List<FieldError> errors = result.getFieldErrors();

            for (FieldError error : errors ) {
                logger.error(error.getDefaultMessage());
                if(error.getField().equals("courseSession.courseSessionDate"))
                    redirectAttributes.addFlashAttribute("courseSessionDateErrorMsg", "Date must be in MM/DD/YYYY format");
            }

            courseSessionService.deleteCourseSession(courseSessionAttendanceListWrapper.getCourseSession().getCourseSessionID());
            return "redirect:/admin/courseList/viewEnrollment/" + courseID + "/addCourseSession";
        }

        List<Attendance> attendanceList = new ArrayList<>(courseSessionAttendanceListWrapper.getAttendanceMap().values());

        CourseSession courseSession = courseSessionAttendanceListWrapper.getCourseSession();

        Course course = courseService.getCourseByID(courseID);

        double totalCharge = course.getPricePerHour() * course.getClassSessionLengthHours();

        int numAttended = 0;

        for(Attendance attendance : attendanceList) {
            attendance.setAttendanceDate(courseSession.getCourseSessionDate());

            if(attendance.isWasPresent()){
                numAttended++;
                ChargeLine chargeLine = new ChargeLine();
                chargeLine.setAttendanceID(attendance.getAttendanceID());
                chargeLine.setTotalCharge(totalCharge);
                chargeLineService.addChargeLine(chargeLine);
            }
        }

        courseSession.setNumMembersAttended(numAttended);

        courseSessionService.addCourseSession(courseSession);
        attendanceService.addOrUpdateAttendanceList(attendanceList);

        return "redirect:/admin/courseList/viewEnrollment/" + courseID;
    }

    @RequestMapping(value="/viewEnrollment/{courseID}/cancelAddCourseSession/{courseSessionID}")
    public String cancelAddCourseSession(@PathVariable("courseID") int courseID, @PathVariable("courseSessionID") int courseSessionID, @ModelAttribute("courseSessionAttendanceListWrapper") CourseSessionAttendanceListWrapper courseSessionAttendanceListWrapper, Model model){
        courseSessionService.deleteCourseSession(courseSessionID);

        return "redirect:/admin/courseList/viewEnrollment/" + courseID;
    }

    @RequestMapping(value="/{courseID}/editCourseSession/{courseSessionID}")
    public String editCourseSession(@PathVariable("courseID") int courseID, @PathVariable("courseSessionID") int courseSessionID, Model model){
        Course course = courseService.getCourseByID(courseID);
        CourseSession courseSession = courseSessionService.getCourseSessionByID(courseSessionID);
        List<Attendance> attendanceList = attendanceService.getAttendanceForCourseSession(courseSession);
        List<Member> memberList = memberService.getMembersByCourse(course);
        HashMap<Integer, Attendance> attendanceHashMap = new HashMap<>();

        for(Member member : memberList) {
            for (Attendance attendance : attendanceList)
                if(attendance.getMemberID()==member.getMemberID()){
                    attendanceHashMap.put(member.getMemberID(), attendance);
                    break;
                }
        }

        CourseSessionAttendanceListWrapper courseSessionAttendanceListWrapper = new CourseSessionAttendanceListWrapper(courseSession, attendanceHashMap);

        model.addAttribute("courseSessionAttendanceListWrapper", courseSessionAttendanceListWrapper);
        model.addAttribute("memberList", memberList);

        return "/course/editCourseSession";
    }

    @RequestMapping(value="/{courseID}/editCourseSession/{courseSessionID}", method = RequestMethod.POST)
    public String editCourseSession(@PathVariable("courseID") int courseID, @PathVariable("courseSessionID") int courseSessionID, @ModelAttribute CourseSessionAttendanceListWrapper courseSessionAttendanceListWrapper, BindingResult result, final RedirectAttributes redirectAttributes){

        if(result.hasErrors())
        {
            List<FieldError> errors = result.getFieldErrors();

            for (FieldError error : errors ) {
                logger.error(error.getDefaultMessage());
                if(error.getField().equals("courseSession.courseSessionDate"))
                    redirectAttributes.addFlashAttribute("courseSessionDateErrorMsg", "Date must be in MM/DD/YYYY format");
            }

            return "redirect:/admin/courseList/viewEnrollment/" + courseID + "/editCourseSession/" + courseSessionID;
        }

        List<Attendance> attendanceList = new ArrayList<>(courseSessionAttendanceListWrapper.getAttendanceMap().values());

        CourseSession courseSession = courseSessionAttendanceListWrapper.getCourseSession();

        int numAttended = 0;

        for(Attendance attendance : attendanceList) {
            attendance.setAttendanceDate(courseSession.getCourseSessionDate());

            if(attendance.isWasPresent()){
                numAttended++;
            }

            else{
                ChargeLine chargeLine = chargeLineService.getChargeLineByAttendanceID(attendance.getAttendanceID());
                if(chargeLine != null)
                    chargeLineService.deleteChargeLine(chargeLine);
            }
        }

        courseSession.setNumMembersAttended(numAttended);

        courseSessionService.editCourseSession(courseSession);
        attendanceService.addOrUpdateAttendanceList(attendanceList);

        return "redirect:/admin/courseList/viewEnrollment/" + courseID;
    }

    @RequestMapping(value = "/{courseID}/addStudentToCourse", method = RequestMethod.POST)
    public String addStudentToCourse(@PathVariable("courseID") int courseID, @ModelAttribute("studentMember") Member studentMember){
        return null;
    }
}
