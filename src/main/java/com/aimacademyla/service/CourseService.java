package com.aimacademyla.service;

import com.aimacademyla.model.Course;
import com.aimacademyla.model.dto.CourseRegistrationDTO;
import com.aimacademyla.model.dto.CourseSessionDTO;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * Created by davidkim on 2/8/17.
 */
public interface CourseService extends GenericService<Course, Integer>{

    void addCourse(Course course);
    void removeCourse(Course course);
    void updateCourse(CourseRegistrationDTO courseRegistrationDTO);

    List<Course> getActiveList();
    List<Course> getInactiveList();
}
