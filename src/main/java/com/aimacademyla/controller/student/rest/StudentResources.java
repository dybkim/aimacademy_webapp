package com.aimacademyla.controller.student.rest;

import com.aimacademyla.model.Charge;
import com.aimacademyla.model.Course;
import com.aimacademyla.model.Member;
import com.aimacademyla.model.Season;
import com.aimacademyla.model.builder.impl.MemberChargesFinancesWrapperBuilder;
import com.aimacademyla.model.wrapper.MemberChargesFinancesWrapper;
import com.aimacademyla.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Controller
@RequestMapping("/admin/student/rest")
public class StudentResources{

    private MemberService memberService;
    private ChargeService chargeService;
    private ChargeLineService chargeLineService;
    private CourseService courseService;
    private SeasonService seasonService;

    @Autowired
    public StudentResources(MemberService memberService,
                            ChargeService chargeService,
                            ChargeLineService chargeLineService,
                            CourseService courseService,
                            SeasonService seasonService){
        this.memberService = memberService;
        this.chargeService = chargeService;
        this.chargeLineService = chargeLineService;
        this.courseService = courseService;
        this.seasonService = seasonService;
    }

    @RequestMapping("/studentFinances/{memberID}")
    public @ResponseBody
    MemberChargesFinancesWrapper fetchMemberCharges(@PathVariable("memberID") int memberID,
                                                     @RequestParam(name = "month") int month,
                                                     @RequestParam(name = "year") int year){
        LocalDate selectedDate = LocalDate.of(year, month, 1);
        Member member = memberService.get(memberID);

        return new MemberChargesFinancesWrapperBuilder(chargeService, chargeLineService, courseService).setSelectedDate(selectedDate)
                                                                                                        .setMember(member)
                                                                                                        .build();

    }

    @RequestMapping(value="/studentFinances/{memberID}/addMiscCharge", method=RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addMiscCharge(@PathVariable("memberID") int memberID,
                                                @RequestParam(name="chargeDescription") String chargeDescription,
                                                @RequestParam(name="chargeAmount") BigDecimal chargeAmount,
                                                @RequestParam(name="chargeDiscount", required = false) BigDecimal chargeDiscount,
                                                @RequestParam(name="month") int month,
                                                @RequestParam(name="year") int year){
        LocalDate selectedDate = LocalDate.of(year, month, 1);

        Charge charge = new Charge();
        charge.setMemberID(memberID);
        charge.setDescription(chargeDescription);
        charge.setChargeAmount(chargeAmount);
        charge.setDiscountAmount(chargeDiscount);
        charge.setCourseID(Course.OTHER_ID);
        charge.setCycleStartDate(selectedDate);
        charge.setSeasonID(seasonService.getSeason(selectedDate).getSeasonID());

        chargeService.add(charge);
    }

    @RequestMapping(value="/studentFinances/dropMiscCharge/{chargeID}", method=RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void dropMiscCharges(@PathVariable("chargeID") int chargeID){
        Charge charge = chargeService.get(chargeID);
        chargeService.remove(charge);
    }

    @RequestMapping(value="/studentFinances/discountCharge/{chargeID}", method=RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void discountCharge(@PathVariable("chargeID") int chargeID, @RequestParam("discount") BigDecimal discountAmount){
        Charge charge = chargeService.get(chargeID);
        charge.setDiscountAmount(discountAmount);
        chargeService.update(charge);
    }
}
