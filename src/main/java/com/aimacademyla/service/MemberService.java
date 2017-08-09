package com.aimacademyla.service;

import com.aimacademyla.model.Attendance;
import com.aimacademyla.model.Course;
import com.aimacademyla.model.Member;

import java.util.List;

/**
 * Created by davidkim on 1/26/17.
 */
public interface MemberService extends GenericService<Member, Integer>{

    List<Member> getMemberList();

    List<Member> getActiveMembers();

    List<Member> getMembersByCourse(Course course);

    List<Member> getActiveMembersByCourse(Course course);

    List<Member> getMemberListFromAttendanceList(List<Attendance> attendanceList);

    List<Member> getPresentMemberListFromAttendanceList(List<Attendance> attendanceList);

    void updateMemberList(List<Member> memberList);
}
