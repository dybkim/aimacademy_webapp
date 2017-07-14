package com.aimacademyla.service.impl;

import com.aimacademyla.dao.CourseSessionDAO;
import com.aimacademyla.dao.GenericDAO;
import com.aimacademyla.model.Course;
import com.aimacademyla.model.CourseSession;
import com.aimacademyla.service.CourseSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by davidkim on 3/1/17.
 */

@Service
public class CourseSessionServiceImpl extends GenericServiceImpl<CourseSession, Integer> implements CourseSessionService{

    private CourseSessionDAO courseSessionDAO;

    @Autowired
    public CourseSessionServiceImpl(@Qualifier("courseSessionDAO") GenericDAO<CourseSession, Integer> genericDAO){
        super(genericDAO);
        this.courseSessionDAO = (CourseSessionDAO) genericDAO;
    }

    @Override
    public List<CourseSession> getCourseSessionsForCourse(Course course){
        return courseSessionDAO.getCourseSessionsForCourse(course);
    }

    @Override
    public int generateCourseSessionIDAfterSave(CourseSession courseSession){
        return courseSessionDAO.generateCourseSessionIDAfterSave(courseSession);
    }

}
