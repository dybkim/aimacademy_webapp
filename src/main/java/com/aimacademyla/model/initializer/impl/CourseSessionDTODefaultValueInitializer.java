package com.aimacademyla.model.initializer.impl;

import com.aimacademyla.dao.factory.DAOFactory;
import com.aimacademyla.dao.flow.impl.MemberCourseRegistrationDAOAccessFlow;
import com.aimacademyla.model.Attendance;
import com.aimacademyla.model.Course;
import com.aimacademyla.model.MemberCourseRegistration;
import com.aimacademyla.model.builder.entity.AttendanceBuilder;
import com.aimacademyla.model.dto.CourseSessionDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class CourseSessionDTODefaultValueInitializer extends GenericDefaultValueInitializerImpl<CourseSessionDTO>{

    private Course course;
    private static final Logger logger = LogManager.getLogger(CourseSessionDTODefaultValueInitializer.class.getName());

    public CourseSessionDTODefaultValueInitializer(DAOFactory daoFactory) {
        super(daoFactory);
    }

    @Override
    @SuppressWarnings("unchecked")
    public CourseSessionDTO initialize() {
        CourseSessionDTO courseSessionDTO = new CourseSessionDTO();
        List<Attendance> attendanceList = new ArrayList<>();
        List<MemberCourseRegistration> memberCourseRegistrationList = new MemberCourseRegistrationDAOAccessFlow(getDAOFactory())
                .addQueryParameter(course)
                .getList();

        logger.debug("Number of Members enrolled in Course: " + memberCourseRegistrationList.size());
        for(MemberCourseRegistration memberCourseRegistration : memberCourseRegistrationList){
            if(!memberCourseRegistration.getIsEnrolled())
                continue;

            Attendance attendance = new AttendanceBuilder()
                    .setMember(memberCourseRegistration.getMember())
                    .setWasPresent(false)
                    .build();

            logger.debug("Member is: " + attendance.getMember().getMemberFirstName() + " " + attendance.getMember().getMemberLastName());
            attendanceList.add(attendance);
        }

        courseSessionDTO.setCourse(course);
        courseSessionDTO.setAttendanceList(attendanceList);
        return courseSessionDTO;
    }

    public CourseSessionDTODefaultValueInitializer setCourse(Course course){
        this.course = course;
        return this;
    }
}
