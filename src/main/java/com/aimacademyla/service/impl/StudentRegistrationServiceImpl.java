package com.aimacademyla.service.impl;

import com.aimacademyla.dao.StudentRegistrationDAO;
import com.aimacademyla.model.Course;
import com.aimacademyla.model.StudentRegistration;
import com.aimacademyla.service.StudentRegistrationService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by davidkim on 2/13/17.
 */

@Service
public class StudentRegistrationServiceImpl implements StudentRegistrationService {

    private StudentRegistrationDAO studentRegistrationDAO;

    public StudentRegistrationServiceImpl(StudentRegistrationDAO studentRegistrationDAO){
        this.studentRegistrationDAO = studentRegistrationDAO;
    }

    @Override
    public List<StudentRegistration> getStudentRegistrationsForCourse(Course course) {
        return studentRegistrationDAO.getStudentRegistrationsForCourse(course);
    }
}
