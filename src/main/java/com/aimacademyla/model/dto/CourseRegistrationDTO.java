package com.aimacademyla.model.dto;

import com.aimacademyla.model.Course;

import java.io.Serializable;
import java.util.List;

/**
 * Created by davidkim on 7/5/17.
 */
public class CourseRegistrationDTO implements Serializable{

    private static final long serialVersionUID = 689499054674508785L;

    private Course course;

    private List<CourseRegistrationDTOListItem> courseRegistrationDTOListItems;

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public List<CourseRegistrationDTOListItem> getCourseRegistrationDTOListItems() {
        return courseRegistrationDTOListItems;
    }

    public void setCourseRegistrationDTOListItems(List<CourseRegistrationDTOListItem> courseRegistrationDTOListItems) {
        this.courseRegistrationDTOListItems = courseRegistrationDTOListItems;
    }

    public int getNumEnrolled(){
        int numEnrolled = 0;

        for(CourseRegistrationDTOListItem courseRegistrationWrapperObject : courseRegistrationDTOListItems)
            if(!courseRegistrationWrapperObject.getIsDropped())
                numEnrolled++;

        return numEnrolled;
    }
}
