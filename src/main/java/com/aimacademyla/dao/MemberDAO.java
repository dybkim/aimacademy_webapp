package com.aimacademyla.dao;

import com.aimacademyla.model.Course;
import com.aimacademyla.model.Member;

import java.util.List;

/**
 * Created by davidkim on 1/18/17.
 */
public interface MemberDAO extends GenericDAO<Member, Integer>{

    List<Member> getMemberList();

    List<Member> getActiveMembers();

    List<Member> getMembersByCourse(Course course);

    void updateMemberList(List<Member> memberList);
}
