package com.aimacademyla.service.impl;

import com.aimacademyla.dao.GenericDAO;
import com.aimacademyla.model.Attendance;
import com.aimacademyla.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Created by davidkim on 3/2/17.
 */

@Service
public class AttendanceServiceImpl extends GenericServiceImpl<Attendance, Integer> implements AttendanceService{

    @Autowired
    public AttendanceServiceImpl(@Qualifier("attendanceDAO") GenericDAO<Attendance, Integer> genericDAO){
        super(genericDAO);
    }
}
