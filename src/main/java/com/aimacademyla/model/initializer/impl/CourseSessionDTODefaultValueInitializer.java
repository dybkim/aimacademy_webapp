package com.aimacademyla.model.initializer.impl;

import com.aimacademyla.dao.flow.impl.MemberCourseRegistrationDAOAccessFlow;
import com.aimacademyla.dao.flow.impl.MemberMonthlyRegistrationDAOAccessFlow;
import com.aimacademyla.model.*;
import com.aimacademyla.model.builder.entity.AttendanceBuilder;
import com.aimacademyla.model.dto.CourseSessionDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CourseSessionDTODefaultValueInitializer extends GenericDefaultValueInitializerImpl<CourseSessionDTO>{

    private Course course;
    private LocalDate cycleStartDate;
    private static final Logger logger = LogManager.getLogger(CourseSessionDTODefaultValueInitializer.class.getName());

    @Override
    public CourseSessionDTO initialize() {
        CourseSessionDTO courseSessionDTO = new CourseSessionDTO();
        List<Attendance> attendanceList = populateAttendanceList();

        courseSessionDTO.setCourse(course);
        courseSessionDTO.setAttendanceList(attendanceList);
        return courseSessionDTO;
    }

    public CourseSessionDTODefaultValueInitializer setCourse(Course course){
        this.course = course;
        return this;
    }

    public CourseSessionDTODefaultValueInitializer setCycleStartDate(LocalDate cycleStartDate){
        this.cycleStartDate = cycleStartDate;
        return this;
    }

    @SuppressWarnings("unchecked")
    private List<Attendance> populateAttendanceList(){
        List<Attendance> attendanceList = new ArrayList<>();

        if(course.getCourseID() != Course.OPEN_STUDY_ID) {
            List<MemberCourseRegistration> memberCourseRegistrationList = new MemberCourseRegistrationDAOAccessFlow()
                    .addQueryParameter(course)
                    .getList();

            for (MemberCourseRegistration memberCourseRegistration : memberCourseRegistrationList) {
                if (!memberCourseRegistration.getIsEnrolled())
                    continue;

                Attendance attendance = new AttendanceBuilder()
                        .setMember(memberCourseRegistration.getMember())
                        .setWasPresent(false)
                        .build();

                attendanceList.add(attendance);
            }
        }

        else{
            List<MemberMonthlyRegistration> memberMonthlyRegistrationList = new MemberMonthlyRegistrationDAOAccessFlow()
                    .addQueryParameter(cycleStartDate)
                    .getList();

            for(MemberMonthlyRegistration memberMonthlyRegistration : memberMonthlyRegistrationList){
                Attendance attendance = new AttendanceBuilder()
                        .setMember(memberMonthlyRegistration.getMember())
                        .setWasPresent(false)
                        .build();
                attendanceList.add(attendance);
            }
        }
        return attendanceList;
    }
}
