package com.aimacademyla.controller.student.rest;

import com.aimacademyla.dao.factory.DAOFactory;
import com.aimacademyla.model.Charge;
import com.aimacademyla.model.ChargeLine;
import com.aimacademyla.model.Course;
import com.aimacademyla.model.Member;
import com.aimacademyla.model.builder.dto.MemberChargesFinancesDTOBuilder;
import com.aimacademyla.model.builder.entity.ChargeLineBuilder;
import com.aimacademyla.model.dto.MemberChargesFinancesDTO;
import com.aimacademyla.model.initializer.impl.ChargeDefaultValueInitializer;
import com.aimacademyla.service.ChargeLineService;
import com.aimacademyla.service.ChargeService;
import com.aimacademyla.service.CourseService;
import com.aimacademyla.service.MemberService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * StudentFinancesResources is a resource provider for requests sent to the studentFinancesController
 */

@Controller
@RequestMapping("/admin/student/rest/studentFinances")
public class StudentFinancesResources {

    private static final Logger logger = LogManager.getLogger(StudentFinancesResources.class.getName());
    private DAOFactory daoFactory;

    private MemberService memberService;
    private ChargeService chargeService;
    private ChargeLineService chargeLineService;
    private CourseService courseService;

    @Autowired
    public StudentFinancesResources(MemberService memberService,
                                    ChargeService chargeService,
                                    ChargeLineService chargeLineService,
                                    CourseService courseService,
                                    DAOFactory daoFactory){
        this.daoFactory = daoFactory;
        this.courseService = courseService;
        this.memberService = memberService;
        this.chargeService = chargeService;
        this.chargeLineService = chargeLineService;
    }

    @RequestMapping("/{memberID}")
    public @ResponseBody
    MemberChargesFinancesDTO fetchMemberCharges(@PathVariable("memberID") int memberID,
                                                @RequestParam(name = "month") int month,
                                                @RequestParam(name = "year") int year){
        LocalDate selectedDate = LocalDate.of(year, month, 1);
        Member member = memberService.get(memberID);

        return new MemberChargesFinancesDTOBuilder(daoFactory)
                            .setSelectedDate(selectedDate)
                            .setMember(member)
                            .build();

    }

    @RequestMapping(value="/{memberID}/addMiscCharge", method=RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addMiscCharge(@PathVariable("memberID") int memberID,
                                                @RequestParam(name="chargeDescription") String chargeDescription,
                                                @RequestParam(name="chargeAmount") BigDecimal chargeAmount,
                                                @RequestParam(name="chargeDiscount", required = false) BigDecimal chargeDiscount,
                                                @RequestParam(name="month") int month,
                                                @RequestParam(name="day") int day,
                                                @RequestParam(name="year") int year){
        Member member = memberService.get(memberID);

        LocalDate selectedDate = LocalDate.of(year, month, day);
        LocalDate cycleStartDate = LocalDate.of(year, month, 1);
        Course otherCourse = courseService.get(Course.OTHER_ID);

        Charge charge = new ChargeDefaultValueInitializer(daoFactory)
                                .setCourse(otherCourse)
                                .setLocalDate(cycleStartDate)
                                .setMember(member)
                                .initialize();

        charge.setDescription(chargeDescription + " (Other)");
        charge.setDiscountAmount(chargeDiscount);

        chargeService.add(charge);

        ChargeLine chargeLine = new ChargeLineBuilder()
                .setBillableUnitsBilled(BigDecimal.ONE)
                .setChargeAmount(chargeAmount)
                .setCharge(charge)
                .setDateCharged(selectedDate)
                .build();

        chargeLineService.addChargeLine(chargeLine);
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
    public List<Charge> getChargeList(@PathVariable("memberID") int memberID, @RequestParam("cycleStartDate") String cycleStartDateString, @RequestParam("cycleEndDate") String cycleEndDateString){
        LocalDate cycleStartDate = LocalDate.parse(cycleStartDateString);
        LocalDate cycleEndDate = LocalDate.parse(cycleEndDateString);
        logger.debug("cycleStartDate: " + cycleStartDate + ", cycleEndDate: " + cycleEndDate);
        Member member = memberService.get(memberID);
        return chargeService.getTransientChargeList(member, cycleStartDate, cycleEndDate);
    }
}
