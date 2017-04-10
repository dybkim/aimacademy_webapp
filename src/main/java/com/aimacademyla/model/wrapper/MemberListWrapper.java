package com.aimacademyla.model.wrapper;

import com.aimacademyla.model.Member;
import org.springframework.context.annotation.Bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by davidkim on 2/14/17.
 */

public class MemberListWrapper implements Serializable{

    private static final long serialVersionUID = 5302331322725345182L;

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
