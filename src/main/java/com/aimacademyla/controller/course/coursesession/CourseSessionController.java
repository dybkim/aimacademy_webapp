package com.aimacademyla.controller.course.coursesession;

import com.aimacademyla.dao.factory.DAOFactory;
import com.aimacademyla.model.*;
import com.aimacademyla.model.dto.CourseSessionDTO;
import com.aimacademyla.model.initializer.impl.CourseSessionDTODefaultValueInitializer;
import com.aimacademyla.model.reference.TemporalReference;
import com.aimacademyla.service.CourseService;
import com.aimacademyla.service.CourseSessionService;
import com.aimacademyla.service.MemberMonthlyRegistrationService;
import com.aimacademyla.service.MemberService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * CourseController handles all requests regarding a course's info and coursesessions.
 */

@Controller
@RequestMapping("/admin/courseList/courseInfo")
public class CourseSessionController {

    private CourseSessionService courseSessionService;
    private CourseService courseService;
    private MemberService memberService;
    private MemberMonthlyRegistrationService memberMonthlyRegistrationService;
    private ConversionService conversionService;

    private static Logger logger = LogManager.getLogger(CourseSessionController.class);

    @Autowired
    public CourseSessionController(DAOFactory daoFactory,
                                   CourseSessionService courseSessionService,
                                   CourseService courseService,
                                   MemberService memberService,
                                   MemberMonthlyRegistrationService memberMonthlyRegistrationService,
                                   ConversionService conversionService){
        this.courseSessionService = courseSessionService;
        this.courseService = courseService;
        this.memberService = memberService;
        this.memberMonthlyRegistrationService = memberMonthlyRegistrationService;
        this.conversionService = conversionService;
    }

    @InitBinder
    public void registerConversionServices(WebDataBinder dataBinder) {
        if(dataBinder.getConversionService() == null)
            dataBinder.setConversionService(conversionService);
    }

    /**
     * NOTE: courseInfo only shows member enrollment list only shows members who are enrolled and ACTIVE.
     */
    @RequestMapping("/{courseID}")
    public String getCourseInfo(@PathVariable("courseID") int courseID,
                                @RequestParam(name="month", required = false) Integer month,
                                @RequestParam(name="year", required = false) Integer year,
                                Model model){
        LocalDate cycleStartDate = LocalDate.now();

        if(month != null && year != null)
            cycleStartDate = LocalDate.of(year, month, 1);

        Course course = courseService.get(courseID);
        course = courseService.loadCollections(course);

        //Populate Attendance Collection for each CourseSession in courseSessionSet
        Set<CourseSession> courseSessionSet = new HashSet<>(courseSessionService.loadCollections(course.getCourseSessionSet()));
        course.setCourseSessionSet(courseSessionSet);

        List<Member> activeMemberList;
        List<Member> inactiveMemberList;
        HashMap<Integer, Integer> memberAttendanceCountMap;
        List<CourseSession> courseSessionList;

        if(courseID != Course.OPEN_STUDY_ID){
            activeMemberList = course.getActiveMembers();
            inactiveMemberList = course.getInactiveMembers();
            memberAttendanceCountMap = new HashMap<>(course.getMemberAttendanceCountHashMap());
            courseSessionList = new ArrayList<>(course.getCourseSessionSet());
        }

        else{
            List<MemberMonthlyRegistration> memberMonthlyRegistrationList = memberMonthlyRegistrationService.getList(cycleStartDate);
            activeMemberList = getActiveMemberList(memberMonthlyRegistrationList);
            inactiveMemberList = getInactiveMemberList(memberMonthlyRegistrationList);
            memberAttendanceCountMap = new HashMap<>(course.getOpenStudyMemberAttendanceCountHashMap(activeMemberList, cycleStartDate));
            courseSessionList = new ArrayList<>(course.getCourseSessionSet(cycleStartDate));
        }

        model.addAttribute("memberList", activeMemberList);
        model.addAttribute("inactiveMemberList", inactiveMemberList);
        model.addAttribute("memberAttendanceCountMap", memberAttendanceCountMap);
        model.addAttribute("course", course);
        model.addAttribute("courseSessionList", courseSessionList);

        if(courseID != Course.OPEN_STUDY_ID)
            return "/course/courseInfo";

        List<LocalDate> monthsList = TemporalReference.getMonthList();
        Collections.reverse(monthsList);

        String cycleString = cycleStartDate.getMonth() + " " + cycleStartDate.getYear();
        model.addAttribute("monthsList", monthsList);
        model.addAttribute("cycleString", cycleString);
        model.addAttribute("cycleStartDate", cycleStartDate);

        return "/course/openstudy/openStudyCourseInfo";
    }

