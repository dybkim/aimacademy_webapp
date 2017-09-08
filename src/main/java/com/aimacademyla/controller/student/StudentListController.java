package com.aimacademyla.controller.student;

import com.aimacademyla.model.*;
import com.aimacademyla.model.builder.impl.MemberCourseFinancesWrapperBuilder;
import com.aimacademyla.model.builder.impl.MemberListWrapperBuilder;
import com.aimacademyla.model.reference.TemporalReference;
import com.aimacademyla.model.wrapper.MemberCourseFinancesWrapper;
import com.aimacademyla.model.wrapper.MemberListWrapper;
import com.aimacademyla.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.temporal.Temporal;
import java.util.*;

/**
 * Created by davidkim on 1/24/17.
 */

@Controller
@RequestMapping("/admin/student/studentList")
public class StudentListController {

    private MemberService memberService;
    private CourseService courseService;
    private MemberCourseRegistrationService memberCourseRegistrationService;
    private CourseSessionService courseSessionService;
    private AttendanceService attendanceService;
    private ChargeService chargeService;
    private PaymentService paymentService;
    private MemberMonthlyRegistrationService memberMonthlyRegistrationService;
    private SeasonService seasonService;

    @Autowired
    public StudentListController(MemberService memberService,
                                 CourseService courseService,
                                 MemberCourseRegistrationService memberCourseRegistrationService,
                                 CourseSessionService courseSessionService,
                                 AttendanceService attendanceService,
                                 ChargeService chargeService,
                                 PaymentService paymentService,
                                 MemberMonthlyRegistrationService memberMonthlyRegistrationService,
                                 SeasonService seasonService){
        this.memberService = memberService;
        this.courseService = courseService;
        this.memberCourseRegistrationService = memberCourseRegistrationService;
        this.courseSessionService = courseSessionService;
        this.attendanceService = attendanceService;
        this.chargeService = chargeService;
        this.paymentService = paymentService;
        this.memberMonthlyRegistrationService = memberMonthlyRegistrationService;
        this.seasonService = seasonService;
    }

    @RequestMapping
    public String getStudentList(@RequestParam(value="month", required = false) Integer month,
                                 @RequestParam(value="year", required = false) Integer year,
                                 Model model){
        LocalDate cycleStartDate = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonthValue(),1);

        if(month != null && year != null)
            cycleStartDate = LocalDate.of(year, month ,1);

        MemberListWrapperBuilder memberListWrapperBuilder = new MemberListWrapperBuilder(memberService, memberMonthlyRegistrationService);
        MemberListWrapper memberListWrapper = memberListWrapperBuilder.setCycleStartDate(cycleStartDate).build();
        List<LocalDate> monthsList = TemporalReference.getMonthList();
        Collections.reverse(monthsList);

        model.addAttribute("cycleStartDate", cycleStartDate);
        model.addAttribute("monthsList", monthsList);
        model.addAttribute("memberListWrapper", memberListWrapper);
        return "/student/studentList";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String getStudentList(@ModelAttribute("memberListWrapper") MemberListWrapper memberListWrapper,
                                 @RequestParam("month") int month,
                                 @RequestParam("year") int year){
        HashMap<Integer, Boolean> membershipHashMap = memberListWrapper.getMembershipHashMap();
        LocalDate cycleStartDate = LocalDate.of(year, month, 1);

        for(int memberID : membershipHashMap.keySet()){
            Member member = memberService.get(memberID);
            MemberMonthlyRegistration memberMonthlyRegistration = memberMonthlyRegistrationService.getMemberMonthlyRegistrationForMemberByDate(member, cycleStartDate);

            Boolean memberIsRegistered = membershipHashMap.get(memberID);

            if(memberIsRegistered == null)
                memberIsRegistered = false;

            if(memberIsRegistered){
                if(memberMonthlyRegistration == null){
                    memberMonthlyRegistration = new MemberMonthlyRegistration();
                    memberMonthlyRegistration.setMemberID(memberID);
                    memberMonthlyRegistration.setCycleStartDate(cycleStartDate);
                    memberMonthlyRegistration.setSeasonID(seasonService.getSeason(cycleStartDate).getSeasonID());
                    memberMonthlyRegistrationService.update(memberMonthlyRegistration);
                }
            }

            else{
                if(memberMonthlyRegistration != null){
                    memberMonthlyRegistrationService.remove(memberMonthlyRegistration);
                }
            }
        }
        return "redirect:/admin/student/studentList?month=" + cycleStartDate.getMonthValue() + "&year=" + cycleStartDate.getYear();
    }

