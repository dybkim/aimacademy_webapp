package com.aimacademyla.dao.impl;

import com.aimacademyla.controller.course.CourseHomeController;
import com.aimacademyla.dao.CourseSessionDAO;
import com.aimacademyla.model.Charge;
import com.aimacademyla.model.Course;
import com.aimacademyla.model.CourseSession;
import com.aimacademyla.model.enums.AIMEntityType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by davidkim on 3/1/17.
 */

@Transactional
@Repository("courseSessionDAO")
public class CourseSessionDAOImpl extends GenericDAOImpl<CourseSession, Integer> implements CourseSessionDAO{
    public CourseSessionDAOImpl(){
        super(CourseSession.class);
    }

    @Override
    public void removeList(List<CourseSession> courseSessionList){
        Session session = currentSession();
        List<Integer> courseSessionIDList = new ArrayList<>();
        for(CourseSession courseSession : courseSessionList)
            courseSessionIDList.add(courseSession.getCourseSessionID());
        Query query = session.createQuery("DELETE FROM Course_Session C WHERE C.courseSessionID in :courseSessionIDList");
        query.setParameterList("courseSessionIDList", courseSessionIDList);
        query.executeUpdate();
    }

    @Override
    public CourseSession getEager(Integer courseSessionID){
        return loadCollections(get(courseSessionID));
    }

    @Override
    public Collection<CourseSession> loadCollections(Collection<CourseSession> courseSessionCollection){
        Session session = currentSession();
        Set<CourseSession> courseSessionSet = new HashSet<>();
        for(CourseSession courseSession : courseSessionCollection){
            courseSession = session.get(CourseSession.class, courseSession.getCourseSessionID());
            Hibernate.initialize(courseSession.getAttendanceMap());
            courseSessionSet.add(courseSession);
        }
        return courseSessionSet;
    }

    @Override
    public CourseSession loadCollections(CourseSession courseSession){
        Session session = currentSession();
        courseSession = session.get(CourseSession.class, courseSession.getCourseSessionID());
        Hibernate.initialize(courseSession.getAttendanceMap());
        session.flush();

        return courseSession;
    }

    @Override
    public CourseSession loadCollection(CourseSession courseSession, Class classType){
        return loadCollections(courseSession);
    }
}