    @RequestMapping(value="/{courseID}/addCourseSession")
    public String addCourseSession(@PathVariable("courseID") int courseID,
                                   @RequestParam(name="month", required=false) Integer month,
                                   @RequestParam(name="year", required=false) Integer year,
                                   Model model){

        LocalDate cycleStartDate = LocalDate.now();

        if(month != null && year != null)
            cycleStartDate = LocalDate.of(year, month, 1);

        System.out.println("CycleStartDate is: " + month + " " + year);
        String monthOffset = "-" + Long.toString(ChronoUnit.MONTHS.between(cycleStartDate, LocalDate.now())) + "m";

        Course course = courseService.get(courseID);
        CourseSessionDTO courseSessionDTO = new CourseSessionDTODefaultValueInitializer()
                                                    .setCourse(course)
                                                    .setCycleStartDate(cycleStartDate)
                                                    .initialize();

        model.addAttribute("courseSessionDTO", courseSessionDTO);
        model.addAttribute("monthOffset", monthOffset);

        return "/course/addCourseSession";
    }

    @RequestMapping(value="/{courseID}/addCourseSession", method = RequestMethod.POST)
    public String addCourseSession(@ModelAttribute("courseSessionDTO") @Valid CourseSessionDTO courseSessionDTO, BindingResult result, @PathVariable("courseID") int courseID, final RedirectAttributes redirectAttributes){
        /*
         * Propagate courseSession's Course field inside this controller method instead of in the JSP because it's simpler to set the values
         * in this method using a converter
         */
        Course course = courseService.getEager(courseID);
        courseSessionDTO.setCourse(course);

        if(hasErrors(result, courseSessionDTO)){
            addErrorMessages(result.getFieldErrors(), courseSessionDTO, redirectAttributes);
            return "redirect:/admin/courseList/courseInfo/" + courseID + "/addCourseSession";
        }

        courseSessionService.addCourseSession(courseSessionDTO);

        return "redirect:/admin/courseList/courseInfo/" + courseID;
    }

    @RequestMapping(value="/{courseID}/editCourseSession/{courseSessionID}")
    public String editCourseSession(@PathVariable("courseID") int courseID, @PathVariable("courseSessionID") int courseSessionID, Model model){
        CourseSession courseSession = courseSessionService.getEager(courseSessionID);
        CourseSessionDTO courseSessionDTO = courseSession.getCourseSessionDTO();

        model.addAttribute("courseSessionDTO", courseSessionDTO);

        return "/course/editCourseSession";
    }

    @RequestMapping(value="/{courseID}/editCourseSession/{courseSessionID}", method = RequestMethod.POST)
    public String editCourseSession(@ModelAttribute("courseSessionDTO") CourseSessionDTO courseSessionDTO, BindingResult result, @PathVariable("courseID") int courseID, @PathVariable("courseSessionID") int courseSessionID, final RedirectAttributes redirectAttributes){
        /*
         * Propagate courseSession's Course field inside this controller method instead of in the JSP because it's simpler to set the values
         * in this method using a converter
         */
        Course course = conversionService.convert(Integer.toString(courseID), Course.class);
        courseSessionDTO.setCourse(course);

        if(hasDateFormatError(courseSessionDTO.getCourseSessionDate()) || hasNullDateError(result.getFieldErrors())){
           if(hasNullDateError(result.getFieldErrors()))
                addNullDateErrorMessage(redirectAttributes);

            else if(hasDuplicateDateError(courseSessionDTO))
                addDuplicateDateErrorMessage(redirectAttributes);

            return "redirect:/admin/courseList/courseInfo/" + courseID + "/addCourseSession";
        }
        courseSessionService.updateCourseSession(courseSessionDTO);

        return "redirect:/admin/courseList/courseInfo/" + courseID;
    }

