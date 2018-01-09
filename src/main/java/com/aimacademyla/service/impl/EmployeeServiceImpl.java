package com.aimacademyla.service.impl;

import com.aimacademyla.dao.EmployeeDAO;
import com.aimacademyla.dao.GenericDAO;
import com.aimacademyla.model.enums.AIMEntityType;
import com.aimacademyla.model.Employee;
import com.aimacademyla.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class EmployeeServiceImpl extends GenericServiceImpl<Employee, Integer> implements EmployeeService{

    private EmployeeDAO employeeDAO;

    private final AIMEntityType AIM_ENTITY_TYPE = AIMEntityType.EMPLOYEE;

    @Autowired
    public EmployeeServiceImpl(@Qualifier("employeeDAO") GenericDAO<Employee, Integer> genericDAO){
        super(genericDAO);
        this.employeeDAO = (EmployeeDAO) genericDAO;
    }

    public List<Employee> getInactiveList(){
        List<Employee> employeeList = getList();
        List<Employee> inactiveList = new ArrayList<>();

        for(Employee employee : employeeList)
            if(!employee.getIsActive())
                inactiveList.add(employee);

        return inactiveList;
    }
}
