package com.aimacademyla.controller.resources;

import com.aimacademyla.model.builder.dto.OutstandingChargesPaymentDTOBuilder;
import com.aimacademyla.model.dto.OutstandingChargesPaymentDTO;
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
    public OutstandingChargesPaymentDTO getMemberChargesList(@RequestParam("month") int month,
                                                             @RequestParam("year") int year){
        LocalDate cycleStartDate = LocalDate.of(year, month, 1);
        return new OutstandingChargesPaymentDTOBuilder().setCycleStartDate(cycleStartDate).build();
    }
}

