package com.aimacademyla.controller.resources;

import com.aimacademyla.model.Member;
import com.aimacademyla.model.builder.impl.MemberChargesFinancesWrapperBuilder;
import com.aimacademyla.model.wrapper.MemberChargesFinancesWrapper;
import com.aimacademyla.service.ChargeLineService;
import com.aimacademyla.service.ChargeService;
import com.aimacademyla.service.CourseService;
import com.aimacademyla.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.ModelAndView;

import java.time.LocalDate;

@Controller
@RequestMapping("/admin/resources/excel")
public class ExcelWriterController {

    private MemberService memberService;
    private CourseService courseService;
    private ChargeService chargeService;
    private ChargeLineService chargeLineService;

    @Autowired
    public ExcelWriterController(MemberService memberService,
                                 CourseService courseService,
                                 ChargeService chargeService,
                                 ChargeLineService chargeLineService) {
        this.memberService = memberService;
        this.courseService = courseService;
        this.chargeService = chargeService;
        this.chargeLineService = chargeLineService;
    }

    @RequestMapping("/generateInvoice/{memberID}")
    public ModelAndView generateInvoice(@PathVariable("memberID") int memberID,
                                        @RequestParam("month") int month,
                                        @RequestParam("year") int year){
        Member member = memberService.get(memberID);
        LocalDate selectedDate = LocalDate.of(year, month, 1);
        MemberChargesFinancesWrapperBuilder memberChargesFinancesWrapperBuilder = new MemberChargesFinancesWrapperBuilder(chargeService, chargeLineService, courseService);
        MemberChargesFinancesWrapper memberChargesFinancesWrapper = memberChargesFinancesWrapperBuilder.setMember(member).setSelectedDate(selectedDate).build();
        return new ModelAndView("excelInvoice", "memberChargesFinancesWrapper", memberChargesFinancesWrapper);
    }
}
