package com.aimacademyla.dao.impl;

import com.aimacademyla.dao.MemberCourseRegistrationDAO;
import com.aimacademyla.model.Charge;
import com.aimacademyla.model.MemberCourseRegistration;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by davidkim on 2/9/17.
 */

@Repository("memberCourseRegistrationDAO")
@Transactional
public class MemberCourseRegistrationDAOImpl extends GenericDAOImpl<MemberCourseRegistration, Integer> implements MemberCourseRegistrationDAO {

    public MemberCourseRegistrationDAOImpl(){
        super(MemberCourseRegistration.class);
    }

    @Override
    public void removeList(List<MemberCourseRegistration> memberCourseRegistrationList){
        Session session = currentSession();
        List<Integer> memberCourseRegistrationIDList = new ArrayList<>();
        for(MemberCourseRegistration memberCourseRegistration : memberCourseRegistrationList)
            memberCourseRegistrationIDList.add(memberCourseRegistration.getMemberCourseRegistrationID());
        Query query = session.createQuery("DELETE FROM Member_Course_Registration M WHERE M.memberCourseRegistrationID in :memberCourseRegistrationIDList");
        query.setParameterList("memberCourseRegistrationIDList", memberCourseRegistrationIDList);
        query.executeUpdate();
    }
}
