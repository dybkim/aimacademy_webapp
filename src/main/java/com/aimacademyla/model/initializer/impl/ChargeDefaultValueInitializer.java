package com.aimacademyla.model.initializer.impl;

import com.aimacademyla.dao.CourseDAO;
import com.aimacademyla.dao.SeasonDAO;
import com.aimacademyla.dao.factory.DAOFactory;
import com.aimacademyla.model.Charge;
import com.aimacademyla.model.Course;
import com.aimacademyla.model.Season;
import com.aimacademyla.model.enums.AIMEntityType;
import com.aimacademyla.model.initializer.GenericDefaultValueInitializer;
import com.aimacademyla.service.CourseService;
import com.aimacademyla.service.SeasonService;
import com.aimacademyla.service.factory.ServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ChargeDefaultValueInitializer extends GenericDefaultValueInitializerImpl<Charge>{

    private SeasonDAO seasonDAO;
    private CourseDAO courseDAO;

    private int memberID;
    private int courseID;
    private LocalDate localDate;

    public ChargeDefaultValueInitializer(DAOFactory daoFactory){
        super(daoFactory);
        this.courseDAO = (CourseDAO) getDAOFactory().getDAO(AIMEntityType.COURSE);
        this.seasonDAO= (SeasonDAO) getDAOFactory().getDAO(AIMEntityType.SEASON);
    }

    @Override
    public Charge initialize() {
        Charge charge = new Charge();
        Season season = seasonDAO.getSeason(localDate);

        if(season == null)
            season = seasonDAO.get(Season.NO_SEASON_FOUND);

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
        charge.setBillableUnitsBilled(BigDecimal.ZERO);
        charge.setBillableUnitType(course.getBillableUnitType());
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
