package com.aimacademyla.service;

import com.aimacademyla.model.Course;

import java.util.List;

/**
 * Created by davidkim on 2/8/17.
 */
public interface CourseService {

    List<Course> getActiveCourseList();

    Course getCourseByName(String courseName);

    Course getCourseByID(int courseID);

    void addCourse(Course course);

    void editCourse(Course course);

    void deleteCourse(Course course);
}
