package com.aimacademyla.controller.resources;

import com.aimacademyla.model.builder.dto.OutstandingChargesPaymentDTOBuilder;
import com.aimacademyla.model.dto.OutstandingChargesPaymentDTO;
import com.aimacademyla.model.temporal.CyclePeriod;
import org.hibernate.type.LocalDateType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;

@Controller
@RequestMapping("/admin/home/rest")
public class HomeResources {

    @RequestMapping("/memberChargesList")
    @ResponseBody
    public OutstandingChargesPaymentDTO getMemberChargesList(@RequestParam("cycleStartDate") String cycleStartDateString,
                                                             @RequestParam("cycleEndDate") String cycleEndDateString){
        LocalDate cycleStartDate = LocalDate.parse(cycleStartDateString);
        LocalDate cycleEndDate = LocalDate.parse(cycleEndDateString);
        CyclePeriod cyclePeriod = new CyclePeriod(cycleStartDate, cycleEndDate);
        return new OutstandingChargesPaymentDTOBuilder().setCyclePeriod(cyclePeriod).build();
    }
}

