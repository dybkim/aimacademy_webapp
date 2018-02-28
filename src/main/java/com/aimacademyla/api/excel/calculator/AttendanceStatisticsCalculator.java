package com.aimacademyla.api.excel.calculator;

import com.aimacademyla.model.Attendance;
import com.aimacademyla.model.CourseSession;
import com.aimacademyla.model.dto.PeriodSummaryDTO;

import java.math.BigDecimal;
import java.util.*;

public class AttendanceStatisticsCalculator {

    public static HashMap<String, CourseStatistics> generateStatistics(PeriodSummaryDTO periodSummaryDTO){
        HashMap<String, CourseStatistics> statisticsHashMap = new HashMap<>();

        statisticsHashMap.put("Open Study", CourseStatisticsFactory.getStatistics(periodSummaryDTO, "Membership"));
        statisticsHashMap.put("Office Hour", CourseStatisticsFactory.getStatistics(periodSummaryDTO, "Office Hour"));
        statisticsHashMap.put("Supplement", CourseStatisticsFactory.getStatistics(periodSummaryDTO, "Supplement"));

        return statisticsHashMap;
    }

    private static class CourseStatisticsFactory{
        private static CourseStatistics getStatistics(PeriodSummaryDTO periodSummaryDTO, String courseType){
            List<CourseSession> courseSessionList = new ArrayList<>();
            List<Attendance> attendanceList = new ArrayList<>();

            for(List<CourseSession> courseSessions: periodSummaryDTO.getCourseSessionListHashMap().values()) {
                if (!courseSessions.isEmpty()) {
                    for(CourseSession courseSession : courseSessions){
                        if(courseSession.getCourse().getCourseType().equals(courseType))
                            courseSessionList.add(courseSession);
                    }
                }
            }

            for(CourseSession courseSession : courseSessionList) {
                System.out.println("CourseSession is: " + courseSession.getCourse().getCourseType() + " on date: " + courseSession.getCourseSessionDate());
                attendanceList.addAll(courseSession.getAttendanceMap().values());
            }

            HashMap<Integer, Integer> memberAttendanceCountHashMap = generateMemberAttendanceCountHashMap(attendanceList);

            List<Integer> attendanceCountList = new ArrayList<>(memberAttendanceCountHashMap.values());

            CourseStatistics courseStatistics = new CourseStatistics();
            courseStatistics.setMedian(getMedian(attendanceCountList));
            courseStatistics.setNinetyFifthPercentile(get95thPercentile(attendanceCountList));
            courseStatistics.setAverage(getAverage(attendanceCountList));
            courseStatistics.setMax(getMax(attendanceCountList));
            courseStatistics.setMin(getMin(attendanceCountList));
            courseStatistics.setTotal(BigDecimal.valueOf(courseSessionList.size()));

            return courseStatistics;
        }

    }

    public static class CourseStatistics {
        private BigDecimal median;
        private BigDecimal average;
        private BigDecimal ninetyFifthPercentile;
        private BigDecimal max;
        private BigDecimal min;
        private BigDecimal total;

        public BigDecimal getMedian() {
            return median;
        }

        public void setMedian(BigDecimal median) {
            this.median = median;
        }

        public BigDecimal getAverage() {
            return average;
        }

        public void setAverage(BigDecimal average) {
            this.average = average;
        }

        public BigDecimal getNinetyFifthPercentile() {
            return ninetyFifthPercentile;
        }

        public void setNinetyFifthPercentile(BigDecimal ninetyFifthPercentile) {
            this.ninetyFifthPercentile = ninetyFifthPercentile;
        }

        public BigDecimal getMax() {
            return max;
        }

        public void setMax(BigDecimal max) {
            this.max = max;
        }

        public BigDecimal getMin() {
            return min;
        }

        public void setMin(BigDecimal min) {
            this.min = min;
        }

        public BigDecimal getTotal() {
            return total;
        }

        public void setTotal(BigDecimal total) {
            this.total = total;
        }
    }

    private static BigDecimal getMedian(List<Integer> attendanceCountList){
        if(attendanceCountList.size() == 0)
            return BigDecimal.ZERO;

        Collections.sort(attendanceCountList);
        int size = attendanceCountList.size();
        return new BigDecimal(((double) (attendanceCountList.get(size/2) + attendanceCountList.get((size - 1)/2)))/2);
    }

    private static BigDecimal get95thPercentile(List<Integer> attendanceCountList){
        if(attendanceCountList.size() == 0)
            return BigDecimal.ZERO;

        attendanceCountList.sort(Collections.reverseOrder());
        int size = attendanceCountList.size();
        int index;
        BigDecimal index95th = BigDecimal.valueOf(size).multiply(BigDecimal.valueOf(.95));
        index = index95th.setScale(2, BigDecimal.ROUND_DOWN).intValue();

        if(index95th.remainder(BigDecimal.ONE).compareTo(BigDecimal.valueOf(.50)) < 0)
            index--;

        return new BigDecimal(attendanceCountList.get(index));
    }

    private static BigDecimal getAverage(List<Integer> attendanceCountList){
        if(attendanceCountList.size() == 0)
            return BigDecimal.ZERO;

        BigDecimal sum = BigDecimal.ZERO;
        for(int attendanceCount : attendanceCountList)
            sum.add(BigDecimal.valueOf(attendanceCount));

        return sum.divide(BigDecimal.valueOf(attendanceCountList.size()), 2);
    }

    private static BigDecimal getMin(List<Integer> attendanceCountList){
        int min = -1;
        for(int attendanceCount : attendanceCountList)
            if(attendanceCount < min || min == -1)
                min = attendanceCount;

        if(min == -1)
            min = 0;

        return BigDecimal.valueOf(min);
    }

    private static BigDecimal getMax(List<Integer> attendanceCountList){
        int max = 0;
        for(int attendanceCount : attendanceCountList)
            if(attendanceCount > max)
                max = attendanceCount;

        return BigDecimal.valueOf(max);
    }

    private static HashMap<Integer, Integer> generateMemberAttendanceCountHashMap(List<Attendance> attendanceList){
        HashMap<Integer, Integer> memberAttendanceCountHashMap = new HashMap<>();
        for(Attendance attendance : attendanceList){

            int memberID = attendance.getMember().getMemberID();
            Integer attendanceCount = memberAttendanceCountHashMap.get(memberID);

            if(attendanceCount == null) {
                if (!attendance.getWasPresent()) {
                    memberAttendanceCountHashMap.put(memberID, 0);
                } else
                    memberAttendanceCountHashMap.put(memberID, 1);
            }

            else
                if (attendance.getWasPresent())
                    memberAttendanceCountHashMap.put(memberID, attendanceCount + 1);
        }

        for(Integer attendanceCount : memberAttendanceCountHashMap.values()){
            System.out.println("Attendance Count is: " + attendanceCount);
        }

        return memberAttendanceCountHashMap;
    }
}
