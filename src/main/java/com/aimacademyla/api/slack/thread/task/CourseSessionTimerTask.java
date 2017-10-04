package com.aimacademyla.api.slack.thread.task;

import com.aimacademyla.api.slack.response.impl.CourseSessionAttendanceAttachmentBuilder;
import com.aimacademyla.model.Course;
import com.aimacademyla.model.CourseSession;
import com.ullink.slack.simpleslackapi.SlackAttachment;
import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackPreparedMessage;
import com.ullink.slack.simpleslackapi.SlackSession;

import java.time.LocalDate;
import java.util.TimerTask;

public class CourseSessionTimerTask extends SlackTimerTask{

    private Course course;

    public CourseSessionTimerTask(SlackSession slackSession, Course course, SlackChannel slackChannel){
        super(slackSession, slackChannel);
        this.course = course;
    }

    @Override
    public void run(){
        CourseSession courseSession = new CourseSession();
        LocalDate courseSessionDate = LocalDate.now();
        courseSession.setCourseSessionDate(courseSessionDate);
        courseSession.setCourseID(course.getCourseID());

        SlackAttachment courseSessionAttendanceAttachment = new CourseSessionAttendanceAttachmentBuilder(courseSession).build();
        SlackPreparedMessage slackPreparedMessage = new SlackPreparedMessage.Builder().withMessage(course.getCourseName() +  " " + courseSessionDate.getMonthValue() + "/" + courseSessionDate.getDayOfMonth() + "/" + courseSessionDate.getYear()).addAttachment(courseSessionAttendanceAttachment).build();
        slackSession.sendMessage(slackChannel, slackPreparedMessage);
    }

    public void setCourseSession(Course course) {
        this.course = course;
    }
}
