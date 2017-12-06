package com.aimacademyla.controller.resources;

import com.aimacademyla.dao.factory.DAOFactory;
import com.aimacademyla.model.builder.dto.OutstandingChargesPaymentDTOBuilder;
import com.aimacademyla.model.dto.OutstandingChargesPaymentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;

@Controller
@RequestMapping("/admin/home/rest")
public class HomeResources {

    private DAOFactory daoFactory;

    @Autowired
    public HomeResources(DAOFactory daoFactory){
        this.daoFactory = daoFactory;
    }

    @RequestMapping("/memberChargesList")
    @ResponseBody
    public OutstandingChargesPaymentDTO getMemberChargesList(@RequestParam("month") int month,
                                                             @RequestParam("year") int year){
        LocalDate cycleStartDate = LocalDate.of(year, month, 1);
        return new OutstandingChargesPaymentDTOBuilder(daoFactory).setCycleStartDate(cycleStartDate).build();
    }
}

