package com.aimacademyla.controller.student;

import com.aimacademyla.model.*;
import com.aimacademyla.service.ChargeLineService;
import com.aimacademyla.service.ChargeService;
import com.aimacademyla.service.CourseService;
import com.aimacademyla.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/admin/student/studentFinances")
public class StudentFinancesController {

    private MemberService memberService;
    @Autowired
    public StudentFinancesController(MemberService memberService){
        this.memberService = memberService;
    }

    @RequestMapping("/{memberID}")
    public String getStudentFinances(@PathVariable("memberID") int memberID,
                                     @RequestParam(name = "month", required = false) Integer month,
                                     @RequestParam(name = "year", required = false) Integer year,
                                     Model model){

        LocalDate selectedDate = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonthValue(), 1);

        if(year != null && month != null)
            selectedDate = LocalDate.of(year, month, 1);

        Member member = memberService.get(memberID);

        model.addAttribute("member", member);
        model.addAttribute("selectedDate", selectedDate);

        return "/student/studentFinancesInfo";
    }

    @RequestMapping(value="/{memberID}", method=RequestMethod.POST)
    public String getStudentFinances(@PathVariable("memberID") int memberID, @ModelAttribute("chargeListHashMap") HashMap<Integer, List<Charge>> chargeListHashMap,
                                     Model model) {

        return "redirect:/admin/student/studentFinances/" + memberID;
    }

}
