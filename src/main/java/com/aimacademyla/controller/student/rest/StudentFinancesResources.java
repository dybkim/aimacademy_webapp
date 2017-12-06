package com.aimacademyla.controller.student.rest;

import com.aimacademyla.dao.factory.DAOFactory;
import com.aimacademyla.model.Charge;
import com.aimacademyla.model.Course;
import com.aimacademyla.model.Member;
import com.aimacademyla.model.MonthlyFinancesSummary;
import com.aimacademyla.model.builder.dto.MemberChargesFinancesDTOBuilder;
import com.aimacademyla.model.builder.entity.ChargeBuilder;
import com.aimacademyla.model.dto.MemberChargesFinancesDTO;
import com.aimacademyla.model.enums.BillableUnitType;
import com.aimacademyla.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

/**
 * StudentFinancesResources is a resource provider for requests sent to the studentFinancesController
 */

@Controller
@RequestMapping("/admin/student/rest/studentFinances")
public class StudentFinancesResources {

    private DAOFactory daoFactory;

    private MemberService memberService;
    private ChargeService chargeService;
    private CourseService courseService;
    private MonthlyFinancesSummaryService monthlyFinancesSummaryService;

    @Autowired
    public StudentFinancesResources(MemberService memberService,
                                    ChargeService chargeService,
                                    CourseService courseService,
                                    MonthlyFinancesSummaryService monthlyFinancesSummaryService,
                                    DAOFactory daoFactory){
        this.daoFactory = daoFactory;
        this.courseService = courseService;
        this.memberService = memberService;
        this.chargeService = chargeService;
        this.monthlyFinancesSummaryService = monthlyFinancesSummaryService;
    }

    @RequestMapping("/{memberID}")
    public @ResponseBody
    MemberChargesFinancesDTO fetchMemberCharges(@PathVariable("memberID") int memberID,
                                                @RequestParam(name = "month") int month,
                                                @RequestParam(name = "year") int year){
        LocalDate selectedDate = LocalDate.of(year, month, 1);
        Member member = memberService.get(memberID);

        //Need to remove circular references to allow JSON object to be instantiated properly
//        member.removeCircularReferences();
        return new MemberChargesFinancesDTOBuilder(daoFactory)
                    .setSelectedDate(selectedDate)
                    .setMember(member)
                    .build();

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
        Course course = courseService.get(Course.OTHER_ID);
        MonthlyFinancesSummary monthlyFinancesSummary = monthlyFinancesSummaryService.getMonthlyFinancesSummary(selectedDate);

        Charge charge = new ChargeBuilder()
                            .setMember(memberService.get(memberID))
                            .setMonthlyFinancesSummary(monthlyFinancesSummary)
                            .setChargeAmount(chargeAmount)
                            .setDiscountAmount(chargeDiscount)
                            .setCourse(course)
                            .setCycleStartDate(selectedDate)
                            .setBillableUnitsBilled(BigDecimal.ONE)
                            .setNumChargeLines(0)
                            .setBillableUnitsType(BillableUnitType.PER_SESSION.toString())
                            .build();

        chargeService.addCharge(charge);
    }

    @RequestMapping(value="/dropMiscCharge/{chargeID}", method=RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void dropMiscCharges(@PathVariable("chargeID") int chargeID){
        Charge charge = chargeService.get(chargeID);
        chargeService.removeCharge(charge);
    }

    @RequestMapping(value="/discountCharge/{chargeID}", method=RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void discountCharge(@PathVariable("chargeID") int chargeID, @RequestParam("discount") BigDecimal discountAmount){
        Charge charge = chargeService.get(chargeID);
        charge.setDiscountAmount(discountAmount);
        chargeService.updateCharge(charge);
    }

    @RequestMapping(value="/getChargeList/{memberID}")
    @ResponseBody
    public List<Charge> getChargeList(@PathVariable("memberID") int memberID, @RequestParam("month") Integer month, @RequestParam("year") Integer year){
        LocalDate cycleStartDate = LocalDate.of(year, month, 1);
        Member member = memberService.get(memberID);
        return chargeService.getList(member, cycleStartDate);
    }
}
