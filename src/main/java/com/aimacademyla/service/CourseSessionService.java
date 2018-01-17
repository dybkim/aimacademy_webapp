package com.aimacademyla.service;

import com.aimacademyla.model.Course;
import com.aimacademyla.model.CourseSession;
import com.aimacademyla.model.dto.CourseSessionDTO;

import javax.validation.constraints.Null;
import java.util.List;

/**
 * Created by davidkim on 3/1/17.
 */
public interface CourseSessionService extends GenericService<CourseSession, Integer> {
    void addCourseSession(CourseSessionDTO courseSessionDTO);
    void updateCourseSession(CourseSessionDTO courseSessionDTO);
    void removeCourseSession(CourseSessionDTO courseSessionDTO);
}
