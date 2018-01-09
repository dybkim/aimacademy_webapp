package com.aimacademyla.dao.flow.impl;

import com.aimacademyla.dao.MemberCourseRegistrationDAO;
import com.aimacademyla.dao.factory.DAOFactory;
import com.aimacademyla.model.Course;
import com.aimacademyla.model.Member;
import com.aimacademyla.model.MemberCourseRegistration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemberCourseRegistrationDAOAccessFlow extends AbstractDAOAccessFlowImpl<MemberCourseRegistration>{

    private MemberCourseRegistrationDAO memberCourseRegistrationDAO;

    private static final Logger logger = LogManager.getLogger(MemberCourseRegistrationDAOAccessFlow.class.getName());

    public MemberCourseRegistrationDAOAccessFlow(DAOFactory daoFactory) {
        super(daoFactory);
        this.memberCourseRegistrationDAO = (MemberCourseRegistrationDAO) daoFactory.getDAO(MemberCourseRegistration.class);
        dispatch.put(Member.class, super::handleMember);
        dispatch.put(Course.class, super::handleCourse);
    }

    @Override
    public MemberCourseRegistration get() {
        logger.debug("get():criteria.size()=" + criteria.size());
        return memberCourseRegistrationDAO.get(criteria);
    }

    @Override
    public List<MemberCourseRegistration> getList() {
        logger.debug("getList():criteria.size()=" + criteria.size());
        return memberCourseRegistrationDAO.getList(criteria);
    }

}
