package com.aimacademyla.service.impl;

import com.aimacademyla.dao.*;
import com.aimacademyla.dao.flow.impl.ChargeDAOAccessFlow;
import com.aimacademyla.dao.flow.impl.MemberMonthlyRegistrationDAOAccessFlow;
import com.aimacademyla.model.*;
import com.aimacademyla.model.builder.entity.ChargeLineBuilder;
import com.aimacademyla.model.dto.CourseSessionDTO;
import com.aimacademyla.service.ChargeLineService;
import com.aimacademyla.service.CourseSessionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by davidkim on 3/1/17.
 */

@Service
public class CourseSessionServiceImpl extends GenericServiceImpl<CourseSession, Integer> implements CourseSessionService{

    private static Logger logger = LogManager.getLogger(CourseSessionServiceImpl.class);

    private CourseSessionDAO courseSessionDAO;
    private AttendanceDAO attendanceDAO;
    private ChargeLineService chargeLineService;
    private CourseDAO courseDAO;
    private ChargeLineDAO chargeLineDAO;
    private ChargeDAO chargeDAO;

    @Autowired
    public CourseSessionServiceImpl(@Qualifier("courseSessionDAO") GenericDAO<CourseSession, Integer> genericDAO,
                                    AttendanceDAO attendanceDAO,
                                    ChargeLineService chargeLineService,
                                    ChargeLineDAO chargeLineDAO,
                                    CourseDAO courseDAO,
                                    ChargeDAO chargeDAO){
        super(genericDAO);
        this.courseSessionDAO = (CourseSessionDAO) genericDAO;
        this.attendanceDAO = attendanceDAO;
        this.chargeLineService = chargeLineService;
        this.chargeLineDAO = chargeLineDAO;
        this.courseDAO = courseDAO;
        this.chargeDAO = chargeDAO;
    }

    /*
     * addCourseSession introduces some tight coupling between CourseSessionService and the entity fields of CourseSessionDTO mainly due
     * to the fact that CourseSessionDTO's Attendance List is required before CourseSession's Attendance Map can be utilized properly.
     */
    @Override
    public  void addCourseSession(CourseSessionDTO courseSessionDTO){
        List<Attendance> attendanceList = courseSessionDTO.getAttendanceList();
        Course course = courseDAO.getEager(courseSessionDTO.getCourse().getCourseID());
        LocalDate courseSessionDate = courseSessionDTO.getCourseSessionDate();

        //Persist courseSession to generate ID
        CourseSession courseSession = courseSessionDTO.getCourseSession();
        courseSessionDAO.add(courseSession);

        /*
         * Adds new Attendance for new CourseSession
         * Updates ChargeLines for each Member's Attendance
         */
        for(Attendance attendance : attendanceList){
            attendance.setAttendanceDate(courseSessionDate);
            attendance.setCourseSession(courseSession);
            //Persist attendance to generate unique IDs for attendance
            attendanceDAO.add(attendance);

            //Skip adding ChargeLine if Course is Open Study
            if(attendance.getWasPresent() && course.getCourseID() != Course.OPEN_STUDY_ID) {
                Member member = attendance.getMember();
                MemberMonthlyRegistration memberMonthlyRegistration = (MemberMonthlyRegistration) new MemberMonthlyRegistrationDAOAccessFlow()
                                                                            .addQueryParameter(member)
                                                                            .addQueryParameter(courseSessionDate)
                                                                            .get();

                BigDecimal chargeAmount = course.getCourseSessionChargeAmount(memberMonthlyRegistration != null);

                Charge charge = (Charge) new ChargeDAOAccessFlow()
                                            .addQueryParameter(member)
                                            .addQueryParameter(course)
                                            .addQueryParameter(courseSessionDate)
                                            .get();

                ChargeLine chargeLine = new ChargeLineBuilder()
                                            .setAttendance(attendance)
                                            .setChargeAmount(chargeAmount)
                                            .setCharge(charge)
                                            .setBillableUnitsBilled(course.getBillableUnitDuration())
                                            .setDateCharged(courseSessionDate)
                                            .build();

                chargeLineService.addChargeLine(chargeLine);
                attendance.setChargeLine(chargeLine);
            }
            courseSession.updateAttendance(attendance);
        }
        course.addCourseSession(courseSession);
        courseDAO.update(course);
    }

