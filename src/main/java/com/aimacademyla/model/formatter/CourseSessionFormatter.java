package com.aimacademyla.model.formatter;

import com.aimacademyla.model.CourseSession;
import com.aimacademyla.service.CourseSessionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

@Component
public class CourseSessionFormatter implements Formatter<CourseSession> {
    private static final Logger logger = LogManager.getLogger(CourseSessionFormatter.class.getName());

    private CourseSessionService courseSessionService;

    @Autowired
    public CourseSessionFormatter(CourseSessionService courseSessionService){
        this.courseSessionService = courseSessionService;
    }
    @Override
    public CourseSession parse(String courseSessionID, Locale locale) throws ParseException {
        logger.debug("parsing courseSessionID: " + courseSessionID);
        return courseSessionService.get(Integer.parseInt(courseSessionID));
    }

    @Override
    public String print(CourseSession courseSession, Locale locale) {
        return courseSession.toString();
    }
}
