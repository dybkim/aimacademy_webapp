package com.aimacademyla.model;

import com.aimacademyla.model.enums.BillableUnitType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

/**
 * Course Entity represents one academic program held during a specific time period
 *
 * Created by davidkim on 2/8/17.
 */

@Entity
@NamedEntityGraph(name="graph.Course.courseSessionSet",
                attributeNodes = {@NamedAttributeNode(value="courseSessionSet", subgraph = "courseSessionSet"),
                                 @NamedAttributeNode(value="memberCourseRegistrationSet")},
                subgraphs = @NamedSubgraph(name="courseSessionSet", attributeNodes = @NamedAttributeNode("attendanceMap")))
public class Course implements Serializable{

    private static final long serialVersionUID = 3942567537260692323L;

    public static final int OPEN_STUDY_ID = 2;
    public static final int OTHER_ID = 1;

    private static final Logger logger = LogManager.getLogger(Course.class.getName());

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="CourseID")
    private int courseID;

    @Column(name="CourseName")
    @NotEmpty(message = "Must provide course title")
    @Length(max=30)
    private String courseName;

    @Column(name="CourseStartDate")
    @DateTimeFormat(pattern="MM/dd/yyyy")
    private LocalDate courseStartDate;

    @Column(name="CourseEndDate")
    @DateTimeFormat(pattern="MM/dd/yyyy")
    private LocalDate courseEndDate;

    @Column(name="IsActive")
    private boolean isActive;

    @Column(name="CourseType")
    @NotEmpty(message = "Must provide course type")
    @Length(max=15)
    private String courseType;

    @Column(name="NumEnrolled")
    private int numEnrolled;

    @Column(name="TotalNumSessions")
    private int totalNumSessions;

    @ManyToOne
    @JoinColumn(name="SeasonID")
    private Season season;

    @Column(name="MemberPricePerBillableUnit")
    @NumberFormat(style= NumberFormat.Style.CURRENCY)
    @NotNull
    private BigDecimal memberPricePerBillableUnit;

    @Column(name="NonMemberPricePerBillableUnit")
    @NumberFormat(style= NumberFormat.Style.CURRENCY)
    private BigDecimal nonMemberPricePerBillableUnit;

    @Column(name="ClassDuration")
    private BigDecimal classDuration;

    @Column(name="BillableUnitDuration")
    private BigDecimal billableUnitDuration;

    @Column(name="BillableUnitType")
    @Length(max=10)
    private String billableUnitType;

    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "course",
            orphanRemoval = true)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Set<MemberCourseRegistration> memberCourseRegistrationSet;

    /*
     * If Cascade for courseSessionSet has CascadeType.MERGE, then there are merge conflicts when courseSessions are updated
     * If Cascade for courseSessionSet doesn't have CascadeType.MERGE, then existing courseSessions do not get updated when changes are made to them
     */
    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "course",
            orphanRemoval = true)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Set<CourseSession> courseSessionSet;

    public void addMemberCourseRegistration(MemberCourseRegistration memberCourseRegistration){
        if(memberCourseRegistrationSet == null)
            return;

        memberCourseRegistrationSet.add(memberCourseRegistration);
        memberCourseRegistration.setCourse(this);
    }

    public void removeMemberCourseRegistration(MemberCourseRegistration memberCourseRegistration){
        if(memberCourseRegistrationSet == null)
           return;

        Iterator it = memberCourseRegistrationSet.iterator();
        while(it.hasNext()){
            MemberCourseRegistration iteratedMemberCourseRegistration = (MemberCourseRegistration) it.next();
            if(iteratedMemberCourseRegistration.getMemberCourseRegistrationID() == memberCourseRegistration.getMemberCourseRegistrationID()){
                it.remove();
                return;
            }
        }
    }

    public void addCourseSession(CourseSession courseSession){
        if(courseSessionSet == null)
            return;

        logger.debug("Adding CourseSession, current numSessions: " + totalNumSessions);
        courseSessionSet.add(courseSession);
        courseSession.setCourse(this);
        totalNumSessions++;
        logger.debug("Added CourseSession, current numSessions: " + totalNumSessions);
    }

    private CourseSession getCourseSession(int courseSessionID){
        for(CourseSession courseSession : courseSessionSet)
            if(courseSession.getCourseSessionID() == courseSessionID)
                return courseSession;

        return null;
    }

    public void updateCourseSession(CourseSession courseSession){
        if(courseSessionSet == null)
            return;

        CourseSession oldCourseSession = getCourseSession(courseSession.getCourseSessionID());
        logger.debug("Updating CourseSession, current numSessions: " + totalNumSessions);

        if(oldCourseSession != null)
            removeCourseSession(oldCourseSession);

        addCourseSession(courseSession);
        courseSession.setCourse(this);
        logger.debug("Updated CourseSession, current numSessions: " + totalNumSessions);
    }

    public void removeCourseSession(CourseSession courseSession){
        if (courseSessionSet == null)
            return;

       logger.debug("Removing CourseSession, current numSessions: " + totalNumSessions);
       courseSessionSet.remove(courseSession);

        if(totalNumSessions > 0)
            totalNumSessions--;

        logger.debug("Removed CourseSession, current numSessions: " + totalNumSessions);
    }

    @JsonIgnore
    public List<Member> getActiveMembers(){
        List<Member> activeMemberList = new ArrayList<>();

        if(memberCourseRegistrationSet == null)
            return activeMemberList;

        for(MemberCourseRegistration memberCourseRegistration : memberCourseRegistrationSet){
            Member member = memberCourseRegistration.getMember();
            if(memberCourseRegistration.getIsEnrolled())
                activeMemberList.add(member);
        }
        return activeMemberList;
    }

    @JsonIgnore
    public List<Member> getInactiveMembers(){
        List<Member> inactiveMemberList = new ArrayList<>();

        if(memberCourseRegistrationSet == null)
            return inactiveMemberList;

        for(MemberCourseRegistration memberCourseRegistration : memberCourseRegistrationSet){
            Member member = memberCourseRegistration.getMember();
            if(!memberCourseRegistration.getIsEnrolled())
                inactiveMemberList.add(member);
        }
        return inactiveMemberList;
    }

    /*
     * Need to make sure that Attendance Collection for CourseSessions in courseSessionSet have been initialized
     */
    @JsonIgnore
    public int getNumAttendanceForMember(Member member){
        int numAttendance = 0;
        for(CourseSession courseSession : courseSessionSet){
            if(courseSession.memberWasPresent(member))
                numAttendance++;
        }
        return numAttendance;
    }

    @JsonIgnore
    public Map<Integer, Integer> getMemberAttendanceCountHashMap(){
        HashMap<Integer, Integer> memberAttendanceCountHashMap = new HashMap<>();

        for(MemberCourseRegistration memberCourseRegistration : memberCourseRegistrationSet){
            Member member = memberCourseRegistration.getMember();
            memberAttendanceCountHashMap.put(member.getMemberID(), getNumAttendanceForMember(member));
        }

        return memberAttendanceCountHashMap;
    }

    @JsonIgnore
    public BigDecimal getCourseSessionChargeAmount(boolean isActiveMember){
        logger.debug("Current course charge rates for: " + courseName + " is " + memberPricePerBillableUnit + "(Member), " + nonMemberPricePerBillableUnit + " (NonMember)" + " for " + billableUnitDuration + " units per session");
        if(isActiveMember)
            return memberPricePerBillableUnit.multiply(billableUnitDuration);

        return nonMemberPricePerBillableUnit.multiply(billableUnitDuration);
    }

    @Override
    public boolean equals(Object object){
        if(!(object instanceof Course))
            throw new IllegalArgumentException("Argument must be of type Course!");

        Course course = (Course) object;
        return course.getCourseID() == courseID;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public LocalDate getCourseStartDate() {
        return courseStartDate;
    }

    public void setCourseStartDate(LocalDate courseStartDate) {
        this.courseStartDate = courseStartDate;
    }

    public LocalDate getCourseEndDate() {
        return courseEndDate;
    }

    public void setCourseEndDate(LocalDate courseEndDate) {
        this.courseEndDate = courseEndDate;
    }

    public String getCourseType() {
        return courseType;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }

    public int getNumEnrolled() {
        return numEnrolled;
    }

    public void setNumEnrolled(int numEnrolled) {
        this.numEnrolled = numEnrolled;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean active) {
        isActive = active;
    }

    public int getTotalNumSessions() {
        return totalNumSessions;
    }

    public void setTotalNumSessions(int totalNumSessions) {
        this.totalNumSessions = totalNumSessions;
    }

    public BigDecimal getMemberPricePerBillableUnit() {
        return memberPricePerBillableUnit;
    }

    public void setMemberPricePerBillableUnit(BigDecimal memberPricePerBillableUnit) {
        this.memberPricePerBillableUnit = memberPricePerBillableUnit;
    }

    public BigDecimal getNonMemberPricePerBillableUnit() {
        return nonMemberPricePerBillableUnit;
    }

    public void setNonMemberPricePerBillableUnit(BigDecimal nonMemberPricePerBillableUnit) {
        this.nonMemberPricePerBillableUnit = nonMemberPricePerBillableUnit;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Season getSeason() {
        return season;
    }

    public void setSeason(Season season) {
        this.season = season;
    }

    public BigDecimal getBillableUnitDuration() {
        return billableUnitDuration;
    }

    public void setBillableUnitDuration(){
        switch(BillableUnitType.parseString(billableUnitType)){
            case PER_HOUR:
                if(classDuration != null){
                    billableUnitDuration = classDuration;
                }
                logger.debug("Set " + courseName + " BillableUnitDuration to: " + billableUnitDuration);
                return;
            case PER_SESSION:
                billableUnitDuration = BigDecimal.ONE;
                logger.debug("Set " + courseName + " BillableUnitDuration to: " + billableUnitDuration);
                return;
            default:
                billableUnitDuration = BigDecimal.ZERO;
                logger.debug("Set " + courseName + " BillableUnitDuration to: " + billableUnitDuration);
        }
    }

    public void setBillableUnitDuration(BigDecimal billableUnitDuration) {
        this.billableUnitDuration = billableUnitDuration;
    }

    public BigDecimal getClassDuration() {
        return classDuration;
    }

    public void setClassDuration(BigDecimal classDuration) {
        this.classDuration = classDuration;
    }

    public String getBillableUnitType() {
        return billableUnitType;
    }

    public void setBillableUnitType(String billableUnitType) {
        this.billableUnitType = billableUnitType;
    }

    public Set<MemberCourseRegistration> getMemberCourseRegistrationSet() {
        return memberCourseRegistrationSet;
    }

    public void setMemberCourseRegistrationSet(Set<MemberCourseRegistration> memberCourseRegistrationSet) {
        this.memberCourseRegistrationSet = memberCourseRegistrationSet;
    }

    public Set<CourseSession> getCourseSessionSet(){
        return courseSessionSet;
    }

    public void setCourseSessionSet(Set<CourseSession> courseSessionSet) {
        this.courseSessionSet = courseSessionSet;
    }
}
