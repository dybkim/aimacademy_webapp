package com.aimacademyla.dao.impl;

import com.aimacademyla.dao.EmployeeDAO;
import com.aimacademyla.model.Employee;
import com.aimacademyla.model.enums.AIMEntityType;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository("employeeDAO")
@Transactional
public class EmployeeDAOImpl extends GenericDAOImpl<Employee, Integer> implements EmployeeDAO{

    private final AIMEntityType AIM_ENTITY_TYPE = AIMEntityType.EMPLOYEE;

    public EmployeeDAOImpl() {
        super(Employee.class);
    }

    @Override
    public AIMEntityType getAIMEntityType() {
        return AIM_ENTITY_TYPE;
    }
}
