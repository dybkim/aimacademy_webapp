package com.aimacademyla.controller.course.coursesession;

import com.aimacademyla.dao.factory.DAOFactory;
import com.aimacademyla.model.*;
import com.aimacademyla.model.dto.CourseSessionDTO;
import com.aimacademyla.model.initializer.impl.CourseSessionDTODefaultValueInitializer;
import com.aimacademyla.service.CourseService;
import com.aimacademyla.service.CourseSessionService;
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
import java.util.*;

/**
 * CourseController handles all requests regarding a course's info and coursesessions.
 */

@Controller
@RequestMapping("/admin/courseList/courseInfo")
public class CourseSessionController {

    private CourseSessionService courseSessionService;
    private CourseService courseService;
    private DAOFactory daoFactory;
    private ConversionService conversionService;

    private static Logger logger = LogManager.getLogger(CourseSessionController.class);

    @Autowired
    public CourseSessionController(DAOFactory daoFactory,
                                   CourseSessionService courseSessionService,
                                   CourseService courseService,
                                   ConversionService conversionService){
        this.daoFactory = daoFactory;
        this.courseSessionService = courseSessionService;
        this.courseService = courseService;
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
    public String getCourseInfo(@PathVariable("courseID") int courseID, Model model){
        Course course = courseService.get(courseID);
        course = courseService.loadCollections(course);

        //Populate Attendance Collection for each CourseSession in courseSessionSet
        Set<CourseSession> courseSessionSet = new HashSet<>(courseSessionService.loadCollections(course.getCourseSessionSet()));
        course.setCourseSessionSet(courseSessionSet);

        List<Member> activeMemberList = course.getActiveMembers();
        List<Member> inactiveMemberList = course.getInactiveMembers();

        List<CourseSession> courseSessionList = new ArrayList<>(course.getCourseSessionSet());
        HashMap<Integer, Integer> memberAttendanceCountMap = new HashMap<>(course.getMemberAttendanceCountHashMap());

        model.addAttribute("memberList", activeMemberList);
        model.addAttribute("inactiveMemberList", inactiveMemberList);
        model.addAttribute("memberAttendanceCountMap", memberAttendanceCountMap);
        model.addAttribute("course", course);
        model.addAttribute("courseSessionList", courseSessionList);
        return "/course/courseInfo";
    }

    @RequestMapping(value="/{courseID}/addCourseSession")
    public String addCourseSession(@PathVariable("courseID") int courseID, Model model){
        Course course = courseService.get(courseID);
        CourseSessionDTO courseSessionDTO = new CourseSessionDTODefaultValueInitializer(daoFactory).setCourse(course).initialize();
        model.addAttribute("courseSessionDTO", courseSessionDTO);

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
