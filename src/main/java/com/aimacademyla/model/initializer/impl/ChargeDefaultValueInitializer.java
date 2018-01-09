package com.aimacademyla.model.initializer.impl;

import com.aimacademyla.dao.CourseDAO;
import com.aimacademyla.dao.MonthlyFinancesSummaryDAO;
import com.aimacademyla.dao.SeasonDAO;
import com.aimacademyla.dao.factory.DAOFactory;
import com.aimacademyla.dao.flow.impl.MonthlyFinancesSummaryDAOAccessFlow;
import com.aimacademyla.dao.flow.impl.PaymentDAOAccessFlow;
import com.aimacademyla.model.*;
import com.aimacademyla.model.builder.entity.ChargeBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * ChargeDefaultValueInitializer initializes a charge to have a value of 0 for all its numeric fields.
 */

public class ChargeDefaultValueInitializer extends GenericDefaultValueInitializerImpl<Charge>{


    private Member member;
    private Course course;
    private LocalDate localDate;

    public ChargeDefaultValueInitializer(DAOFactory daoFactory){
        super(daoFactory);
    }

    @Override
    public Charge initialize() {
        Charge charge = new Charge();
        MonthlyFinancesSummary monthlyFinancesSummary = (MonthlyFinancesSummary) new MonthlyFinancesSummaryDAOAccessFlow(getDAOFactory())
                                                                                    .addQueryParameter(localDate)
                                                                                    .get();

        Payment payment = (Payment) new PaymentDAOAccessFlow(getDAOFactory())
                                        .addQueryParameter(member)
                                        .addQueryParameter(LocalDate.of(localDate.getYear(), localDate.getMonthValue(), 1))
                                        .get();

        if(course.getCourseID() != Course.OTHER_ID)
            charge.setDescription(course.getCourseName() + " (" + course.getCourseType()+")");

        charge = new ChargeBuilder()
                                .setMember(member)
                                .setCourse(course)
                                .setChargeAmount(BigDecimal.ZERO)
                                .setCycleStartDate(LocalDate.of(localDate.getYear(), localDate.getMonth(), 1))
                                .setDiscountAmount(BigDecimal.ZERO)
                                .setBillableUnitsType(course.getBillableUnitType())
                                .setBillableUnitsBilled(BigDecimal.ZERO)
                                .setPayment(payment)
                                .setMonthlyFinancesSummary(monthlyFinancesSummary)
                                .setNumChargeLines(0)
                                .setDiscountAmount(BigDecimal.ZERO)
                                .setDescription(course.getCourseName() + " (" + course.getCourseType() +")")
                                .build();
        return charge;
    }

    public ChargeDefaultValueInitializer setMember(Member member){
        this.member = member;
        return this;
    }

    public ChargeDefaultValueInitializer setCourse(Course course){
        this.course = course;
        return this;
    }

    public ChargeDefaultValueInitializer setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
        return this;
    }
}
