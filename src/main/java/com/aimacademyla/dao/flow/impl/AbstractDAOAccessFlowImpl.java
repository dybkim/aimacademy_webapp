package com.aimacademyla.dao.flow.impl;

import com.aimacademyla.dao.factory.DAOFactory;
import com.aimacademyla.dao.flow.DAOAccessFlow;
import com.aimacademyla.model.Course;
import com.aimacademyla.model.Member;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LocalDateType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractDAOAccessFlowImpl<T> implements DAOAccessFlow<T> {

    private DAOFactory daoFactory;
    HashMap<Class, Object> parameterHashMap;
    List<Criterion> criteria;

    private static final Logger logger = LogManager.getLogger(AbstractDAOAccessFlowImpl.class.getName());

    AbstractDAOAccessFlowImpl(DAOFactory daoFactory){
        this.daoFactory = daoFactory;
        parameterHashMap = new HashMap<>();
        criteria = new ArrayList<>();
    }

    DAOFactory getDaoFactory(){
        return daoFactory;
    }

    /*
     * Uses deprecated Criterion class because using JPA Predicate requires a Hibernate Session (needed to get CriteriaBuilder to get Root)
     */
    @Override
    public AbstractDAOAccessFlowImpl addQueryParameter(Object object){
        parameterHashMap.put(object.getClass(), object);
        Handler handler = dispatch.get(object.getClass());
        if(handler != null)
            handler.handle(object, criteria);

        return this;
    }

    @Override
    public Object getQueryParameter(Class classType){
        return parameterHashMap.get(classType);
    }

    @Override
    public abstract T get();

    @Override
    public abstract List<T> getList();

    /*
     * Handler interface adds the appropriate query criteria to the criteria list based on the entity type
     */
    interface Handler {
        void handle(Object o, List<Criterion> criteria);
    }

    /*
    * Dispatch map is used to map Entity class types to certain query Criteria to build and add Criteria when Entity objects are added as
    * query parameters
    *
    * Dispatch map is implemented in this manner to mimic the behavior of a switch statement (since we can't use switch statements on class types)
    */
    final Map<Class, Handler> dispatch = new HashMap<>();

    /*
     * NOTE: For Hibernate.Criteria, when using Restrictions to generate criteria, use property names in the entity POJO's, not the
     * row name in the database itself.
     *
     * When using Restrictions.sqlRestriction however, use the row name in the database, NOT the property name in the entity POJO's.
     */
    void handleMember(Object object, List<Criterion> criteria){
        Member member = (Member) object;
        criteria.add(Restrictions.eq("member.memberID", member.getMemberID()));
        logger.debug("Added query parameter: Member");
    }

    void handleCourse(Object object, List<Criterion> criteria){
        Course course = (Course) object;
        criteria.add(Restrictions.eq("course.courseID", course.getCourseID()));
        logger.debug("Added query parameter: Course");
    }

    void handleLocalDate(Object object, List<Criterion> criteria){
        LocalDate localDate = (LocalDate) object;
        Criterion yearCriterion = Restrictions.sqlRestriction("YEAR({alias}.CycleStartDate) = ?", localDate.getYear(), IntegerType.INSTANCE);
        Criterion monthCriterion = Restrictions.sqlRestriction("MONTH({alias}.CycleStartDate) = ?", localDate.getMonthValue(), IntegerType.INSTANCE);
        criteria.add(Restrictions.and(yearCriterion, monthCriterion));
        logger.debug("Added query parameter: LocalDate");
    }

    void handleCyclePeriod(Object object, List<Criterion> criteria){
        CyclePeriod cyclePeriod = (CyclePeriod) object;
        LocalDate periodStartDate = cyclePeriod.getCycleStartDate();
        LocalDate periodEndDate = cyclePeriod.getCycleEndDate();
        Criterion startCriterion = Restrictions.sqlRestriction("{alias}.CycleStartDate >= ?", periodStartDate, LocalDateType.INSTANCE);
        Criterion endCriterion = Restrictions.sqlRestriction("{alias}.CycleStartDate <= ?", periodEndDate, LocalDateType.INSTANCE);
        criteria.add(Restrictions.and(startCriterion, endCriterion));
    }

    public static class CyclePeriod{
        private LocalDate cycleStartDate;
        private LocalDate cycleEndDate;

        public LocalDate getCycleStartDate() {
            return cycleStartDate;
        }

        public CyclePeriod setCycleStartDate(LocalDate cycleStartDate) {
            this.cycleStartDate = cycleStartDate;
            return this;
        }

        public LocalDate getCycleEndDate() {
            return cycleEndDate;
        }

        public CyclePeriod setCycleEndDate(LocalDate cycleEndDate) {
            this.cycleEndDate = cycleEndDate;
            return this;
        }
    }
}
