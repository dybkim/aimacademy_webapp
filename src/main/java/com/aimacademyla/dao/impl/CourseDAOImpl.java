package com.aimacademyla.dao.impl;

import com.aimacademyla.dao.CourseDAO;
import com.aimacademyla.model.Course;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * Created by davidkim on 2/8/17.
 */

@Repository("courseDAO")
@Transactional
public class CourseDAOImpl extends GenericDAOImpl<Course, Integer> implements CourseDAO {

    public CourseDAOImpl(){
        super(Course.class);
    }

    @Override
    public List<Course> getActiveCourseList() {
        Session session = currentSession();
        Query query = session.createQuery("FROM Course WHERE IsActive = 1");
        List<Course> courseList = query.getResultList();
        session.flush();
        return courseList;
    }

    @Override
    public Course getCourseByName(String courseName) {
        Session session = currentSession();
        Query query = session.createQuery("FROM Course WHERE CourseName = :courseName");
        query.setParameter("courseName", courseName);
        session.flush();

        return (Course) query.uniqueResult();
    }

    @Override
    public List<Course> getCourseListBySeason(int seasonID) {
        Session session = currentSession();
        Query query = session.createQuery("FROM Course WHERE SeasonID = :seasonID");
        query.setParameter("seasonID", seasonID);
        List<Course> courseList = query.getResultList();
        session.flush();

        return courseList;
    }

    @Override
    public List<Course> getCourseListByDate(LocalDate date){
        Session session = currentSession();
        Query query = session.createQuery("FROM Course WHERE CourseStartDate < :date AND CourseEndDate > :date");
        query.setParameter("date",date);
        List<Course> courseList = query.getResultList();
        session.flush();

        return courseList;
    }

}
