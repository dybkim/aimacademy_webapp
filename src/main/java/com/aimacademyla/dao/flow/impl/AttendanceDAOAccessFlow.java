package com.aimacademyla.dao.flow.impl;

import com.aimacademyla.dao.AttendanceDAO;
import com.aimacademyla.model.Attendance;
import com.aimacademyla.model.Charge;
import com.aimacademyla.model.Course;
import com.aimacademyla.model.Member;
import com.aimacademyla.model.initializer.impl.ChargeDefaultValueInitializer;
import com.aimacademyla.model.temporal.CyclePeriod;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LocalDateType;

import java.time.LocalDate;
import java.util.List;

public class AttendanceDAOAccessFlow extends AbstractDAOAccessFlowImpl<Attendance>{

    private AttendanceDAO attendanceDAO;

    public AttendanceDAOAccessFlow(){
        this.attendanceDAO = (AttendanceDAO) getDAOFactory().getDAO(Attendance.class);
        dispatch.put(Member.class, super::handleMember);
        dispatch.put(Course.class, this::handleCourse);
        dispatch.put(LocalDate.class, this::handleLocalDate);
        dispatch.put(CyclePeriod.class, this::handleCyclePeriod);
    }

    @Override
    public Attendance get(){
        return attendanceDAO.get(criteria);
    }

    @Override
    public List<Attendance> getList(){
        return attendanceDAO.getList(criteria);
    }

    void handleCourse(Object object, List<Criterion> criteria){
        Course course = (Course) object;
        criteria.add(Restrictions.sqlRestriction("{alias}.courseSessionID IN (SELECT courseSessionID FROM Course_Session WHERE courseID = ?)", course.getCourseID(), IntegerType.INSTANCE));
    }

    void handleLocalDate(Object object, List<Criterion> criteria){
        LocalDate localDate = (LocalDate) object;
        Criterion yearCriterion = Restrictions.sqlRestriction("YEAR({alias}.attendanceDate) = ?", localDate.getYear(), IntegerType.INSTANCE);
        Criterion monthCriterion = Restrictions.sqlRestriction("MONTH({alias}.attendanceDate) = ?", localDate.getMonthValue(), IntegerType.INSTANCE);
        Criterion dayCriterion = Restrictions.sqlRestriction("DAY({alias}.attendanceDate) = ?", localDate.getDayOfMonth(), IntegerType.INSTANCE);
        criteria.add(Restrictions.and(Restrictions.and(yearCriterion, monthCriterion), dayCriterion));
    }

    void handleCyclePeriod(Object object, List<Criterion> criteria){
        CyclePeriod cyclePeriod = (CyclePeriod) object;
        LocalDate periodStartDate = cyclePeriod.getCycleStartDate();
        LocalDate periodEndDate = cyclePeriod.getCycleEndDate();
        Criterion startCriterion = Restrictions.sqlRestriction("{alias}.attendanceDate >= ?", periodStartDate, LocalDateType.INSTANCE);
        Criterion endCriterion = Restrictions.sqlRestriction("{alias}.attendanceDate <= ?", periodEndDate, LocalDateType.INSTANCE);
        criteria.add(Restrictions.and(startCriterion, endCriterion));
    }
}
