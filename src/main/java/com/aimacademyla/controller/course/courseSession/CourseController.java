package com.aimacademyla.controller.course.courseSession;

import com.aimacademyla.model.*;
import com.aimacademyla.model.composite.MemberCourseRegistrationPK;
import com.aimacademyla.model.wrapper.CourseSessionAttendanceListWrapper;
import com.aimacademyla.service.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by davidkim on 3/17/17.
 */

@Controller
@RequestMapping("/admin/courseList/courseInfo")
public class CourseController {

    private CourseService courseService;

    private MemberService memberService;

    private CourseSessionService courseSessionService;

    private AttendanceService attendanceService;

    private ChargeLineService chargeLineService;

    private MemberCourseRegistrationService memberCourseRegistrationService;

    private ChargeService chargeService;

    private static Logger logger = LogManager.getLogger(CourseController.class);

    @Autowired
    public CourseController(CourseService courseService,
                            MemberService memberService,
                            CourseSessionService courseSessionService,
                            AttendanceService attendanceService,
                            ChargeLineService chargeLineService,
                            MemberCourseRegistrationService memberCourseRegistrationService,
                            ChargeService chargeService){
        this.courseService = courseService;
        this.memberService = memberService;
        this.courseSessionService = courseSessionService;
        this.attendanceService = attendanceService;
        this.chargeLineService = chargeLineService;
        this.memberCourseRegistrationService = memberCourseRegistrationService;
        this.chargeService = chargeService;
    }

    /**
     * NOTE: courseInfo only shows member enrollment list only shows members who are enrolled and ACTIVE.
     */
    @RequestMapping("/{courseID}")
    public String courseInfo(@PathVariable("courseID") int courseID, Model model){
        Course course = courseService.get(courseID);
        List<Member> allMemberList;
        List<Member> activeMemberList = memberService.getActiveMembersByCourse(course);
        List<MemberCourseRegistration> memberCourseRegistrationList = memberCourseRegistrationService.getMemberCourseRegistrationListForCourse(course);
        List<Member> inactiveMemberList = new ArrayList<>();
        List<CourseSession> courseSessionList = courseSessionService.getCourseSessionsForCourse(course);
        HashMap<Integer, Integer> memberAttendanceCountMap = new HashMap<>();
        HashMap<Integer, Integer> courseSessionMemberCountMap = new HashMap<>();

        for(MemberCourseRegistration memberCourseRegistration : memberCourseRegistrationList){
            if(!memberCourseRegistration.getIsEnrolled())
                inactiveMemberList.add(memberService.get(memberCourseRegistration.getMemberID()));
        }

        allMemberList = new ArrayList<>(activeMemberList);
        allMemberList.addAll(inactiveMemberList);

        for(CourseSession courseSession : courseSessionList){
            int membersEnrolled = attendanceService.getAttendanceForCourseSession(courseSession).size();
            courseSessionMemberCountMap.put(courseSession.getCourseSessionID(), membersEnrolled);
        }

        /**
         * Tallies the number of courseSessions attended for each member, active and inactive
         */
        for(Member member : allMemberList){
            List<Attendance> attendanceList = attendanceService.getAttendanceForMemberForCourse(member, course);
            Iterator it = attendanceList.iterator();

            while(it.hasNext()){
                Attendance attendance = (Attendance) it.next();

                if(!attendance.getWasPresent())
                    it.remove();
            }

            int attendanceCount = attendanceList.size();

            memberAttendanceCountMap.put(member.getMemberID(), attendanceCount);
        }

        model.addAttribute("memberList", activeMemberList);
        model.addAttribute("inactiveMemberList", inactiveMemberList);
        model.addAttribute("memberAttendanceCountMap", memberAttendanceCountMap);
        model.addAttribute("course", course);
        model.addAttribute("courseSessionList", courseSessionList);
        model.addAttribute("courseSessionMemberCountMap", courseSessionMemberCountMap);

        return "/course/courseInfo";
    }


