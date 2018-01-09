package com.aimacademyla.model.dto;

import com.aimacademyla.model.Member;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

/**
 * Created by davidkim on 2/14/17.
 */

public class MemberListDTO implements Serializable{

    private static final long serialVersionUID = 5302331322725345182L;

    private List<Member> activeMemberList;

    private List<Member> inactiveMemberList;

    private HashMap<Integer, Boolean> isActiveMemberHashMap;

    @DateTimeFormat(pattern="MM/dd/yyyy")
    private LocalDate cycleStartDate;

    public List<Member> getActiveMemberList(){
        return this.activeMemberList;
    }

    public void setActiveMemberList(List<Member> activeMemberList){
        this.activeMemberList = activeMemberList;
    }

    public List<Member> getInactiveMemberList() {
        return inactiveMemberList;
    }

    public void setInactiveMemberList(List<Member> inactiveMemberList) {
        this.inactiveMemberList = inactiveMemberList;
    }

    public HashMap<Integer, Boolean> getIsActiveMemberHashMap() {
        return isActiveMemberHashMap;
    }

    public void setIsActiveMemberHashMap(HashMap<Integer, Boolean> isActiveMemberHashMap) {
        this.isActiveMemberHashMap = isActiveMemberHashMap;
    }

    public LocalDate getCycleStartDate() {
        return cycleStartDate;
    }

    public void setCycleStartDate(LocalDate cycleStartDate) {
        this.cycleStartDate = cycleStartDate;
    }
}
