package com.aimacademyla.model.formatter;

import com.aimacademyla.dao.CourseDAO;
import com.aimacademyla.model.Course;
import com.aimacademyla.service.CourseService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

@Component
public class CourseFormatter implements Formatter<Course> {

    private static final Logger logger = LogManager.getLogger(CourseFormatter.class.getName());
    private CourseService courseService;

    @Autowired
    public CourseFormatter(CourseService courseService){
        logger.debug("courseFormatter initialized");
        this.courseService = courseService;
    }

    @Override
    public String print(Course course, Locale locale) {
        return course.getCourseName();
    }

    @Override
    public Course parse(String courseID, Locale locale) throws ParseException {
        logger.debug("Parsing courseID: " + courseID);
        return courseService.get(Integer.parseInt(courseID));
    }
}
