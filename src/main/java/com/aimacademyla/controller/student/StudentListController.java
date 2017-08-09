package com.aimacademyla.controller.student;

import com.aimacademyla.model.*;
import com.aimacademyla.model.reference.TemporalReference;
import com.aimacademyla.model.wrapper.MemberCourseFinancesWrapper;
import com.aimacademyla.model.wrapper.MemberListWrapper;
import com.aimacademyla.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
    private MemberCourseRegistrationService memberCourseRegistrationService;
    private CourseSessionService courseSessionService;
    private AttendanceService attendanceService;
    private ChargeService chargeService;
    private PaymentService paymentService;

    @Autowired
    public StudentListController(MemberService memberService,
                                 CourseService courseService,
                                 MemberCourseRegistrationService memberCourseRegistrationService,
                                 CourseSessionService courseSessionService,
                                 AttendanceService attendanceService,
                                 ChargeService chargeService,
                                 PaymentService paymentService){
        this.memberService = memberService;
        this.courseService = courseService;
        this.memberCourseRegistrationService = memberCourseRegistrationService;
        this.courseSessionService = courseSessionService;
        this.attendanceService = attendanceService;
        this.chargeService = chargeService;
        this.paymentService = paymentService;
    }

    @RequestMapping
    public String getStudentList(Model model){
        List<Member> memberList = memberService.getMemberList();
        List<Member> inactiveList = new ArrayList<>();
        Iterator it = memberList.iterator();

        while(it.hasNext()){
            Member member = (Member) it.next();
            if(!member.getMemberIsActive()){
                inactiveList.add(member);
                it.remove();
            }
        }

        MemberListWrapper memberListWrapper = new MemberListWrapper(memberList, inactiveList);
        model.addAttribute("memberListWrapper", memberListWrapper);
        return "/student/studentList";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String getStudentList(@ModelAttribute("memberListWrapper") MemberListWrapper memberListWrapper, Model model){
        List<Member> memberList = memberListWrapper.getMemberList();
        memberList.addAll(memberListWrapper.getInactiveList());

        memberService.updateMemberList(memberList);
        return "redirect:/admin/student/studentList";
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
        HashMap<Integer, Payment> chargePaymentHashMap = new HashMap<>();

        int numMonthsFromInception = TemporalReference.numMonthsFromInception();

        for(int counter = 0; counter < numMonthsFromInception; counter++){

            double totalChargeAmount = 0;
            double totalPaymentAmount = 0;

            int year = TemporalReference.START_DATE.getDate().getYear() + (counter / 12);
            int month = TemporalReference.START_DATE.getDate().getMonthValue() + (counter % 12);

            LocalDate localDate = LocalDate.of(year, month, 1);

            List<Charge> chargeList = chargeService.getChargesByMemberByDate(member, localDate);
            Iterator it = chargeList.iterator();

            /**
             * Remove charges from charge list that have an amount of 0 dollars
             */
            while(it.hasNext()){
                Charge charge = (Charge) it.next();
                if(charge.getChargeAmount() <= 0){
                    it.remove();
                    continue;
                }

                totalChargeAmount += charge.getChargeAmount();
                Payment payment = paymentService.getPaymentForCharge(charge);

                if(payment.getPaymentID() != Payment.NO_PAYMENT){
                    totalPaymentAmount += payment.getPaymentAmount();
                    chargePaymentHashMap.put(charge.getChargeID(), payment);
                }
            }

            LocalDate date = LocalDate.of(year, month, 1);
            MemberCourseFinancesWrapper.MemberCourseFinancesWrapperBuilder memberCourseFinancesWrapperBuilder = new MemberCourseFinancesWrapper.MemberCourseFinancesWrapperBuilder();

            MemberCourseFinancesWrapper memberCourseFinancesWrapper = memberCourseFinancesWrapperBuilder.setChargeList(chargeList)
                                                                                                        .setDate(date)
                                                                                                        .setTotalChargeAmount(totalChargeAmount)
                                                                                                        .setTotalPaymentAmount(totalPaymentAmount)
                                                                                                        .setChargePaymentHashMap(chargePaymentHashMap)
                                                                                                        .build();

            memberCourseFinancesWrapperList.add(memberCourseFinancesWrapper);
        }

        return memberCourseFinancesWrapperList;
    }
}
