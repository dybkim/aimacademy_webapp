package com.aimacademyla.dao.impl;

import com.aimacademyla.dao.MemberCourseRegistrationDAO;
import com.aimacademyla.model.Course;
import com.aimacademyla.model.MemberCourseRegistration;
import com.aimacademyla.model.composite.MemberCourseRegistrationPK;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by davidkim on 2/9/17.
 */

@Repository("memberCourseRegistrationDAO")
@Transactional
public class MemberCourseRegistrationDAOImpl extends GenericDAOImpl<MemberCourseRegistration, MemberCourseRegistrationPK> implements MemberCourseRegistrationDAO {

    public MemberCourseRegistrationDAOImpl(){
        super(MemberCourseRegistration.class);
    }

    @Override
    public List<MemberCourseRegistration> getMemberCourseRegistrationListForCourse(Course course){
        Session session = currentSession();
        Query query = session.createQuery("FROM Member_Course_Registration WHERE CourseID = :courseID");
        query.setParameter("courseID", course.getCourseID());
        List<MemberCourseRegistration> memberCourseRegistrationList = query.getResultList();

        System.out.println("number of students enrolled: " + memberCourseRegistrationList.size());

        session.flush();

        return memberCourseRegistrationList;
    }

    @Override
    public MemberCourseRegistration getMemberCourseRegistrationForCourse(int memberID, int courseID){
        Session session = currentSession();
        Query query = session.createQuery("FROM Member_Course_Registration WHERE MemberID = :memberID AND CourseID = :courseID");
        query.setParameter("memberID", memberID).setParameter("courseID", courseID);

        MemberCourseRegistration memberCourseRegistration = (MemberCourseRegistration)query.uniqueResult();

        return memberCourseRegistration;
    }
}
