package com.aimacademyla.service.impl;

import com.aimacademyla.dao.CourseSessionDAO;
import com.aimacademyla.model.Course;
import com.aimacademyla.model.CourseSession;
import com.aimacademyla.service.CourseSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by davidkim on 3/1/17.
 */

@Service
public class CourseSessionServiceImpl implements CourseSessionService{

    private CourseSessionDAO courseSessionDAO;

    @Autowired
    public CourseSessionServiceImpl(CourseSessionDAO courseSessionDAO){
        this.courseSessionDAO = courseSessionDAO;
    }

    @Override
    public List<CourseSession> getCourseSessionsForCourse(Course course){
        return courseSessionDAO.getCourseSessionsForCourse(course);
    }

    @Override
    public CourseSession getCourseSessionByID(int courseSessionID){
        return courseSessionDAO.getCourseSessionByID(courseSessionID);
    }

    @Override
    public int generateCourseSessionIDAfterSave(CourseSession courseSession){
        return courseSessionDAO.generateCourseSessionIDAfterSave(courseSession);
    }

    @Override
    public void addCourseSession(CourseSession courseSession){
        courseSessionDAO.addCourseSession(courseSession);
    }


    @Override
    public void deleteCourseSession(CourseSession courseSession){
        courseSessionDAO.deleteCourseSession(courseSession);
    }

    @Override
    public void deleteCourseSession(int courseSessionID){
        courseSessionDAO.deleteCourseSession(courseSessionID);
    }

    @Override
    public void editCourseSession(CourseSession courseSession){
        courseSessionDAO.editCourseSession(courseSession);
    }

}
