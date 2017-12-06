package com.aimacademyla.controller.student;

import com.aimacademyla.dao.factory.DAOFactory;
import com.aimacademyla.model.*;
import com.aimacademyla.model.builder.dto.MemberCourseFinancesDTOBuilder;
import com.aimacademyla.model.builder.dto.MemberListDTOBuilder;
import com.aimacademyla.model.dto.MemberCourseFinancesDTO;
import com.aimacademyla.model.dto.MemberListDTO;
import com.aimacademyla.model.reference.TemporalReference;
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
import java.time.LocalDate;
import java.util.*;

/**
 * Created by davidkim on 1/24/17.
 */

@Controller
@RequestMapping("/admin/student/studentList")
public class StudentListController {

    private MemberService memberService;
    private CourseService courseService;
    private MemberMonthlyRegistrationService memberMonthlyRegistrationService;
    private DAOFactory daoFactory;

    private static final Logger logger = LogManager.getLogger(StudentListController.class.getName());
    @Autowired
    public StudentListController(MemberService memberService,
                                 CourseService courseService,
                                 MemberMonthlyRegistrationService memberMonthlyRegistrationService,
                                 DAOFactory daoFactory){
        this.memberService = memberService;
        this.courseService = courseService;
        this.memberMonthlyRegistrationService = memberMonthlyRegistrationService;
        this.daoFactory = daoFactory;
    }

    @RequestMapping
    public String getStudentList(@RequestParam(value="month", required = false) Integer month,
                                 @RequestParam(value="year", required = false) Integer year,
                                 Model model){
        LocalDate cycleStartDate = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonthValue(),1);

        if(month != null && year != null)
            cycleStartDate = LocalDate.of(year, month ,1);

        MemberListDTO memberListDTO = new MemberListDTOBuilder(daoFactory).setCycleStartDate(cycleStartDate).build();
        List<LocalDate> monthsList = TemporalReference.getMonthList();
        Collections.reverse(monthsList);

        model.addAttribute("cycleStartDate", cycleStartDate);
        model.addAttribute("monthsList", monthsList);
        model.addAttribute("memberListDTO", memberListDTO);
        return "/student/studentList";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String updateStudentList(@ModelAttribute("memberListWrapper") MemberListDTO memberListDTO,
                                 @RequestParam("month") int month,
                                 @RequestParam("year") int year){

        LocalDate cycleStartDate = LocalDate.of(year, month, 1);
        memberMonthlyRegistrationService.updateMemberMonthlyRegistrationList(memberListDTO);

        return "redirect:/admin/student/studentList?month=" + cycleStartDate.getMonthValue() + "&year=" + cycleStartDate.getYear();
    }

    @RequestMapping("/addStudent")
    public String addStudent(Model model){
        Member member = new Member();
        Course openStudy = courseService.get(Course.OPEN_STUDY_ID);
        model.addAttribute("membershipRate", openStudy.getMemberPricePerBillableUnit());
        model.addAttribute(member);
        return "/student/addStudent";
    }

    @RequestMapping(value = "/addStudent", method = RequestMethod.POST)
    public String addStudent(@Valid @ModelAttribute("member") Member member, BindingResult result, RedirectAttributes redirectAttributes){
        if(hasErrors(result)){
            addErrorMessages(result.getFieldErrors(),redirectAttributes);
            return "redirect:/admin/student/studentList/editStudent/" + member.getMemberID();
        }

        Course openStudy = courseService.get(Course.OPEN_STUDY_ID);

        if(member.getMembershipRate() == null)
            member.setMembershipRate(openStudy.getMemberPricePerBillableUnit());

        memberService.add(member);
        return "redirect:/admin/student/studentList";
    }

    @RequestMapping(value="/editStudent/{id}")
    public String editStudent(@PathVariable("id") int id, Model model){
        Member member = memberService.get(id);
        model.addAttribute(member);
        return "/student/editStudent";
    }

    @RequestMapping(value = "/editStudent", method = RequestMethod.POST)
    public String editStudent(@Valid @ModelAttribute("member") Member member, BindingResult result, RedirectAttributes redirectAttributes){
        if(hasErrors(result)){
            addErrorMessages(result.getFieldErrors(),redirectAttributes);
            return "redirect:/admin/student/studentList/editStudent/" + member.getMemberID();
        }

        Member persistedMember = memberService.get(member.getMemberID());

        if(member.getMembershipRate() == null)
            member.setMembershipRate(persistedMember.getMembershipRate());

        memberService.update(member);
        return "redirect:/admin/student/studentList";
    }

