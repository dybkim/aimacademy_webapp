package com.aimacademyla.dao;

import com.aimacademyla.model.Course;
import com.aimacademyla.model.StudentRegistration;

import java.util.List;

/**
 * Created by davidkim on 2/9/17.
 */
public interface StudentRegistrationDAO {

    List<StudentRegistration> getStudentRegistrationsForCourse(Course course);
}
