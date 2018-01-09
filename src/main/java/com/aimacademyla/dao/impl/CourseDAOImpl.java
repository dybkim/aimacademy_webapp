package com.aimacademyla.dao.impl;

import com.aimacademyla.dao.CourseDAO;
import com.aimacademyla.dao.CourseSessionDAO;
import com.aimacademyla.model.Charge;
import com.aimacademyla.model.Course;
import com.aimacademyla.model.CourseSession;
import com.aimacademyla.model.MemberCourseRegistration;
import com.aimacademyla.model.enums.AIMEntityType;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.*;

/**
 * Created by davidkim on 2/8/17.
 */

@Repository("courseDAO")
@Transactional
public class CourseDAOImpl extends GenericDAOImpl<Course, Integer> implements CourseDAO {

    private CourseSessionDAO courseSessionDAO;

    @Autowired
    public CourseDAOImpl(CourseSessionDAO courseSessionDAO){
        super(Course.class);
        this.courseSessionDAO = courseSessionDAO;
    }

    @Override
    public void removeList(List<Course> courseList){
        Session session = currentSession();
        List<Integer> courseIDList = new ArrayList<>();
        for(Course course : courseList)
            courseIDList.add(course.getCourseID());
        Query query = session.createQuery("DELETE FROM Course C WHERE C.chargeID in :courseIDList");
        query.setParameterList("courseIDList", courseIDList);
        query.executeUpdate();
    }

    @Override
    public Course getEager(Integer courseID){
        return loadCollections(get(courseID));
    }

    @Override
    public Course loadCollections(Course course){
        Session session = currentSession();
        course = session.get(Course.class, course.getCourseID());
        Hibernate.initialize(course.getCourseSessionSet());
        Hibernate.initialize(course.getMemberCourseRegistrationSet());
        session.flush();
        return course;
    }

    @Override
    public Course loadCollection(Course course, Class classType){
        Session session = currentSession();

        if(classType == CourseSession.class){
            course = session.get(Course.class, course.getCourseID());
            Hibernate.initialize(course.getCourseSessionSet());
        }

        if(classType == MemberCourseRegistration.class){
            course = session.get(Course.class, course.getCourseID());
            Hibernate.initialize(course.getMemberCourseRegistrationSet());
        }
        session.flush();

        return course;
    }

    @Override
    public Course loadSubcollections(Course course){
        if(course.getCourseSessionSet() == null || course.getCourseSessionSet().isEmpty())
            return course;

        Set<CourseSession> courseSessionSet = new HashSet<>();

        for(CourseSession courseSession : course.getCourseSessionSet())
            courseSessionSet.add(courseSessionDAO.loadCollections(courseSession));

        course.setCourseSessionSet(courseSessionSet);
        return course;
    }
}
