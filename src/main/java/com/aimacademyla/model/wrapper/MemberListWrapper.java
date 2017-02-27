package com.aimacademyla.model.wrapper;

import com.aimacademyla.model.Member;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by davidkim on 2/14/17.
 */
public class MemberListWrapper {

    private List<Member> memberList;

    public MemberListWrapper(){
        memberList = new ArrayList<>();
    }

    public MemberListWrapper(List<Member> memberList){
        this.memberList = memberList;
    }

    public List<Member> getMemberList(){
        return this.memberList;
    }

    public void setMemberList(List<Member> memberList){
        this.memberList = memberList;
    }
}
