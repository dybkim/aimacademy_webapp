package com.aimacademyla.model.builder.dto;

import com.aimacademyla.dao.ChargeDAO;
import com.aimacademyla.dao.CourseSessionDAO;
import com.aimacademyla.dao.flow.impl.AttendanceDAOAccessFlow;
import com.aimacademyla.dao.flow.impl.CourseSessionDAOAccessFlow;
import com.aimacademyla.model.*;
import com.aimacademyla.model.builder.GenericBuilder;
import com.aimacademyla.model.dto.OutstandingChargesPaymentDTO;
import com.aimacademyla.model.dto.PeriodSummaryDTO;
import com.aimacademyla.model.temporal.CyclePeriod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class PeriodSummaryDTOBuilder extends GenericDTOBuilderImpl<PeriodSummaryDTO> implements GenericBuilder<PeriodSummaryDTO> {

    private CyclePeriod cyclePeriod;
    private List<Course> courseList;

    @SuppressWarnings("unchecked")
    public PeriodSummaryDTOBuilder(){
        courseList = getDAOFactory().getDAO(Course.class).getList();
    }

    @Override
    public PeriodSummaryDTO build() {
        PeriodSummaryDTO periodSummaryDTO = new PeriodSummaryDTO();
        OutstandingChargesPaymentDTO outstandingChargesPaymentDTO = new OutstandingChargesPaymentDTOBuilder().setCyclePeriod(cyclePeriod).build();

        List<Member> memberList = outstandingChargesPaymentDTO.getOutstandingBalanceMemberList();
        memberList.addAll(outstandingChargesPaymentDTO.getPaidBalanceMemberList());

        for(Member member : memberList) {
            List<Charge> chargeList = outstandingChargesPaymentDTO.getChargeListHashMap().get(member.getMemberID());
            chargeList = new ArrayList<>(((ChargeDAO)getDAOFactory().getDAO(Charge.class)).loadCollections(chargeList));

            PeriodSummaryDTO.MemberSummary memberSummary = new PeriodSummaryDTOBuilder.MemberSummaryBuilder()
                                                                .setMember(member)
                                                                .setChargeList(chargeList)
                                                                .setCourseList(courseList)
                                                                .setCyclePeriod(cyclePeriod)
                                                                .build();
            periodSummaryDTO.setCyclePeriod(cyclePeriod);
            periodSummaryDTO.addMemberSummary(memberSummary);
            periodSummaryDTO.setCourseSessionListHashMap(generateCourseSessionListHashMap());
        }

        return periodSummaryDTO;
    }

    @SuppressWarnings("unchecked")
    private HashMap<Integer, List<CourseSession>> generateCourseSessionListHashMap(){
        HashMap<Integer, List<CourseSession>> courseSessionListHashMap = new HashMap<>();
        for(Course course : courseList){
            List<CourseSession> courseSessionList = new CourseSessionDAOAccessFlow()
                                                        .addQueryParameter(course)
                                                        .addQueryParameter(cyclePeriod)
                                                        .getList();

            CourseSessionDAO courseSessionDAO = (CourseSessionDAO) getDAOFactory().getDAO(CourseSession.class);
            courseSessionList = new ArrayList<>(courseSessionDAO.loadCollections(courseSessionList));

            courseSessionListHashMap.put(course.getCourseID(), courseSessionList);
        }
        return courseSessionListHashMap;
    }

    public static class MemberSummaryBuilder{
        private Member member;
        private List<Charge> chargeList;
        private List<Course> courseList;
        private CyclePeriod cyclePeriod;

        public MemberSummaryBuilder setMember(Member member){
            this.member = member;
            return this;
        }

        public MemberSummaryBuilder setChargeList(List<Charge> chargeList){
            this.chargeList = chargeList;
            return this;
        }

        public MemberSummaryBuilder setCourseList(List<Course> courseList){
            this.courseList = courseList;
            return this;
        }

        public MemberSummaryBuilder setCyclePeriod(CyclePeriod cyclePeriod){
            this.cyclePeriod = cyclePeriod;
            return this;
        }

        public PeriodSummaryDTO.MemberSummary build(){
            PeriodSummaryDTO.MemberSummary memberSummary = new PeriodSummaryDTO.MemberSummary();
            memberSummary.setChargeList(chargeList);
            memberSummary.setMember(member);
            memberSummary.setCyclePeriod(cyclePeriod);
            memberSummary.setCourseAttendanceListHashMap(generateCourseAttendanceHashMap());
            return memberSummary;
        }

        @SuppressWarnings("unchecked")
        private HashMap<Integer, List<Attendance>> generateCourseAttendanceHashMap(){
            HashMap<Integer, List<Attendance>> courseAttendanceHashMap = new HashMap<>();

            for(Course course : courseList){
                List<Attendance> attendanceList = new AttendanceDAOAccessFlow()
                                                        .addQueryParameter(member)
                                                        .addQueryParameter(course)
                                                        .addQueryParameter(cyclePeriod)
                                                        .getList();

                Iterator it = attendanceList.iterator();
                while(it.hasNext()){
                    Attendance attendance = (Attendance) it.next();
                    if(!attendance.getWasPresent())
                        it.remove();
                }

                if(!attendanceList.isEmpty())
                    courseAttendanceHashMap.put(course.getCourseID(), attendanceList);
            }

            return courseAttendanceHashMap;
        }
    }

    public PeriodSummaryDTOBuilder setCyclePeriod(CyclePeriod cyclePeriod) {
        this.cyclePeriod = cyclePeriod;
        return this;
    }
}