    @RequestMapping(value="/{courseID}/removeCourseSession/{courseSessionID}")
    public String removeCourseSession(@PathVariable("courseID") int courseID,
                                      @PathVariable("courseSessionID") int courseSessionID){
        /*
         * Propagate courseSession's Course field inside this controller method instead of in the JSP because it's simpler to set the values
         * in this method using a converter
         */
        CourseSession courseSession = courseSessionService.getEager(courseSessionID);
        CourseSessionDTO courseSessionDTO = courseSession.getCourseSessionDTO();

        logger.debug("Removing CourseSession: " + courseSession.getCourseSessionID());
        courseSessionService.removeCourseSession(courseSessionDTO);

        return "redirect:/admin/courseList/courseInfo/" + courseID;
    }

    private List<Member> getActiveMemberList(List<MemberMonthlyRegistration> memberMonthlyRegistrationList){
        Map<Integer, Member> memberMap = new HashMap<>();
        for(MemberMonthlyRegistration memberMonthlyRegistration : memberMonthlyRegistrationList)
            if(memberMonthlyRegistration.getMember().getMemberID() > 1)
                memberMap.put(memberMonthlyRegistration.getMember().getMemberID(), memberMonthlyRegistration.getMember());

        return new ArrayList<>(memberMap.values());
    }

    private List<Member> getInactiveMemberList(List<MemberMonthlyRegistration> memberMonthlyRegistrationList){
        Map<Integer, Member> memberMap = new HashMap<>();
        List<Member> memberList = memberService.getList();

        for(Member member : memberList)
            if(member.getMemberID() > 1)
                memberMap.put(member.getMemberID(), member);

        for(MemberMonthlyRegistration memberMonthlyRegistration : memberMonthlyRegistrationList)
            memberMap.remove(memberMonthlyRegistration.getMember().getMemberID());

        return new ArrayList<>(memberMap.values());
    }

    private boolean hasErrors(BindingResult result, CourseSessionDTO courseSessionDTO){
        List<FieldError> errorList = result.getFieldErrors();
        return (hasDateErrors(errorList, courseSessionDTO));
    }

    private boolean hasDateErrors(List<FieldError> errorList, CourseSessionDTO courseSessionDTO){
        return (hasDateFormatError(courseSessionDTO.getCourseSessionDate()) || hasNullDateError(errorList) || hasDuplicateDateError(courseSessionDTO));
    }

    private boolean hasDateFormatError(LocalDate courseSessionDate){
        if(courseSessionDate == null){
            logger.error("Error in CourseSessionController: courseSessionDate was null!");
            return true;
        }
        return false;
    }

    private boolean hasNullDateError(List<FieldError> errorList){
        for (FieldError error : errorList) {
            if(error.getField().equals("courseSessionDate")) {
                logger.error("Error in CourseSessionController: " + error.getDefaultMessage());
                return true;
            }
        }
        return false;
    }

    private boolean hasDuplicateDateError(CourseSessionDTO courseSessionDTO){
        Course course = courseSessionDTO.getCourse();
        for(CourseSession persistedCourseSession : course.getCourseSessionSet()){
            if(persistedCourseSession.getCourseSessionDate().equals(courseSessionDTO.getCourseSessionDate()))
                return true;
        }
        return false;
    }

    private RedirectAttributes addErrorMessages(List<FieldError> errorList, CourseSessionDTO courseSessionDTO, RedirectAttributes redirectAttributes){
        if(hasDateErrors(errorList, courseSessionDTO))
            addDateFormatErrorMessage(redirectAttributes);

        else if(hasNullDateError(errorList))
            addNullDateErrorMessage(redirectAttributes);

        else if(hasDuplicateDateError(courseSessionDTO))
            addDuplicateDateErrorMessage(redirectAttributes);

        return redirectAttributes;
    }

    private RedirectAttributes addDateFormatErrorMessage(RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("courseSessionDateErrorMsg", "Date must be in MM/DD/YYYY format");
        return redirectAttributes;
    }

    private RedirectAttributes addNullDateErrorMessage(RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("courseSessionDateErrorMessage", "Date field cannot be empty!");
        return redirectAttributes;
    }

    private RedirectAttributes addDuplicateDateErrorMessage(RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("courseSessionDateErrorMessage", "CourseSession already exists!");
        return redirectAttributes;
    }


}
