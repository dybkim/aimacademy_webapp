package com.aimacademyla.service.impl;

import com.aimacademyla.dao.AttendanceDAO;
import com.aimacademyla.dao.GenericDAO;
import com.aimacademyla.model.*;
import com.aimacademyla.model.enums.AIMEntityType;
import com.aimacademyla.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
