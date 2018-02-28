package com.aimacademyla.dao.flow.impl;

import com.aimacademyla.dao.ChargeDAO;
import com.aimacademyla.model.Charge;
import com.aimacademyla.model.Course;
import com.aimacademyla.model.Member;
import com.aimacademyla.model.initializer.impl.ChargeDefaultValueInitializer;
import com.aimacademyla.model.temporal.CyclePeriod;

import java.time.LocalDate;
import java.util.List;


public class ChargeDAOAccessFlow extends AbstractDAOAccessFlowImpl<Charge>{

    private ChargeDAO chargeDAO;

    public ChargeDAOAccessFlow(){
        this.chargeDAO = (ChargeDAO) getDAOFactory().getDAO(Charge.class);

        dispatch.put(Member.class, super::handleMember);
        dispatch.put(Course.class, super::handleCourse);
        dispatch.put(LocalDate.class, super::handleLocalDate);
        dispatch.put(CyclePeriod.class, super::handleCyclePeriod);
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
            charge = new ChargeDefaultValueInitializer()
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
