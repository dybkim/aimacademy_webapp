package com.aimacademyla.dao.impl;

import com.aimacademyla.dao.StudentRegistrationDAO;
import com.aimacademyla.model.Course;
import com.aimacademyla.model.StudentRegistration;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by davidkim on 2/9/17.
 */

@Repository("studentRegistrationDAO")
@Transactional
public class StudentRegistrationDAOImpl implements StudentRegistrationDAO {

    private SessionFactory sessionFactory;

    @Autowired
    public StudentRegistrationDAOImpl(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    public List<StudentRegistration> getStudentRegistrationsForCourse(Course course){
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("FROM Student_Registration WHERE CourseID = :courseID");
        query.setParameter("courseID", course.getCourseID());
        List<StudentRegistration> studentRegistrationList = query.getResultList();

        System.out.println("number of students enrolled: " + studentRegistrationList.size());

        session.flush();

        return studentRegistrationList;
    }


}
