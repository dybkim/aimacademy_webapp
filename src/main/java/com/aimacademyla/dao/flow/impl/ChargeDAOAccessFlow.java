package com.aimacademyla.dao.flow.impl;

import com.aimacademyla.dao.ChargeDAO;
import com.aimacademyla.dao.factory.DAOFactory;
import com.aimacademyla.model.*;
import com.aimacademyla.model.enums.AIMEntityType;
import com.aimacademyla.model.initializer.impl.ChargeDefaultValueInitializer;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LocalDateType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ChargeDAOAccessFlow extends AbstractDAOAccessFlowImpl<Charge>{

    private ChargeDAO chargeDAO;

    public ChargeDAOAccessFlow(DAOFactory daoFactory){
        super(daoFactory);
        this.chargeDAO = (ChargeDAO) getDaoFactory().getDAO(Charge.class);

        dispatch.put(Member.class, super::handleMember);
        dispatch.put(Course.class, super::handleCourse);
        dispatch.put(LocalDate.class, super::handleLocalDate);
        dispatch.put(AbstractDAOAccessFlowImpl.CyclePeriod.class, super::handleCyclePeriod);
    }

    @Override
    public Charge get(){
        Member member = (Member) getQueryParameter(Member.class);
        Course course = (Course) getQueryParameter(Course.class);
        LocalDate cycleStartDate = (LocalDate) getQueryParameter(LocalDate.class);

        if(member == null || course == null || cycleStartDate == null)
            throw new NullPointerException();

        Charge charge = chargeDAO.get(criteria);

        if(charge == null) {
            charge = new ChargeDefaultValueInitializer(getDaoFactory())
                    .setCourse(course)
                    .setMember(member)
                    .setLocalDate(cycleStartDate)
                    .initialize();
            chargeDAO.add(charge);
        }

        return charge;
    }

    @Override
    public List<Charge> getList(){
        return chargeDAO.getList(criteria);
    }
}
