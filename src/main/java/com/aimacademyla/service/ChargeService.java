package com.aimacademyla.service;

import com.aimacademyla.model.Charge;
import com.aimacademyla.model.Course;
import com.aimacademyla.model.Member;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * Created by davidkim on 4/10/17.
 */
public interface ChargeService extends GenericService<Charge, Integer>{
    List<Charge> getChargesByMember(Member member);
    List<Charge> getChargesByMemberForCourse(Member member, Course course);
    List<Charge> getChargesByMemberByDate(Member member, LocalDate localDate);
    Charge getChargeByMemberForCourseByDate(Member member, Course course, LocalDate date);
    List<Charge> getChargesByCourse(int courseID);
    List<Charge> getChargesByCourse(Course course);
}
