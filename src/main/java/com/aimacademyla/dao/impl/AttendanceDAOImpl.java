package com.aimacademyla.dao.impl;

import com.aimacademyla.dao.AttendanceDAO;
import com.aimacademyla.model.Attendance;
import com.aimacademyla.model.Course;
import com.aimacademyla.model.CourseSession;
import com.aimacademyla.model.Member;
import com.aimacademyla.model.enums.AIMEntityType;
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
 * Created by davidkim on 3/2/17.
 */

@Repository("attendanceDAO")
@Transactional
public class AttendanceDAOImpl extends GenericDAOImpl<Attendance,Integer> implements AttendanceDAO{

    private final AIMEntityType AIM_ENTITY_TYPE = AIMEntityType.ATTENDANCE;

    public AttendanceDAOImpl(){
        super(Attendance.class);
    }

    @Override
    public List<Attendance> getAttendanceForCourse(Course course) {
        Session session = currentSession();
        Query query = session.createQuery("FROM Attendance WHERE courseSessionID IN (SELECT courseSessionID FROM Course_Session WHERE courseID = :courseID)");
        query.setParameter("courseID", course.getCourseID());
        List<Attendance> attendanceList = query.getResultList();
        session.flush();

        return attendanceList;
    }

    @Override
    public List<Attendance> getAttendanceForCourseForDate(Course course, LocalDate date) {
        return null;
    }

    @Override
    public List<Attendance> getAttendanceForCourseSession(int courseSessionID){
        Session session = currentSession();
        Query query = session.createQuery("FROM Attendance WHERE courseSessionID = :courseSessionID");
        query.setParameter("courseSessionID", courseSessionID);
        List<Attendance> attendanceList = query.getResultList();

        session.flush();

        return attendanceList;
    }

    @Override
    public List<Attendance> getAttendanceForCourseSession(CourseSession courseSession) {
        return getAttendanceForCourseSession(courseSession.getCourseSessionID());
    }

    @Override
    public List<Attendance> getAttendanceForMember(Member member) {
        Session session = currentSession();
        Query query = session.createQuery("FROM Attendance WHERE MemberID = :memberID");
        query.setParameter("memberID", member.getMemberID());
        List<Attendance> attendanceList = query.getResultList();

        session.flush();

        return attendanceList;
    }

    @Override
    public List<Attendance> getAttendanceForMemberForCourse(Member member, Course course) {
        Session session = currentSession();
        Query query = session.createQuery("FROM Attendance WHERE memberID = :memberID AND courseSessionID IN (SELECT courseSessionID FROM Course_Session WHERE courseID = :courseID)");
        query.setParameter("memberID", member.getMemberID());
        query.setParameter("courseID", course.getCourseID());
        List<Attendance> attendanceList = query.getResultList();

        session.flush();

        return attendanceList;
    }

    @Override
    public Attendance getAttendanceForMemberForCourseForDate(Member member, Course course, LocalDate date) {
        return null;
    }

    @Override
    public void addOrUpdateAttendanceList(List<Attendance> attendanceList){
        Session session = currentSession();
        for(Attendance attendance : attendanceList)
            session.saveOrUpdate(attendance);
        session.flush();
    }

    public AIMEntityType getAIMEntityType() {
        return AIM_ENTITY_TYPE;
    }
}