    /**
     * NOTE: When adding a CourseSession, a CourseSession is preemptively instantiated in the RDB. In the case that the query is cancelled,
     * the CourseSession instance is deleted.
     */
    @RequestMapping(value="/{courseID}/addCourseSession")
    public String addCourseSession(@PathVariable("courseID") int courseID, Model model){
        Course course = courseService.get(courseID);
        CourseSession courseSession = new CourseSession();
        courseSession.setCourseID(course.getCourseID());
        List<Member> activeMemberList = memberService.getActiveMembersByCourse(course);
        HashMap<Integer, Attendance> activeMemberAttendanceHashMap = new HashMap<>();

        for(Member member : activeMemberList){
            activeMemberAttendanceHashMap.put(member.getMemberID(), new Attendance());
        }

        CourseSessionAttendanceListWrapper courseSessionAttendanceListWrapper = new CourseSessionAttendanceListWrapper(courseSession, activeMemberAttendanceHashMap);

        model.addAttribute("courseSessionAttendanceListWrapper", courseSessionAttendanceListWrapper);
        model.addAttribute("memberList", activeMemberList);

        return "/course/addCourseSession";
    }

    @RequestMapping(value="/{courseID}/addCourseSession", method = RequestMethod.POST)
    public String addCourseSession(@ModelAttribute("courseSessionAttendanceListWrapper") CourseSessionAttendanceListWrapper courseSessionAttendanceListWrapper, BindingResult result, @PathVariable("courseID") int courseID, final RedirectAttributes redirectAttributes){

        /**
         * Redirects page if date field is not inputted in the correct format
         */
        if(result.hasErrors())
        {
            List<FieldError> errors = result.getFieldErrors();

            for (FieldError error : errors ) {
                logger.error(error.getDefaultMessage());
                if(error.getField().equals("courseSession.courseSessionDate"))
                    redirectAttributes.addFlashAttribute("courseSessionDateErrorMessage", "Date must be in valid MM/DD/YYYY format");
            }
            return "redirect:/admin/courseList/courseInfo/" + courseID + "/addCourseSession";
        }

        List<Attendance> attendanceList = new ArrayList<>();
        attendanceList.addAll(courseSessionAttendanceListWrapper.getAttendanceMap().values());
        CourseSession courseSession = courseSessionAttendanceListWrapper.getCourseSession();
        Course course = courseService.get(courseID);

        if(courseSession.getCourseSessionDate() == null){
            redirectAttributes.addFlashAttribute("courseSessionDateErrorMessage", "Date field cannot be empty!");
            return "redirect:/admin/courseList/courseInfo/" + courseID + "/addCourseSession";
        }

        BigDecimal totalCharge = course.getPricePerHour().multiply(course.getClassDuration());
        int numAttended = 0;

        /**
         * Adds new attendance for new courseSession
         * Updates chargeLines for each member's attendance
         */
        for(Attendance attendance : attendanceList) {
            attendance.setAttendanceDate(courseSession.getCourseSessionDate());
            attendanceService.add(attendance);

            if(attendance.getWasPresent()){
                numAttended++;
                Member member = memberService.get(attendance.getMemberID());
                Charge charge = chargeService.getChargeByMemberForCourseByDate(member, course, attendance.getAttendanceDate());
                ChargeLine chargeLine = new ChargeLine();
                chargeLine.setAttendanceID(attendance.getAttendanceID());
                chargeLine.setTotalCharge(totalCharge);
                chargeLine.setChargeID(charge.getChargeID());
                chargeLineService.add(chargeLine);
            }
        }

        courseSession.setNumMembersAttended(numAttended);
        courseSessionService.update(courseSession);

        return "redirect:/admin/courseList/courseInfo/" + courseID;
    }
    @RequestMapping(value="/{courseID}/cancelAddCourseSession/{courseSessionID}")
    public String cancelAddCourseSession(@PathVariable("courseID") int courseID, @PathVariable("courseSessionID") int courseSessionID, @ModelAttribute("courseSessionAttendanceListWrapper") CourseSessionAttendanceListWrapper courseSessionAttendanceListWrapper, Model model){
        CourseSession courseSession = courseSessionService.get(courseSessionID);
        courseSessionService.remove(courseSession);

        return "redirect:/admin/courseList/courseInfo/" + courseID;
    }

