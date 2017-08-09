package com.aimacademyla.controller.student;

import com.aimacademyla.model.*;
import com.aimacademyla.model.reference.TemporalReference;
import com.aimacademyla.service.ChargeService;
import com.aimacademyla.service.CourseService;
import com.aimacademyla.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

@Controller
@RequestMapping("/admin/student/studentFinances")
public class StudentFinancesController {

    private MemberService memberService;
    private CourseService courseService;
    private ChargeService chargeService;

    @Autowired
    public StudentFinancesController(MemberService memberService,
                                     CourseService courseService,
                                     ChargeService chargeService){
        this.memberService = memberService;
        this.courseService = courseService;
        this.chargeService = chargeService;
    }

    @RequestMapping("/{memberID}")
    public String getStudentFinances(@PathVariable("memberID") int memberID,
                                     @RequestParam(name = "month", required = false) int month,
                                     @RequestParam(name = "year", required = false) int year,
                                     Model model){

        LocalDate selectedDate = null;
        int selectedDateIndex = 0;

        if(year != 0 && month != 0)
            selectedDate = LocalDate.of(year, month, 1);

        Member member = memberService.get(memberID);

        HashMap<Integer, List<Charge>> chargeListHashMap = new HashMap<>();
        LinkedHashMap<Integer, LocalDate> monthHashMap = new LinkedHashMap<>();

        int numMonthsFromInception = TemporalReference.numMonthsFromInception();
        LocalDate startDate = TemporalReference.START_DATE.getDate();

        for(int index = 0; index < numMonthsFromInception; index++){
            int numYears = index / 12;
            int numMonths = index % 12;
            LocalDate date = LocalDate.of(startDate.getYear() + numYears, startDate.getMonthValue() + numMonths, 1);
            List<Charge> chargeList = chargeService.getChargesByMemberByDate(member, date);
            chargeListHashMap.put(index, chargeList);
            monthHashMap.put(index, date);

            if(selectedDate == null && index == numMonthsFromInception - 1){
                selectedDate = date;
                selectedDateIndex = index;
            }
        }

        model.addAttribute("member", member);
        model.addAttribute("chargeListHashMap", chargeListHashMap);
        model.addAttribute("monthHashMap", monthHashMap);
        model.addAttribute("selectedDate", selectedDate);
        model.addAttribute("selectedDateIndex", selectedDateIndex);
        return "/student/studentFinancesInfo";
    }

    @RequestMapping(value="/{memberID}", method=RequestMethod.PUT)
    public String getStudentFinances(@PathVariable("memberID") int memberID, @ModelAttribute("chargeListHashMap") HashMap<Integer, List<Charge>> chargeListHashMap,
                                     Model model) {

        return "redirect:/admin/student/studentFinances/" + memberID;
    }

}
