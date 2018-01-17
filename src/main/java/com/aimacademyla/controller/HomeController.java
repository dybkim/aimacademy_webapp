package com.aimacademyla.controller;

import com.aimacademyla.controller.resources.HomeResources;
import com.aimacademyla.dao.ChargeLineDAO;
import com.aimacademyla.dao.MemberDAO;
import com.aimacademyla.dao.PaymentDAO;
import com.aimacademyla.model.ChargeLine;
import com.aimacademyla.model.Member;
import com.aimacademyla.model.Payment;
import com.aimacademyla.model.builder.dto.OutstandingChargesPaymentDTOBuilder;
import com.aimacademyla.model.dto.OutstandingChargesPaymentDTO;
import com.aimacademyla.model.reference.TemporalReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Created by davidkim on 1/17/17.
 */

@Controller
@RequestMapping("/admin")
public class HomeController {

    @RequestMapping("/home")
    public String home(Model model,
                       @RequestParam(name="month", required = false) Integer month,
                       @RequestParam(name="year", required = false) Integer year){

        LocalDate now = LocalDate.now();
        LocalDate cycleStartDate = LocalDate.of(now.getYear(), now.getMonthValue(), 1);
        LocalDate cycleEndDate = LocalDate.of(now.getYear(), now.getMonthValue(), now.getMonth().length(now.isLeapYear()));

        if(month != null && year != null) {
            cycleStartDate = LocalDate.of(year, month, 1);
            cycleEndDate = LocalDate.of(year, month, cycleStartDate.getMonth().length(cycleStartDate.isLeapYear()));
        }

        OutstandingChargesPaymentDTO outstandingChargesPaymentDTO = new OutstandingChargesPaymentDTOBuilder()
                                                                        .setCycleStartDate(cycleStartDate)
                                                                        .setCycleEndDate(cycleEndDate)
                                                                        .build();

        List<LocalDate> monthsList = TemporalReference.getMonthList();
        Collections.reverse(monthsList);
        String cycleString = cycleStartDate.getMonth() + " " + cycleStartDate.getYear();

        model.addAttribute("cycleString", cycleString);
        model.addAttribute("cycleStartDate", cycleStartDate);
        model.addAttribute("cycleEndDate", cycleEndDate);
        model.addAttribute("monthsList", monthsList);
        model.addAttribute("outstandingChargesPaymentDTO", outstandingChargesPaymentDTO);

        return "home";
    }

    @RequestMapping("/home/fetchPeriod")
    public String getPeriodSummary(Model model, @RequestParam(name="cycleStartDate") String cycleStartDateString,
                                                @RequestParam(name="cycleEndDate") String cycleEndDateString){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM/d/yyyy");

        LocalDate cycleStartDate = LocalDate.parse(cycleStartDateString, dateTimeFormatter);
        LocalDate cycleEndDate = LocalDate.parse(cycleEndDateString, dateTimeFormatter);

        OutstandingChargesPaymentDTO outstandingChargesPaymentDTO = new OutstandingChargesPaymentDTOBuilder()
                                                                            .setCycleStartDate(cycleStartDate)
                                                                            .setCycleEndDate(cycleEndDate)
                                                                            .build();

        List<LocalDate> monthsList = TemporalReference.getMonthList();
        Collections.reverse(monthsList);
        String cycleString = cycleStartDate.getMonth() + " " + Integer.toString(cycleStartDate.getDayOfMonth()) + ", " + cycleStartDate.getYear() + " - "
                + cycleEndDate.getMonth() + " " + cycleEndDate.getDayOfMonth() + ", " + cycleEndDate.getYear();

        model.addAttribute("cycleString", cycleString);
        model.addAttribute("cycleStartDate", cycleStartDate);
        model.addAttribute("cycleEndDate", cycleEndDate);
        model.addAttribute("monthsList", monthsList);
        model.addAttribute("outstandingChargesPaymentDTO", outstandingChargesPaymentDTO);

        return "home";
    }
}


