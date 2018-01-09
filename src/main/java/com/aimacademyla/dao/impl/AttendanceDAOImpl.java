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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by davidkim on 3/2/17.
 */

@Repository("attendanceDAO")
@Transactional
public class AttendanceDAOImpl extends GenericDAOImpl<Attendance,Integer> implements AttendanceDAO{

    public AttendanceDAOImpl(){
        super(Attendance.class);
    }

    @Override
    public void removeList(List<Attendance> attendanceList){
        Session session = currentSession();
        List<Integer> attendanceIDList = new ArrayList<>();
        for(Attendance attendance : attendanceList)
            attendanceIDList.add(attendance.getAttendanceID());
        Query query = session.createQuery("DELETE FROM Attendance A WHERE A.attendanceID in :attendanceIDList");
        query.setParameterList("attendanceIDList", attendanceIDList);
        query.executeUpdate();
    }
}
