package com.aimacademyla.service;

import com.aimacademyla.model.Course;
import com.aimacademyla.model.StudentRegistration;

import java.util.List;

/**
 * Created by davidkim on 2/9/17.
 */
public interface StudentRegistrationService {

    List<StudentRegistration> getStudentRegistrationsForCourse(Course course);
}
