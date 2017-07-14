package com.aimacademyla.service;

import com.aimacademyla.model.Charge;
import com.aimacademyla.model.Course;
import com.aimacademyla.model.Member;

import java.util.List;

/**
 * Created by davidkim on 4/10/17.
 */
public interface ChargeService extends GenericService<Charge, Integer>{
    List<Charge> getChargesByMember(int memberID);
    List<Charge> getChargesByMember(Member member);
    List<Charge> getChargesByCourse(int courseID);
    List<Charge> getChargesByCourse(Course course);
}
