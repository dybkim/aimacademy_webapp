package com.aimacademyla.service;

import com.aimacademyla.model.Employee;

import java.util.List;

public interface EmployeeService extends GenericService<Employee, Integer> {
    List<Employee> getInactiveList();
}
