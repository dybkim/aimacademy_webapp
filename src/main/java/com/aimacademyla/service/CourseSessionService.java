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

    int generateCourseSessionIDAfterSave(CourseSession courseSession);

    void addCourseSession(CourseSession courseSession);

    void deleteCourseSession(CourseSession courseSession);

    void deleteCourseSession(int courseSessionID);

    void editCourseSession(CourseSession courseSession);
}
