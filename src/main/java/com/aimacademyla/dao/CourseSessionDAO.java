package com.aimacademyla.dao;

import com.aimacademyla.model.Course;
import com.aimacademyla.model.CourseSession;

import java.util.List;

/**
 * Created by davidkim on 3/1/17.
 */
public interface CourseSessionDAO extends GenericDAO<CourseSession, Integer>{
    List<CourseSession> getCourseSessionsForCourse(Course course);
    List<CourseSession> getCourseSessionsForCourse(int courseID);

    int generateCourseSessionIDAfterSave(CourseSession courseSession);
}
