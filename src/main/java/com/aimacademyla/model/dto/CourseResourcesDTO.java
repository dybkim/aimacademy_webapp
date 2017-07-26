package com.aimacademyla.model.dto;

import com.aimacademyla.model.Member;
import com.aimacademyla.model.wrapper.CourseRegistrationWrapper;

import java.util.List;

/**
 * Created by davidkim on 7/14/17.
 */
public class CourseResourcesDTO {

    private List<Member> memberList;
    private List<Member> dropMemberList;
    private int memberID;

    public List<Member> getMemberList() {
        return memberList;
    }

    public void setMemberList(List<Member> memberList) {
        this.memberList = memberList;
    }

    public List<Member> getDropMemberList() {
        return dropMemberList;
    }

    public void setDropMemberList(List<Member> dropMemberList) {
        this.dropMemberList = dropMemberList;
    }

    public int getMemberID() {
        return memberID;
    }

    public void setMemberID(int memberID) {
        this.memberID = memberID;
    }
}