    @Override
    public  void updateCourseSession(CourseSessionDTO courseSessionDTO){
        List<Attendance> attendanceList = courseSessionDTO.getAttendanceList();
        LocalDate courseSessionDate = courseSessionDTO.getCourseSessionDate();
        Course course = courseDAO.getEager(courseSessionDTO.getCourse().getCourseID());
        /*
         * Must load lazy initialized Collection of Attendances for CourseSession
         */
        CourseSession courseSession = courseSessionDAO.getEager(courseSessionDTO.getCourseSessionID());

        for(Attendance attendance : attendanceList) {
            attendance.setAttendanceDate(courseSessionDTO.getCourseSessionDate());

            //Skip adding ChargeLine if Course is Open Study
            if(course.getCourseID() != Course.OPEN_STUDY_ID) {
                ChargeLine chargeLine = chargeLineDAO.get(attendance.getChargeLine().getChargeLineID());
                attendance.setChargeLine(chargeLine);

                Member member = attendance.getMember();

                if (attendance.getWasPresent() && chargeLine == null) {
                    Charge charge = (Charge) new ChargeDAOAccessFlow()
                            .addQueryParameter(member)
                            .addQueryParameter(course)
                            .addQueryParameter(courseSessionDate)
                            .get();

                    charge = chargeDAO.loadCollections(charge);

                    MemberMonthlyRegistration memberMonthlyRegistration = (MemberMonthlyRegistration) new MemberMonthlyRegistrationDAOAccessFlow()
                            .addQueryParameter(member)
                            .addQueryParameter(courseSessionDate)
                            .get();

                    BigDecimal chargeAmount = course.getCourseSessionChargeAmount(memberMonthlyRegistration.getMemberMonthlyRegistrationID() != MemberMonthlyRegistration.INACTIVE);

                    chargeLine = new ChargeLineBuilder()
                            .setAttendance(attendance)
                            .setChargeAmount(chargeAmount)
                            .setCharge(charge)
                            .setBillableUnitsBilled(course.getBillableUnitDuration())
                            .setDateCharged(courseSessionDate)
                            .build();

                    attendance.setChargeLine(chargeLine);
                    chargeLineService.addChargeLine(chargeLine);
                }
                else if(!attendance.getWasPresent() && chargeLine != null){
                        attendance.setChargeLine(null);
                        //Update Attendance with null ChargeLine first before removing ChargeLine
                        attendanceDAO.update(attendance);
                        chargeLineService.removeChargeLine(chargeLine);
                }
            }

            /*
             * For Open Study CourseSessions, set ChargeLine as null for all Attendances so that
             * Hibernate doesn't try to persist new instances (and throw an exception in the process of doing so)
             */
            else
                attendance.setChargeLine(null);

            courseSession.updateAttendance(attendance);
        }
        course.updateCourseSession(courseSession);
        /*
         * Must update CourseSession before updating Course or else Hibernate will produce a merge conflict when updating Course
         */
        courseSessionDAO.update(courseSession);
        courseDAO.update(course);
    }

    @Override
    public void removeCourseSession(CourseSessionDTO courseSessionDTO){
        Course course = courseSessionDTO.getCourse();
        course = courseDAO.loadCollections(course);

        List<Attendance> attendanceList = courseSessionDTO.getAttendanceList();
        CourseSession courseSession = courseSessionDTO.getCourseSession();
        List<ChargeLine> chargeLineList = new ArrayList<>();

        for(Attendance attendance : attendanceList){
            if (attendance.getChargeLine() != null){
                ChargeLine chargeLine = chargeLineService.get(attendance.getChargeLine().getChargeLineID());
                chargeLineList.add(chargeLine);
            }
        }

        //Must remove Attendance first since Attendance has a ZeroToOne relationship with ChargeLine
        course.removeCourseSession(courseSession);
        courseDAO.update(course);

        for(ChargeLine chargeLine : chargeLineList)
            chargeLineService.removeChargeLine(chargeLine);
    }
}
