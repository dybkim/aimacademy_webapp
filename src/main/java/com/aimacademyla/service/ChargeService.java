package com.aimacademyla.service;

import com.aimacademyla.model.Charge;
import com.aimacademyla.model.ChargeLine;
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
    List<Charge> getChargesByMember(int memberID);
    List<Charge> getChargesByMemberForCourse(Member member, Course course);
    List<Charge> getChargesByMemberForCourse(int memberID, int courseID);
    List<Charge> getChargesByMemberByDate(Member member, LocalDate localDate);
    List<Charge> getChargesByMemberByDate(int memberID, LocalDate localDate);
    Charge getChargeByMemberForCourseByDate(Member member, Course course, LocalDate date);
    Charge getChargeByMemberForCourseByDate(int memberID, int courseID, LocalDate date);
    List<Charge> getChargesByCourse(int courseID);
    List<Charge> getChargesByCourse(Course course);
    List<Charge> getChargesByDate(LocalDate localDate);
    void remove(List<Charge> chargeList);
    void addChargeLine(ChargeLine chargeLine);
    void updateChargeLine(ChargeLine chargeLine);
    void removeChargeLine(ChargeLine chargeLine);
}
