package com.aimacademyla.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

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

    @Column(name="MemberID")
    private int memberID;

    @Column(name="SessionID")
    private Integer sessionID;

    @Column(name="attendanceDate")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern="MM/dd/yyyy")
    private Date attendanceDate;

    @Column(name="wasPresent")
    private boolean wasPresent;

    public int getAttendanceID() {
        return attendanceID;
    }

    public void setAttendanceID(int attendanceID) {
        this.attendanceID = attendanceID;
    }

    public int getMemberID() {
        return memberID;
    }

    public void setMemberID(int memberID) {
        this.memberID = memberID;
    }

    public int getSessionID() {
        return sessionID;
    }

    public void setSessionID(int sessionID) {
        this.sessionID = sessionID;
    }

    public Date getDateAttended() {
        return attendanceDate;
    }

    public void setDateAttended(Date attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public void setWasPresent(boolean wasPresent){this.wasPresent = wasPresent;}

    public boolean getWasPresent(){return this.wasPresent;}

}
