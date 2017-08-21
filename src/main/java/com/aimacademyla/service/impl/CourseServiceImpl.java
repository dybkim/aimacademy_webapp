package com.aimacademyla.service.impl;

import com.aimacademyla.dao.ChargeDAO;
import com.aimacademyla.dao.CourseDAO;
import com.aimacademyla.dao.GenericDAO;
import com.aimacademyla.model.Charge;
import com.aimacademyla.model.Course;
import com.aimacademyla.service.ChargeService;
import com.aimacademyla.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * Created by davidkim on 2/8/17.
 */

@Service
public class CourseServiceImpl extends GenericServiceImpl<Course, Integer> implements CourseService {

    private CourseDAO courseDAO;

    @Autowired
    public CourseServiceImpl(@Qualifier("courseDAO") GenericDAO<Course, Integer> genericDAO){
        super(genericDAO);
        this.courseDAO = (CourseDAO) genericDAO;
    }

    @Override
    public List<Course> getCourseList() {
        return courseDAO.getCourseList();
    }

    @Override
    public Course getCourseByName(String courseName) {
        return courseDAO.getCourseByName(courseName);
    }

    @Override
    public List<Course> getCourseListBySeason(int seasonID){
        return courseDAO.getCourseListBySeason(seasonID);
    }

    @Override
    public List<Course> getCourseListByDate(LocalDate date){
        return courseDAO.getCourseListByDate(date);
    }
}
