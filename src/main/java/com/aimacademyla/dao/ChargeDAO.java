package com.aimacademyla.dao;

import com.aimacademyla.model.Charge;
import com.aimacademyla.model.Course;
import com.aimacademyla.model.Member;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * Created by davidkim on 4/10/17.
 */
public interface ChargeDAO extends GenericDAO<Charge,Integer>{
    List<Charge> getChargesByMember(int memberID);
    List<Charge> getChargesByMember(Member member);
    List<Charge> getChargesByMemberByDate(Member member, LocalDate localDate);
    List<Charge> getChargesByMemberForCourse(Member member, Course course);
    Charge getChargeByMemberForCourseByDate(Member member, Course course, LocalDate date);
    List<Charge> getChargesByCourse(int courseID);
    List<Charge> getChargesByCourse(Course course);
}
