package com.aimacademyla.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * Created by davidkim on 3/2/17.
 */

@Entity
public class Attendance implements Serializable{

    private static final long serialVersionUID = -6614132982434332722L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="AttendanceID")
    private int attendanceID;

    @ManyToOne
    @JoinColumn(name="MemberID")
    private Member member;

    @ManyToOne
    @JoinColumn(name="CourseSessionID")
    private CourseSession courseSession;

    //ZeroToOne mapping workaround
    @ManyToOne
    @JoinColumn(name="ChargeLineID")
    @NotFound(action = NotFoundAction.IGNORE)
    @JsonBackReference
    private ChargeLine chargeLine;

    @Column(name="AttendanceDate")
    @DateTimeFormat(pattern="MM/dd/yyyy")
    private LocalDate attendanceDate;

    @Column(name="WasPresent")
    private boolean wasPresent;

    @Override
    public boolean equals(Object object) {
        if(!(object instanceof Attendance))
            throw new IllegalArgumentException("Argument must be of type Attendance!");

        Attendance attendance = (Attendance) object;
        return attendance.getAttendanceID() == attendanceID;
    }

    public int getAttendanceID() {
        return attendanceID;
    }

    public void setAttendanceID(int attendanceID) {
        this.attendanceID = attendanceID;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public CourseSession getCourseSession() {
        return courseSession;
    }

    public void setCourseSession(CourseSession courseSession) {
        this.courseSession = courseSession;
    }

    public boolean isWasPresent() {
        return wasPresent;
    }

    public LocalDate getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(LocalDate attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public boolean getWasPresent() {
        return wasPresent;
    }

    public void setWasPresent(boolean wasPresent){this.wasPresent = wasPresent;}

    public ChargeLine getChargeLine() {
        return chargeLine;
    }

    public void setChargeLine(ChargeLine chargeLine) {
        this.chargeLine = chargeLine;
    }
}
