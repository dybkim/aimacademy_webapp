package com.aimacademyla.api.slack.response.impl;

import com.aimacademyla.api.slack.response.AttachmentBuilder;
import com.aimacademyla.api.slack.service.ServiceFactory;
import com.aimacademyla.model.Course;
import com.aimacademyla.model.CourseSession;
import com.aimacademyla.model.Member;
import com.aimacademyla.service.CourseService;
import com.aimacademyla.service.CourseSessionService;
import com.aimacademyla.service.MemberService;
import com.ullink.slack.simpleslackapi.SlackAction;
import com.ullink.slack.simpleslackapi.SlackAttachment;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CourseSessionAttendanceAttachmentBuilder implements AttachmentBuilder {

    private final static String NO_STUDENTS_TEXT = "NO STUDENTS REGISTERED FOR COURSE";

    @Autowired
    private CourseService courseService;

    @Autowired
    private MemberService memberService;

    private SlackAttachment courseSessionAttendanceAttachment;

    private CourseSession courseSession;

    public CourseSessionAttendanceAttachmentBuilder(){
        courseSessionAttendanceAttachment = new SlackAttachment();
    }

    public CourseSessionAttendanceAttachmentBuilder(CourseSession courseSession){
        courseSessionAttendanceAttachment = new SlackAttachment();
        this.courseSession = courseSession;
    }

    @Override
    public SlackAttachment build() {
        Course course = courseService.get(courseSession.getCourseID());

        String title = course.getClassDuration().toString() + " hours";
        String text = "Pick 'No Session' if session was canceled";

        List<SlackAction> slackActionList = buildActionList(course);

        if(slackActionList.size() <= 1){
            courseSessionAttendanceAttachment.setTitle(title);
            courseSessionAttendanceAttachment.setText(NO_STUDENTS_TEXT);
            return courseSessionAttendanceAttachment;
        }

        courseSessionAttendanceAttachment.setTitle(title);
        courseSessionAttendanceAttachment.setText(text);

        for(SlackAction slackAction : slackActionList)
            courseSessionAttendanceAttachment.addAction(slackAction);

        return courseSessionAttendanceAttachment;
    }

    private List<SlackAction> buildActionList(Course course){
        List<SlackAction> slackActionList = new ArrayList<>();
        List<Member> memberList = memberService.getMembersByCourse(course);

        for(Member member : memberList){
            SlackAction slackAction = new SlackAction("member", member.getMemberFirstName() + " " + member.getMemberLastName(), "button", Integer.toString(member.getMemberID()));
            slackActionList.add(slackAction);
        }

        SlackAction slackAction = new SlackAction("null", "No Session", "button", "null");
        slackActionList.add(slackAction);

        return slackActionList;
    }

    public CourseSessionAttendanceAttachmentBuilder setCourseService(CourseService courseService) {
        this.courseService = courseService;
        return this;
    }

    public void setMemberService(MemberService memberService) {
        this.memberService = memberService;
    }

    public void setCourseSession(CourseSession courseSession) {
        this.courseSession = courseSession;
    }
}
