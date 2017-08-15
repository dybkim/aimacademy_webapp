package com.aimacademyla.controller.resources;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.time.YearMonth;

@Controller
@RequestMapping("/resource/localDateResource")
public class LocalDateResource {

    @RequestMapping("/getDays")
    public Integer getNumDays(LocalDate localDate){
        YearMonth yearMonth = YearMonth.of(localDate.getYear(), localDate.getMonthValue());
        return yearMonth.lengthOfMonth();
    }
}
