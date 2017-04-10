package com.aimacademyla.dao.impl;

import com.aimacademyla.dao.CourseDAO;
import com.aimacademyla.model.Course;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by davidkim on 2/8/17.
 */

@Repository("courseDAO")
@Transactional
public class CourseDAOImpl implements CourseDAO {

    private SessionFactory sessionFactory;

    @Autowired
    public CourseDAOImpl(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Course> getActiveCourseList() {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("FROM Course WHERE IsActive = 1");
        List<Course> courseList = query.getResultList();
        session.flush();
        return courseList;
    }

    @Override
    public Course getCourseByName(String courseName) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("FROM Course WHERE CourseName = :courseName");
        query.setParameter("courseName", courseName);
        session.flush();

        return (Course) query.uniqueResult();
    }

    @Override
    public Course getCourseByID(int courseID){
        Session session = sessionFactory.getCurrentSession();
        Course course = session.get(Course.class, courseID);
        session.flush();

        return course;
    }

    @Override
    public int getNumEnrolled(int courseID){
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("SELECT COUNT(*) FROM Student_Registration WHERE CourseID = :courseID");
        query.setParameter("courseID", courseID);
        int numEnrolled = ((Long)query.getSingleResult()).intValue();
        session.flush();

        return numEnrolled;
    }

    @Override
    public void addCourse(Course course) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(course);
        session.flush();
    }

    @Override
    public void editCourse(Course course) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(course);
        session.flush();
    }

    @Override
    public void deleteCourse(Course course) {
        Session session = sessionFactory.getCurrentSession();
        session.remove(course);
        session.flush();
    }
}
