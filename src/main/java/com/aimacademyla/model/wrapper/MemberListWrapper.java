package com.aimacademyla.model.wrapper;

import com.aimacademyla.model.Member;
import org.springframework.context.annotation.Bean;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by davidkim on 2/14/17.
 */

public class MemberListWrapper implements Serializable{

    private static final long serialVersionUID = 5302331322725345182L;

    private List<Member> memberList;

    private List<Member> inactiveMemberList;

    private HashMap<Integer, Boolean> membershipHashMap;

    public List<Member> getMemberList(){
        return this.memberList;
    }

    public void setMemberList(List<Member> memberList){
        this.memberList = memberList;
    }

    public List<Member> getInactiveMemberList() {
        return inactiveMemberList;
    }

    public void setInactiveMemberList(List<Member> inactiveMemberList) {
        this.inactiveMemberList = inactiveMemberList;
    }

    public HashMap<Integer, Boolean> getMembershipHashMap() {
        return membershipHashMap;
    }

    public void setMembershipHashMap(HashMap<Integer, Boolean> membershipHashMap) {
        this.membershipHashMap = membershipHashMap;
    }
}
