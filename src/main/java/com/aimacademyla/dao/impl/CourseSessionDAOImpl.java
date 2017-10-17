package com.aimacademyla.dao.impl;

import com.aimacademyla.controller.course.CourseHomeController;
import com.aimacademyla.dao.CourseSessionDAO;
import com.aimacademyla.model.Course;
import com.aimacademyla.model.CourseSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

@Transactional
@Repository("courseSessionDAO")
public class CourseSessionDAOImpl extends GenericDAOImpl<CourseSession, Integer> implements CourseSessionDAO{

    private static final Logger logger = LogManager.getLogger(CourseHomeController.class);

    public CourseSessionDAOImpl(){
        super(CourseSession.class);
    }

    @Override
    public List<CourseSession> getCourseSessionsForCourse(Course course) {
        return getCourseSessionsForCourse(course.getCourseID());
    }

    @Override
    public List<CourseSession> getCourseSessionsForCourse(int courseID){
        Session session = currentSession();
        Query query = session.createQuery("FROM Course_Session WHERE CourseID = :courseID");
        query.setParameter("courseID", courseID);
        List<CourseSession> courseSessionList = query.getResultList();
        session.flush();

        return courseSessionList;
    }

    @Override
    public int generateCourseSessionIDAfterSave(CourseSession courseSession){
        int id;

        Session session = currentSession();
        session.saveOrUpdate(courseSession);
        id = courseSession.getCourseSessionID();

        return id;
    }
}
