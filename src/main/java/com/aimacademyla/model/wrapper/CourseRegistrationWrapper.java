package com.aimacademyla.model.wrapper;

import com.aimacademyla.model.Course;
import com.aimacademyla.model.Member;

import java.io.Serializable;
import java.util.List;

/**
 * Created by davidkim on 7/5/17.
 */
public class CourseRegistrationWrapper implements Serializable{

    private static final long serialVersionUID = 689499054674508785L;

    private Course course;

    private List<Member> memberList;

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public List<Member> getMemberList() {
        return memberList;
    }

    public void setMemberList(List<Member> memberList) {
        this.memberList = memberList;
    }
}
