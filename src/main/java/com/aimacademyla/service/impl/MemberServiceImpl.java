package com.aimacademyla.service.impl;

import com.aimacademyla.dao.GenericDAO;
import com.aimacademyla.dao.MemberCourseRegistrationDAO;
import com.aimacademyla.dao.MemberDAO;
import com.aimacademyla.model.Attendance;
import com.aimacademyla.model.Course;
import com.aimacademyla.model.Member;
import com.aimacademyla.model.MemberCourseRegistration;
import com.aimacademyla.model.composite.MemberCourseRegistrationPK;
import com.aimacademyla.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by davidkim on 1/26/17.
 */

@Service
public class MemberServiceImpl extends GenericServiceImpl<Member, Integer> implements MemberService {

    private MemberDAO memberDAO;
    private MemberCourseRegistrationDAO memberCourseRegistrationDAO;

    @Autowired
    public MemberServiceImpl(@Qualifier("memberDAO") GenericDAO<Member, Integer> genericDAO,
                             MemberCourseRegistrationDAO memberCourseRegistrationDAO){
        super(genericDAO);
        this.memberDAO = (MemberDAO) genericDAO;
        this.memberCourseRegistrationDAO = memberCourseRegistrationDAO;
    }

    @Override
    public List<Member> getMemberList() {
       return memberDAO.getMemberList();
    }

    @Override
    public List<Member> getMembersByCourse(Course course){return memberDAO.getMembersByCourse(course);}

    @Override
    public List<Member> getActiveMembersByCourse(Course course){
        List<Member> memberList = new ArrayList<>();

        for(Member member : memberDAO.getMembersByCourse(course))
        {
            if(memberCourseRegistrationDAO.get(new MemberCourseRegistrationPK(member.getMemberID(), course.getCourseID())).getIsEnrolled())
                memberList.add(member);
        }

        return memberList;
    }

    @Override
    public List<Member> getMemberListFromAttendanceList(List<Attendance> attendanceList){

        List<Member> memberList = new ArrayList<>();

        for(Attendance attendance : attendanceList)
            memberList.add(memberDAO.get(attendance.getMemberID()));

        return memberList;
    }

    @Override
    public List<Member> getPresentMemberListFromAttendanceList(List<Attendance> attendanceList){
        List<Member> memberList = new ArrayList<>();

        for(Attendance attendance : attendanceList){
            if(attendance.getWasPresent())
                memberList.add(memberDAO.get(attendance.getMemberID()));
        }

        return memberList;
    }




    @Override
    public void updateMemberList(List<Member> memberList){
        memberDAO.updateMemberList(memberList);
    }
}
