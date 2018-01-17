package com.aimacademyla.dao.impl;

import com.aimacademyla.dao.AttendanceDAO;
import com.aimacademyla.model.Attendance;
import com.aimacademyla.model.id.IDGenerationStrategy;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
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
