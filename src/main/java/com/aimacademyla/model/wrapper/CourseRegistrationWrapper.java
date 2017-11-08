package com.aimacademyla.model.wrapper;

import com.aimacademyla.model.Course;

import java.io.Serializable;
import java.util.List;

/**
 * Created by davidkim on 7/5/17.
 */
public class CourseRegistrationWrapper implements Serializable{

    private static final long serialVersionUID = 689499054674508785L;

    private Course course;

    private List<CourseRegistrationWrapperListItem> courseRegistrationWrapperObjectList;

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public List<CourseRegistrationWrapperListItem> getCourseRegistrationWrapperObjectList() {
        return courseRegistrationWrapperObjectList;
    }

    public void setCourseRegistrationWrapperObjectList(List<CourseRegistrationWrapperListItem> courseRegistrationWrapperObjectList) {
        this.courseRegistrationWrapperObjectList = courseRegistrationWrapperObjectList;
    }
}
