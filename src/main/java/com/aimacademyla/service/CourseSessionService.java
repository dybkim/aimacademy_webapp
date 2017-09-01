package com.aimacademyla.service;

import com.aimacademyla.model.Course;
import com.aimacademyla.model.CourseSession;

import java.util.List;

/**
 * Created by davidkim on 3/1/17.
 */
public interface CourseSessionService extends GenericService<CourseSession, Integer> {

    List<CourseSession> getCourseSessionsForCourse(Course course);

    int generateCourseSessionIDAfterSave(CourseSession courseSession);

    void remove(List<CourseSession> courseSessionList);
}
