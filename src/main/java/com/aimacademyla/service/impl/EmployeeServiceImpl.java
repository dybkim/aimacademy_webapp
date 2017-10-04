package com.aimacademyla.service.impl;

import com.aimacademyla.dao.EmployeeDAO;
import com.aimacademyla.dao.GenericDAO;
import com.aimacademyla.model.Employee;
import com.aimacademyla.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl extends GenericServiceImpl<Employee, Integer> implements EmployeeService{

    private EmployeeDAO employeeDAO;

    @Autowired
    public EmployeeServiceImpl(@Qualifier("employeeDAO") GenericDAO<Employee, Integer> genericDAO){
        super(genericDAO);
        this.employeeDAO = (EmployeeDAO) genericDAO;
    }
}