    @RequestMapping("/{memberID}")
    public String getStudentInfo(@PathVariable("memberID") int memberID, Model model){
        Member member = memberService.get(memberID);
        member = memberService.loadCollection(member, MemberCourseRegistration.class);

        List<MemberCourseRegistration> memberCourseRegistrationList = new ArrayList<>(member.getMemberCourseRegistrationMap().values());
        List<Course> allCourseList = new ArrayList<>();
        List<Course> activeCourseList = new ArrayList<>();
        List<Course> inactiveCourseList = new ArrayList<>();
        HashMap<Integer, Integer> numCourseSessionsHashMap = new HashMap<>();

        List<MemberCourseFinancesDTO> memberCourseFinancesDTOList = generateMemberCourseFinancesDTOList(member);

        for(MemberCourseRegistration memberCourseRegistration : memberCourseRegistrationList){
            Course course = memberCourseRegistration.getCourse();
            int numCourseSessions = course.getCourseSessionSet().size();

            numCourseSessionsHashMap.put(course.getCourseID(), numCourseSessions);
            allCourseList.add(course);

            if(!course.getIsActive() || !memberCourseRegistration.getIsEnrolled()){
                inactiveCourseList.add(course);
                continue;
            }
            activeCourseList.add(course);

        }
        HashMap<Integer, Integer> courseAttendanceCountHashMap = generateCourseAttendanceCountHashMap(allCourseList, member);

        model.addAttribute("activeCourseList", activeCourseList);
        model.addAttribute("inactiveCourseList", inactiveCourseList);
        model.addAttribute("courseAttendanceCountHashMap", courseAttendanceCountHashMap);
        model.addAttribute("numCourseSessionsHashMap", numCourseSessionsHashMap);
        model.addAttribute("memberCourseFinancesDTOList", memberCourseFinancesDTOList);
        model.addAttribute("member", member);
        return "/student/studentInfo";
    }

    private HashMap<Integer, Integer> generateCourseAttendanceCountHashMap(List<Course> allCourseList, Member member){
        HashMap<Integer, Integer> courseAttendanceCountHashMap = new HashMap<>();

        for(Course course : allCourseList){
            int numAttendance = course.getNumAttendanceForMember(member);
            courseAttendanceCountHashMap.put(course.getCourseID(), numAttendance);
        }

        return courseAttendanceCountHashMap;
    }

    private List<MemberCourseFinancesDTO> generateMemberCourseFinancesDTOList(Member member){
        List<MemberCourseFinancesDTO> memberCourseFinancesDTOList = new ArrayList<>();
        List<LocalDate> monthsList = TemporalReference.getMonthList();

        for(LocalDate cycleStartDate : monthsList){
            MemberCourseFinancesDTO memberCourseFinancesDTO = new MemberCourseFinancesDTOBuilder(daoFactory)
                                                                    .setCycleStartDate(cycleStartDate)
                                                                    .setMember(member)
                                                                    .build();
            memberCourseFinancesDTOList.add(memberCourseFinancesDTO);
        }

        return memberCourseFinancesDTOList;
    }

    private boolean hasErrors(BindingResult result){
        return hasDateFormatErrors(result.getFieldErrors()) || hasMembershipRateErrors(result.getFieldErrors());
    }

    private boolean hasDateFormatErrors(List<FieldError> errorList){
        for (FieldError error : errorList)
            if(error.getField().equals("memberEntryDate"))
                return true;
        return false;
    }

    private boolean hasMembershipRateErrors(List<FieldError> errorList){
        for (FieldError error : errorList)
            if(error.getField().equals("membershipRate"))
             return true;
        return false;
    }

    private void addErrorMessages(List<FieldError> errorList, RedirectAttributes redirectAttributes){
        if(hasDateFormatErrors(errorList))
            addDateFormatErrorMessage(redirectAttributes);

        if(hasMembershipRateErrors(errorList))
            addMembershipRateErrorMessage(redirectAttributes);
    }

    private void addDateFormatErrorMessage(RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("dateJoinedErrorMessage", "Date must be in valid MM/DD/YYYY format");
    }

    private void addMembershipRateErrorMessage(RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("membershipRateErrorMessage", "Membership Rate must be a valid amount!");
    }
}
