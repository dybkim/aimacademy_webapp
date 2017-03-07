package com.aimacademyla.service.impl;

import com.aimacademyla.dao.MemberDAO;
import com.aimacademyla.model.Attendance;
import com.aimacademyla.model.Course;
import com.aimacademyla.model.Member;
import com.aimacademyla.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by davidkim on 1/26/17.
 */

@Service
public class MemberServiceImpl implements MemberService {

    private MemberDAO memberDAO;

    public MemberServiceImpl(MemberDAO memberDAO){
        this.memberDAO = memberDAO;
    }

    @Override
    public List<Member> getMemberList() {
       return memberDAO.getMemberList();
    }

    @Override
    public Member getMemberByID(int memberID) {
        return memberDAO.getMemberByID(memberID);
    }

    @Override
    public List<Member> getMembersByCourse(Course course){
        return memberDAO.getMembersByCourse(course);
    }

    @Override
    public List<Member> getMemberListFromAttendanceList(List<Attendance> attendanceList){

        List<Member> memberList = new ArrayList<>();

        for(Attendance attendance : attendanceList)
            memberList.add(memberDAO.getMemberByID(attendance.getMemberID()));

        return memberList;
    }

    @Override
    public List<Member> getPresentMemberListFromAttendanceList(List<Attendance> attendanceList){
        List<Member> memberList = new ArrayList<>();

        for(Attendance attendance : attendanceList){
            if(attendance.getWasPresent())
                memberList.add(memberDAO.getMemberByID(attendance.getMemberID()));
        }

        return memberList;
    }

    @Override
    public void addMember(Member member) {
        memberDAO.addMember(member);
    }

    @Override
    public void editMember(Member member) {
        memberDAO.editMember(member);
    }

    @Override
    public void deleteMember(Member member) {
        memberDAO.deleteMember(member);
    }

    @Override
    public void editMembers(List<Member> memberList){ memberDAO.editMembers(memberList);}
}
