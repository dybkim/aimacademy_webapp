package com.aimacademyla.model.wrapper;

import com.aimacademyla.model.Member;

import java.io.Serializable;

/**
 * Created by davidkim on 7/17/17.
 */
public class CourseRegistrationWrapperListItem implements Serializable {

    private static final long serialVersionUID = 4767686080695400542L;

    private Member member;

    private boolean isDropped;

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public boolean getIsDropped() {
        return isDropped;
    }

    public void setIsDropped(boolean dropped) {
        isDropped = dropped;
    }
}
