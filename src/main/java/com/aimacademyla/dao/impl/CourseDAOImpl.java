package com.aimacademyla.dao.impl;

import com.aimacademyla.dao.CourseDAO;
import com.aimacademyla.dao.CourseSessionDAO;
import com.aimacademyla.model.Course;
import com.aimacademyla.model.CourseSession;
import com.aimacademyla.model.MemberCourseRegistration;
import com.aimacademyla.model.id.IDGenerationStrategy;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        course = get(course.getCourseID());
        Hibernate.initialize(course.getCourseSessionSet());
        Hibernate.initialize(course.getMemberCourseRegistrationSet());
        session.flush();
        return course;
    }

    @Override
    public Course loadCollection(Course course, Class classType){
        Session session = currentSession();

        if(classType == CourseSession.class){
            course = get(course.getCourseID());
            Hibernate.initialize(course.getCourseSessionSet());
        }

        if(classType == MemberCourseRegistration.class){
            course = get(course.getCourseID());
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
