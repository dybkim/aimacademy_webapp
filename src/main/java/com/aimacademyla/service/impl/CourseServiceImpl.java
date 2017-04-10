package com.aimacademyla.service.impl;

import com.aimacademyla.dao.CourseDAO;
import com.aimacademyla.model.Course;
import com.aimacademyla.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by davidkim on 2/8/17.
 */

@Service
public class CourseServiceImpl implements CourseService {

    private CourseDAO courseDAO;

    @Autowired
    public CourseServiceImpl(CourseDAO courseDAO){
        this.courseDAO = courseDAO;
    }

    @Override
    public List<Course> getActiveCourseList() {
        return courseDAO.getActiveCourseList();
    }

    @Override
    public Course getCourseByName(String courseName) {
        return courseDAO.getCourseByName(courseName);
    }

    @Override
    public Course getCourseByID(int courseID){
        return courseDAO.getCourseByID(courseID);
    }

    @Override
    public int getNumEnrolled(int courseID){
        return courseDAO.getNumEnrolled(courseID);
    }

    @Override
    public void addCourse(Course course) {
        courseDAO.addCourse(course);
    }

    @Override
    public void editCourse(Course course) {
        courseDAO.editCourse(course);
    }

    @Override
    public void deleteCourse(Course course) {
        courseDAO.deleteCourse(course);
    }
}
