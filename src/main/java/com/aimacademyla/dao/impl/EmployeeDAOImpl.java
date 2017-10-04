package com.aimacademyla.dao.impl;

import com.aimacademyla.dao.EmployeeDAO;
import com.aimacademyla.model.Employee;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository("employeeDAO")
@Transactional
public class EmployeeDAOImpl extends GenericDAOImpl<Employee, Integer> implements EmployeeDAO{

    public EmployeeDAOImpl() {
        super(Employee.class);
    }

}
