package com.aimacademyla.model.dto;

import com.aimacademyla.model.*;
import com.aimacademyla.model.temporal.CyclePeriod;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class PeriodSummaryDTO implements Serializable {
    private static final long serialVersionUID = 8402624704702514044L;
    private CyclePeriod cyclePeriod;

    private HashMap<Integer, MemberSummary> memberSummaryHashMap;
    private HashMap<Integer, List<CourseSession>> courseSessionListHashMap;

    public PeriodSummaryDTO(){
        memberSummaryHashMap = new HashMap<>();
        courseSessionListHashMap = new HashMap<>();
    }

    public CyclePeriod getCyclePeriod() {
        return cyclePeriod;
    }

    public void setCyclePeriod(CyclePeriod cyclePeriod) {
        this.cyclePeriod = cyclePeriod;
    }

    public HashMap<Integer, MemberSummary> getMemberSummaryHashMap() {
        return memberSummaryHashMap;
    }

    public void setMemberSummaryHashMap(HashMap<Integer, MemberSummary> memberSummaryHashMap) {
        this.memberSummaryHashMap = memberSummaryHashMap;
    }

    public void addMemberSummary(MemberSummary memberSummary){
        memberSummaryHashMap.put(memberSummary.getMember().getMemberID(), memberSummary);
    }

    public HashMap<Integer, List<CourseSession>> getCourseSessionListHashMap() {
        return courseSessionListHashMap;
    }

    public void setCourseSessionListHashMap(HashMap<Integer, List<CourseSession>> courseSessionListHashMap) {
        this.courseSessionListHashMap = courseSessionListHashMap;
    }

    /**
     * Inner static class MemberSummary is to be used to represent Member information for a certain CyclePeriod
     * PeriodSummaryDTO stores a hashmap of MemberSummary instance inside the memberSummaryHashMap
     */
    public static class MemberSummary{
        private Member member;
        private List<Charge> chargeList;
        //Key: CourseID; Value: List<Attendance>
        private HashMap<Integer, List<Attendance>> courseAttendanceListHashMap;
        private CyclePeriod cyclePeriod;

        public Member getMember() {
            return member;
        }

        public void setMember(Member member) {
            this.member = member;
        }

        public List<Charge> getChargeList() {
            return chargeList;
        }

        public void setChargeList(List<Charge> chargeList) {
            this.chargeList = chargeList;
        }

        public HashMap<Integer, List<Attendance>> getCourseAttendanceListHashMap() {
            return courseAttendanceListHashMap;
        }

        public void setCourseAttendanceListHashMap(HashMap<Integer, List<Attendance>> courseAttendanceListHashMap) {
            this.courseAttendanceListHashMap = courseAttendanceListHashMap;
        }

        public CyclePeriod getCyclePeriod() {
            return cyclePeriod;
        }

        public void setCyclePeriod(CyclePeriod cyclePeriod) {
            this.cyclePeriod = cyclePeriod;
        }
    }
}
