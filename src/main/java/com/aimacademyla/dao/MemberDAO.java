package com.aimacademyla.dao;

import com.aimacademyla.model.Course;
import com.aimacademyla.model.Member;

import java.util.List;

/**
 * Created by davidkim on 1/18/17.
 */
public interface MemberDAO {

    List<Member> getMemberList();

    Member getMemberByID(int id);

    List<Member> getMembersByCourse(Course course);

    void addMember(Member member);

    void editMember(Member member);

    void deleteMember(Member member);

    void editMembers(List<Member> memberList);
}
