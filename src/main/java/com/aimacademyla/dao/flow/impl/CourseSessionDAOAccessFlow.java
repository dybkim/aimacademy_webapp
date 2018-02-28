package com.aimacademyla.dao.flow.impl;

import com.aimacademyla.dao.CourseSessionDAO;
import com.aimacademyla.model.Course;
import com.aimacademyla.model.CourseSession;
import com.aimacademyla.model.temporal.CyclePeriod;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.LocalDateType;

import java.time.LocalDate;
import java.util.List;

public class CourseSessionDAOAccessFlow extends AbstractDAOAccessFlowImpl<CourseSession>{

    private CourseSessionDAO courseSessionDAO;

    public CourseSessionDAOAccessFlow(){
        this.courseSessionDAO = (CourseSessionDAO) getDAOFactory().getDAO(CourseSession.class);
        dispatch.put(Course.class, super::handleCourse);
        dispatch.put(CyclePeriod.class, this::handleCyclePeriod);
    }

    @Override
    public CourseSession get(){
        return courseSessionDAO.get(criteria);
    }

    @Override
    public List<CourseSession> getList(){
        return courseSessionDAO.getList(criteria);
    }

    void handleCyclePeriod(Object object, List<Criterion> criteria){
        CyclePeriod cyclePeriod = (CyclePeriod) object;
        LocalDate periodStartDate = cyclePeriod.getCycleStartDate();
        LocalDate periodEndDate = cyclePeriod.getCycleEndDate();
        Criterion startCriterion = Restrictions.sqlRestriction("{alias}.courseSessionDate >= ?", periodStartDate, LocalDateType.INSTANCE);
        Criterion endCriterion = Restrictions.sqlRestriction("{alias}.courseSessionDate <= ?", periodEndDate, LocalDateType.INSTANCE);
        criteria.add(Restrictions.and(startCriterion, endCriterion));
    }
}