    @RequestMapping("/addStudent")
    public String addStudent(Model model){
        Member member = new Member();
        model.addAttribute(member);
        return "/student/addStudent";
    }

    @RequestMapping(value = "/addStudent", method = RequestMethod.POST)
    public String addStudent(@Valid @ModelAttribute("member") Member member, BindingResult result){
        if(result.hasErrors())
            return "/student/addStudent";

        memberService.add(member);
        return "redirect:/admin/student/studentList";
    }

    @RequestMapping(value="/editStudent/{id}", method=RequestMethod.GET)
    public String editStudent(@PathVariable("id") int id, Model model){
        Member member = memberService.get(id);
        model.addAttribute(member);
        return "/student/editStudent";
    }

    @RequestMapping(value = "/editStudent", method = RequestMethod.POST)
    public String editStudent(@Valid @ModelAttribute("member") Member member, BindingResult result){
        if(result.hasErrors())
            return "/student/editStudent";

        memberService.update(member);
        return "redirect:/admin/student/studentList";
    }

    @RequestMapping("/{memberID}")
    public String getStudentInfo(@PathVariable("memberID") int memberID, Model model){
        Member member = memberService.get(memberID);
        List<MemberCourseRegistration> memberCourseRegistrationList = memberCourseRegistrationService.getMemberCourseRegistrationListForMember(member);
        List<Course> allCourseList;
        List<Course> activeCourseList = new ArrayList<>();
        List<Course> inactiveCourseList = new ArrayList<>();
        HashMap<Integer, List<CourseSession>> courseSessionListHashMap = new HashMap<>();
        HashMap<Integer, List<Attendance>> courseAttendanceListHashMap = new HashMap<>();
        HashMap<Integer, Integer> courseAttendanceCountHashMap = new HashMap<>();

        List<MemberCourseFinancesWrapper> memberCourseFinancesWrapperList = generateMemberCourseFinancesWrapperList(member);

        for(MemberCourseRegistration memberCourseRegistration : memberCourseRegistrationList){
            Course course = courseService.get(memberCourseRegistration.getCourseID());
            courseSessionListHashMap.put(course.getCourseID(),courseSessionService.getCourseSessionsForCourse(course));
            courseAttendanceListHashMap.put(course.getCourseID(), attendanceService.getAttendanceForMemberForCourse(member,course));

            if(!course.getIsActive() || !memberCourseRegistration.getIsEnrolled()){
                inactiveCourseList.add(course);
                continue;
            }

            activeCourseList.add(course);
        }

        allCourseList = new ArrayList<>(activeCourseList);
        allCourseList.addAll(inactiveCourseList);

        for(Course course : allCourseList){
            List<Attendance> attendanceList = courseAttendanceListHashMap.get(course.getCourseID());
            Iterator it = attendanceList.iterator();
            int attendanceCount = 0;

            while(it.hasNext()){
                Attendance attendance = (Attendance) it.next();
                if(attendance.getWasPresent())
                    attendanceCount++;
            }

            courseAttendanceCountHashMap.put(course.getCourseID(), attendanceCount);
        }

        model.addAttribute("activeCourseList", activeCourseList);
        model.addAttribute("inactiveCourseList", inactiveCourseList);
        model.addAttribute("courseAttendanceListHashMap", courseAttendanceListHashMap);
        model.addAttribute("courseAttendanceCountHashMap", courseAttendanceCountHashMap);
        model.addAttribute("courseSessionListHashMap", courseSessionListHashMap);
        model.addAttribute("memberCourseFinancesWrapperList", memberCourseFinancesWrapperList);
        model.addAttribute("member", member);
        return "/student/studentInfo";
    }

    private List<MemberCourseFinancesWrapper> generateMemberCourseFinancesWrapperList(Member member){
        List<MemberCourseFinancesWrapper> memberCourseFinancesWrapperList= new ArrayList<>();
        List<LocalDate> monthsList = TemporalReference.getMonthList();

        for(LocalDate cycleStartDate : monthsList){
            MemberCourseFinancesWrapperBuilder memberCourseFinancesWrapperBuilder = new MemberCourseFinancesWrapperBuilder(paymentService, chargeService);
            MemberCourseFinancesWrapper memberCourseFinancesWrapper = memberCourseFinancesWrapperBuilder.setCycleStartDate(cycleStartDate)
                                                                                                        .setMember(member)
                                                                                                        .build();
            memberCourseFinancesWrapperList.add(memberCourseFinancesWrapper);
        }

        return memberCourseFinancesWrapperList;
    }
}
