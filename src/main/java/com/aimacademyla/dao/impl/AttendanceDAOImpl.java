package com.aimacademyla.dao.impl;

import com.aimacademyla.dao.AttendanceDAO;
import com.aimacademyla.model.Attendance;
import com.aimacademyla.model.Course;
import com.aimacademyla.model.CourseSession;
import com.aimacademyla.model.Member;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by davidkim on 3/2/17.
 */

@Repository("attendanceDAO")
@Transactional
public class AttendanceDAOImpl implements AttendanceDAO{

    private SessionFactory sessionFactory;

    @Autowired
    public AttendanceDAOImpl(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Attendance> getAttendanceForCourse(Course course) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("FROM Attendance WHERE courseSessionID IN (SELECT courseSessionID FROM Course_Session WHERE courseID = :courseID)");
        query.setParameter("courseID", course.getCourseID());
        List<Attendance> attendanceList = query.getResultList();
        session.flush();

        return attendanceList;
    }

    @Override
    public List<Attendance> getAttendanceForCourse(Course course, Date date) {
        return null;
    }

    @Override
    public List<Attendance> getAttendanceForCourseSession(CourseSession courseSession) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("FROM Attendance WHERE courseSessionID = :courseSessionID");
        query.setParameter("courseSessionID", courseSession.getCourseSessionID());
        List<Attendance> attendanceList = query.getResultList();

        session.flush();

        return attendanceList;
    }

    @Override
    public List<Attendance> getAttendanceForMember(Member member) {
        return null;
    }

    @Override
    public List<Attendance> getAttendanceForMember(Member member, Course course) {
        return null;
    }

    @Override
    public List<Attendance> getAttendanceForMember(Member member, Course course, Date date) {
        return null;
    }

    @Override
    public void addAttendance(Attendance attendance) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(attendance);
        session.flush();
    }

    @Override
    public void addOrUpdateAttendanceList(List<Attendance> attendanceList){
        Session session = sessionFactory.getCurrentSession();
        for(Attendance attendance : attendanceList)
            session.saveOrUpdate(attendance);
        session.flush();
    }

    @Override
    public void editAttendance(Attendance attendance) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(attendance);
        session.flush();
    }

    @Override
    public void deleteAttendance(Attendance attendance) {
        Session session = sessionFactory.getCurrentSession();
        session.remove(attendance);
        session.flush();
    }
}
