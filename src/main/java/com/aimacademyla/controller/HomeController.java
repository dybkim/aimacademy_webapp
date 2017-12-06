package com.aimacademyla.controller;

import com.aimacademyla.dao.factory.DAOFactory;
import com.aimacademyla.model.*;
import com.aimacademyla.model.builder.dto.OutstandingChargesPaymentDTOBuilder;
import com.aimacademyla.model.dto.OutstandingChargesPaymentDTO;
import com.aimacademyla.model.reference.TemporalReference;
import com.aimacademyla.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.*;

/**
 * Created by davidkim on 1/17/17.
 */

@Controller
@RequestMapping("/admin")
public class HomeController {
    private CourseService courseService;
    private DAOFactory daoFactory;

    @Autowired
    public HomeController(CourseService courseService, DAOFactory daoFactory){
        this.courseService = courseService;
        this.daoFactory = daoFactory;
    }

    @RequestMapping("/home")
    public String home(Model model,
                       @RequestParam(name="month", required = false) Integer month,
                       @RequestParam(name="year", required = false) Integer year){

        LocalDate cycleStartDate = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonthValue(), 1);

        if(month != null && year != null)
            cycleStartDate = LocalDate.of(year, month, 1);

        OutstandingChargesPaymentDTO outstandingChargesPaymentDTO = new OutstandingChargesPaymentDTOBuilder(daoFactory)
                                                                        .setCycleStartDate(cycleStartDate)
                                                                        .build();

        HashMap<Integer, Course> courseHashMap = new HashMap<>();

        for(Course course : courseService.getList())
            courseHashMap.put(course.getCourseID(), course);

        List<LocalDate> monthsList = TemporalReference.getMonthList();
        Collections.reverse(monthsList);

        model.addAttribute("cycleStartDate", cycleStartDate);
        model.addAttribute("monthsList", monthsList);
        model.addAttribute("outstandingChargesPaymentDTO", outstandingChargesPaymentDTO);
        model.addAttribute("courseHashMap", courseHashMap);

        return "home";
    }
}


