package com.aimacademyla.dao;

import com.aimacademyla.model.Course;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * Created by davidkim on 2/8/17.
 */
public interface CourseDAO extends GenericDAO<Course, Integer>{
    List<Course> getActiveCourseList();

    Course getCourseByName(String courseName);

    List<Course> getCourseListByDate(LocalDate date);

    List<Course> getCourseListBySeason(int seasonID);

}
