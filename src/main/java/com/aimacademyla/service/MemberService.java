package com.aimacademyla.service;

import com.aimacademyla.model.Course;
import com.aimacademyla.model.Member;

import java.util.List;

/**
 * Created by davidkim on 1/26/17.
 */
public interface MemberService {

    List<Member> getMemberList();

    List<Member> getMembersByCourse(Course course);

    Member getMemberByID(int memberID);

    void addMember(Member member);

    void editMember(Member member);

    void deleteMember(Member member);

    void editMembers(List<Member> memberList);
}
