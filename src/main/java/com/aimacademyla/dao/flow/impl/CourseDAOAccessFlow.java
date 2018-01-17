package com.aimacademyla.dao.flow.impl;

import com.aimacademyla.dao.CourseDAO;
import com.aimacademyla.model.Course;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.LocalDateType;

import java.time.LocalDate;
import java.util.List;

public class CourseDAOAccessFlow extends AbstractDAOAccessFlowImpl<Course>{

    private CourseDAO courseDAO;

    public CourseDAOAccessFlow() {
        this.courseDAO = (CourseDAO) getDAOFactory().getDAO(Course.class);
        dispatch.put(LocalDate.class, this::handleLocalDate);
    }

    /*
     * Fetch Courses that are active in a certain Date range
     */
    void handleLocalDate(Object object, List<Criterion> criteria){
        LocalDate localDate = (LocalDate) object;
        Criterion startDateCriterion = Restrictions.sqlRestriction("CourseStartDate <= ?", localDate, LocalDateType.INSTANCE);
        Criterion endDateCriterion = Restrictions.sqlRestriction("CourseEndDate >= ?", localDate, LocalDateType.INSTANCE);
        Criterion dateCriterion = Restrictions.and(startDateCriterion, endDateCriterion);

        Criterion isActiveCriterion = Restrictions.eq("isActive", true);
        Criterion activeCourseCriterion = Restrictions.and(startDateCriterion, isActiveCriterion);

        Criterion completeCriterion = Restrictions.or(dateCriterion, activeCourseCriterion);

        criteria.add(completeCriterion);
    }

    @Override
    public Course get() {
        return courseDAO.get(criteria);
    }

    @Override
    public List<Course> getList() {
        return courseDAO.getList(criteria);
    }
}