    @RequestMapping(value="/{courseID}/editCourseSession/{courseSessionID}")
    public String editCourseSession(@PathVariable("courseID") int courseID, @PathVariable("courseSessionID") int courseSessionID, Model model){
        Course course = courseService.get(courseID);
        CourseSession courseSession = courseSessionService.get(courseSessionID);
        List<Attendance> attendanceList = attendanceService.getAttendanceForCourseSession(courseSession);
        List<Member> memberList = new ArrayList<>();
        HashMap<Integer, Attendance> attendanceHashMap = new HashMap<>();

        /**
         * Assigns attendance for course for all members enrolled in that course
         */
        for(Attendance attendance : attendanceList){
            Member member = memberService.get(attendance.getMemberID());
            attendanceHashMap.put(member.getMemberID(), attendance);
            memberList.add(member);
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

            return "redirect:/admin/courseList/courseInfo/" + courseID + "/editCourseSession/" + courseSessionID;
        }

        List<Attendance> attendanceList = new ArrayList<>(courseSessionAttendanceListWrapper.getAttendanceMap().values());
        CourseSession courseSession = courseSessionAttendanceListWrapper.getCourseSession();
        Course course = courseService.get(courseSession.getCourseID());
        BigDecimal totalCharge = course.getPricePerHour().multiply(course.getClassDuration());

        int numAttended = 0;

        /**
         * Updates chargesLines for each member's attendance
         */

        for(Attendance attendance : attendanceList) {
            attendance.setAttendanceDate(courseSession.getCourseSessionDate());

            if(attendance.getWasPresent()) {
                numAttended++;
                Member member = memberService.get(attendance.getMemberID());
                ChargeLine chargeLine = chargeLineService.getChargeLineByAttendanceID(attendance.getAttendanceID());
                /**
                 * If no chargeline currently exists
                 * create new chargeline with the corresponding attendance instance
                 */
                if(chargeLine == null){
                    chargeLine = new ChargeLine();
                    Charge charge = chargeService.getChargeByMemberForCourseByDate(member, course, attendance.getAttendanceDate());
                    chargeLine.setTotalCharge(totalCharge);
                    chargeLine.setChargeID(charge.getChargeID());
                    chargeLine.setAttendanceID(attendance.getAttendanceID());
                    chargeLine.setTotalCharge(totalCharge);
                    chargeLineService.add(chargeLine);
                    continue;
                }

                chargeLine.setTotalCharge(totalCharge);
                chargeLineService.update(chargeLine);
            }

            else{
                ChargeLine chargeLine = chargeLineService.getChargeLineByAttendanceID(attendance.getAttendanceID());
                if(chargeLine != null){
                    chargeLineService.remove(chargeLine);
                }

            attendanceService.update(attendance);
            }
        }

        courseSession.setNumMembersAttended(numAttended);

        courseSessionService.update(courseSession);
        attendanceService.addOrUpdateAttendanceList(attendanceList);

        return "redirect:/admin/courseList/courseInfo/" + courseID;
    }

    @RequestMapping("/{courseID}/removeCourseSession/{courseSessionID}")
    public String removeCourseSession(@ModelAttribute("courseSessionAttendanceListWrapper") CourseSessionAttendanceListWrapper courseSessionAttendanceListWrapper,
                                      @PathVariable("courseID") int courseID,
                                      @PathVariable("courseSessionID") int courseSessionID){

        Course course = courseService.get(courseID);
        CourseSession courseSession = courseSessionService.get(courseSessionID);
        List<Attendance> attendanceList = new ArrayList<>(courseSessionAttendanceListWrapper.getAttendanceMap().values());

        for(Attendance attendance : attendanceList){
            ChargeLine chargeLine = chargeLineService.getChargeLineByAttendanceID(attendance.getAttendanceID());

            if(chargeLine != null)
                chargeLineService.remove(chargeLine);

            attendanceService.remove(attendance);
        }

        courseSessionService.remove(courseSession);

        return "redirect:/admin/courseList/courseInfo/" + courseID;
    }

}
