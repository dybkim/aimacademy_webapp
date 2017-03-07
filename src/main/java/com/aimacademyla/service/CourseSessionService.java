package com.aimacademyla.service;

import com.aimacademyla.model.Course;
import com.aimacademyla.model.CourseSession;

import java.util.List;

/**
 * Created by davidkim on 3/1/17.
 */
public interface CourseSessionService {

    List<CourseSession> getCourseSessionsForCourse(Course course);

    CourseSession getCourseSessionByID(int courseSessionID);

    void addCourseSession(CourseSession courseSession);

    void deleteCourseSession(CourseSession courseSession);

    void editCourseSession(CourseSession courseSession);
}
