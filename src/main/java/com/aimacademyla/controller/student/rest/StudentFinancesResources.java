package com.aimacademyla.controller.student.rest;

import com.aimacademyla.model.Charge;
import com.aimacademyla.model.Course;
import com.aimacademyla.model.Member;
import com.aimacademyla.model.builder.impl.MemberChargesFinancesWrapperBuilder;
import com.aimacademyla.model.wrapper.MemberChargesFinancesWrapper;
import com.aimacademyla.service.*;
import com.aimacademyla.service.factory.ServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * StudentFinancesResources is a resource provider for requests sent to the studentFinancesController
 */

@Controller
@RequestMapping("/admin/student/rest/studentFinances")
public class StudentFinancesResources {

    private ServiceFactory serviceFactory;

    private MemberService memberService;
    private ChargeService chargeService;
    private SeasonService seasonService;

    @Autowired
    public StudentFinancesResources(ServiceFactory serviceFactory,
                                    MemberService memberService,
                                    ChargeService chargeService,
                                    SeasonService seasonService){
        this.serviceFactory = serviceFactory;
        this.memberService = memberService;
        this.chargeService = chargeService;
        this.seasonService = seasonService;
    }

    @RequestMapping("/{memberID}")
    public @ResponseBody
    MemberChargesFinancesWrapper fetchMemberCharges(@PathVariable("memberID") int memberID,
                                                    @RequestParam(name = "month") int month,
                                                    @RequestParam(name = "year") int year){
        LocalDate selectedDate = LocalDate.of(year, month, 1);
        Member member = memberService.get(memberID);

        return new MemberChargesFinancesWrapperBuilder(serviceFactory).setSelectedDate(selectedDate).setMember(member).build();

    }

    @RequestMapping(value="/{memberID}/addMiscCharge", method=RequestMethod.POST)
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

    @RequestMapping(value="/dropMiscCharge/{chargeID}", method=RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void dropMiscCharges(@PathVariable("chargeID") int chargeID){
        Charge charge = chargeService.get(chargeID);
        chargeService.remove(charge);
    }

    @RequestMapping(value="/discountCharge/{chargeID}", method=RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void discountCharge(@PathVariable("chargeID") int chargeID, @RequestParam("discount") BigDecimal discountAmount){
        Charge charge = chargeService.get(chargeID);
        charge.setDiscountAmount(discountAmount);
        chargeService.update(charge);
    }

    @RequestMapping(value="/getChargeList/{memberID}")
    @ResponseBody
    public List<Charge> getChargeList(@PathVariable("memberID") int memberID, @RequestParam("month") Integer month, @RequestParam("year") Integer year){
        LocalDate cycleStartDate = LocalDate.of(year, month, 1);
        return chargeService.getChargesByMemberByDate(memberID, cycleStartDate);
    }
}
