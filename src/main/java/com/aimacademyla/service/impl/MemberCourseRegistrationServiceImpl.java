package com.aimacademyla.service.impl;

import com.aimacademyla.dao.CourseDAO;
import com.aimacademyla.dao.GenericDAO;
import com.aimacademyla.dao.MemberCourseRegistrationDAO;
import com.aimacademyla.dao.flow.impl.MemberCourseRegistrationDAOAccessFlow;
import com.aimacademyla.model.Course;
import com.aimacademyla.model.Member;
import com.aimacademyla.model.MemberCourseRegistration;
import com.aimacademyla.service.MemberCourseRegistrationService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Created by davidkim on 2/13/17.
 */

@Service
public class MemberCourseRegistrationServiceImpl extends GenericServiceImpl<MemberCourseRegistration, Integer> implements MemberCourseRegistrationService {

    private MemberCourseRegistrationDAO memberCourseRegistrationDAO;
    private CourseDAO courseDAO;

    public MemberCourseRegistrationServiceImpl(@Qualifier("memberCourseRegistrationDAO") GenericDAO<MemberCourseRegistration, Integer> genericDAO,
                                               CourseDAO courseDAO) {
        super(genericDAO);
        this.memberCourseRegistrationDAO = (MemberCourseRegistrationDAO) genericDAO;
        this.courseDAO = courseDAO;
    }


    @Override
    public MemberCourseRegistration getMemberCourseRegistration(Member member, Course course){
        return (MemberCourseRegistration) new MemberCourseRegistrationDAOAccessFlow()
                                            .addQueryParameter(member)
                                            .addQueryParameter(course)
                                            .get();

    }
    @Override
    public void addMemberCourseRegistration(MemberCourseRegistration memberCourseRegistration) {
        Course course = memberCourseRegistration.getCourse();
        course.setNumEnrolled(course.getNumEnrolled() + 1);
        memberCourseRegistrationDAO.add(memberCourseRegistration);
        courseDAO.update(course);
    }

    @Override
    public void updateMemberCourseRegistration(MemberCourseRegistration memberCourseRegistration) {
        Member member = memberCourseRegistration.getMember();
        Course course = memberCourseRegistration.getCourse();
        int numEnrolled = course.getNumEnrolled();

        MemberCourseRegistration persistedMemberCourseRegistration = (MemberCourseRegistration) new MemberCourseRegistrationDAOAccessFlow()
                                                                                                    .addQueryParameter(member)
                                                                                                    .addQueryParameter(course)
                                                                                                    .get();

        if (persistedMemberCourseRegistration != null) {
            if (memberCourseRegistration.getIsEnrolled() && !persistedMemberCourseRegistration.getIsEnrolled())
                course.setNumEnrolled(numEnrolled + 1);

            else if (!memberCourseRegistration.getIsEnrolled() && persistedMemberCourseRegistration.getIsEnrolled())
                course.setNumEnrolled(numEnrolled - 1);
            courseDAO.update(course);
            memberCourseRegistrationDAO.update(memberCourseRegistration);
        }

        if (memberCourseRegistration.getIsEnrolled())
            course.setNumEnrolled(numEnrolled + 1);

        courseDAO.update(course);

        memberCourseRegistrationDAO.add(memberCourseRegistration);
    }

    @Override
    public void removeMemberCourseRegistration(MemberCourseRegistration memberCourseRegistration) {
        Course course = memberCourseRegistration.getCourse();
        course.setNumEnrolled(course.getNumEnrolled() - 1);
        courseDAO.update(course);
        memberCourseRegistrationDAO.remove(memberCourseRegistration);
    }
}
