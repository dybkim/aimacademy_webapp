package com.aimacademyla.dao.impl;

import com.aimacademyla.dao.CourseSessionDAO;
import com.aimacademyla.model.Course;
import com.aimacademyla.model.CourseSession;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by davidkim on 3/1/17.
 */

@Repository("courseSessionDAO")
@Transactional
public class CourseSessionDAOImpl implements CourseSessionDAO{

    private SessionFactory sessionFactory;

    @Autowired
    public CourseSessionDAOImpl(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<CourseSession> getCourseSessionsForCourse(Course course) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("FROM Course_Session WHERE CourseID = :courseID");
        query.setParameter("courseID", course.getCourseID());
        List<CourseSession> courseSessionList = query.getResultList();
        session.flush();

        return courseSessionList;
    }

    @Override
    public CourseSession getCourseSessionByID(int courseSessionID) {
        Session session = sessionFactory.getCurrentSession();
        CourseSession courseSession = session.get(CourseSession.class, courseSessionID);
        session.flush();

        return courseSession;
    }

    @Override
    public void addCourseSession(CourseSession courseSession) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(courseSession);
        session.flush();
    }

    @Override
    public void editCourseSession(CourseSession courseSession) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(courseSession);
        session.flush();
    }

    @Override
    public void deleteCourseSession(CourseSession courseSession) {
        Session session = sessionFactory.getCurrentSession();
        session.remove(courseSession);
        session.flush();
    }


}
