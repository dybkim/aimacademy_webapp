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
    public List<Member> getActiveMembers() {return memberDAO.getActiveMembers();}

    @Override
    public List<Member> getMembersByCourse(Course course){
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
            if(attendance.isWasPresent())
                memberList.add(memberDAO.get(attendance.getMemberID()));
        }

        return memberList;
    }

    /**
     * When a new member is added, add open study course registration
     * @param member
     */
    @Override
    public void add(Member member) {
        memberDAO.add(member);
        MemberCourseRegistration memberCourseRegistration = new MemberCourseRegistration();
        memberCourseRegistration.setMemberCourseRegistrationPK(new MemberCourseRegistrationPK(member.getMemberID(),Course.OPEN_STUDY_ID));
        memberCourseRegistration.setMemberID(member.getMemberID());
        memberCourseRegistration.setCourseID(Course.OPEN_STUDY_ID);
        memberCourseRegistrationDAO.add(memberCourseRegistration);
    }


    /**
     * If member's membership status is deactivated, remove open study course registration
     * If member's membership status is reactivated, add open study course registration
     * @param member
     */
    @Override
    public void update(Member member) {
        memberDAO.update(member);
        MemberCourseRegistrationPK memberCourseRegistrationPK = new MemberCourseRegistrationPK(member.getMemberID(), Course.OPEN_STUDY_ID);
        MemberCourseRegistration memberCourseRegistration = memberCourseRegistrationDAO.get(memberCourseRegistrationPK);

        if(!member.getMemberIsActive()){
            if(memberCourseRegistration != null)
                memberCourseRegistrationDAO.remove(memberCourseRegistration);
        }

        else{
            if(memberCourseRegistration == null){
                memberCourseRegistration = new MemberCourseRegistration();
                memberCourseRegistration.setMemberCourseRegistrationPK(memberCourseRegistrationPK);
                memberCourseRegistration.setMemberID(member.getMemberID());
                memberCourseRegistration.setCourseID(Course.OPEN_STUDY_ID);
                memberCourseRegistrationDAO.add(memberCourseRegistration);
            }
        }
    }

    /**
     * When a member is removed for whatever reason, remove the member's open study course registration
     * @param member
     */
    @Override
    public void remove(Member member){
        MemberCourseRegistrationPK memberCourseRegistrationPK = new MemberCourseRegistrationPK(member.getMemberID(), Course.OPEN_STUDY_ID);
        MemberCourseRegistration memberCourseRegistration = memberCourseRegistrationDAO.get(memberCourseRegistrationPK);

        memberCourseRegistrationDAO.remove(memberCourseRegistration);
        memberDAO.remove(member);
    }

    @Override
    public void updateMemberList(List<Member> memberList){
        memberDAO.updateMemberList(memberList);
    }
}
