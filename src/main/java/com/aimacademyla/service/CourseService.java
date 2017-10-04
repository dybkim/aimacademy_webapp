package com.aimacademyla.service;

import com.aimacademyla.model.Course;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * Created by davidkim on 2/8/17.
 */
public interface CourseService extends GenericService<Course, Integer>{

    Course getCourseByName(String courseName);

    List<Course> getCourseListBySeason(int seasonID);

    List<Course> getCourseListByDate(LocalDate date);
}
