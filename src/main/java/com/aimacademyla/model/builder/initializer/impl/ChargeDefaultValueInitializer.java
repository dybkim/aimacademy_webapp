package com.aimacademyla.model.builder.initializer.impl;

import com.aimacademyla.dao.CourseDAO;
import com.aimacademyla.model.Charge;
import com.aimacademyla.model.Course;
import com.aimacademyla.model.Season;
import com.aimacademyla.model.builder.initializer.GenericDefaultValueInitializer;
import com.aimacademyla.service.SeasonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ChargeDefaultValueInitializer implements GenericDefaultValueInitializer<Charge> {

    private Charge charge;
    private SeasonService seasonService;
    private CourseDAO courseDAO;

    private int memberID;
    private int courseID;
    private LocalDate localDate;

    private ChargeDefaultValueInitializer(){}

    public ChargeDefaultValueInitializer(CourseDAO courseDAO, SeasonService seasonService){
        this.courseDAO = courseDAO;
        this.seasonService = seasonService;
        charge = new Charge();
    }

    @Override
    public Charge initialize() {
        Season season = seasonService.getSeason(localDate);
        Course course = courseDAO.get(courseID);

        if(course.getCourseID() != Course.OTHER_ID)
            charge.setDescription(course.getCourseName() + " (" + course.getCourseType()+")");

        charge.setMemberID(memberID);
        charge.setCourseID(courseID);
        charge.setCycleStartDate(LocalDate.of(localDate.getYear(), localDate.getMonth(), 1));
        charge.setChargeAmount(BigDecimal.valueOf(0));
        charge.setSeasonID(season.getSeasonID());
        charge.setNumChargeLines(0);
        charge.setDiscountAmount(BigDecimal.valueOf(0));
        return charge;
    }

    public ChargeDefaultValueInitializer setMemberID(int memberID) {
        this.memberID = memberID;
        return this;
    }

    public ChargeDefaultValueInitializer setCourseID(int courseID) {
        this.courseID = courseID;
        return this;
    }

    public ChargeDefaultValueInitializer setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
        return this;
    }
}
