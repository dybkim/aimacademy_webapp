package com.aimacademyla.dao.impl;

import com.aimacademyla.dao.EmployeeDAO;
import com.aimacademyla.model.Charge;
import com.aimacademyla.model.Employee;
import com.aimacademyla.model.enums.AIMEntityType;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository("employeeDAO")
@Transactional
public class EmployeeDAOImpl extends GenericDAOImpl<Employee, Integer> implements EmployeeDAO{
    public EmployeeDAOImpl() {
        super(Employee.class);
    }

    @Override
    public void removeList(List<Employee> employeeList){
        Session session = currentSession();
        List<Integer> employeeIDList = new ArrayList<>();
        for(Employee employee : employeeList)
            employeeIDList.add(employee.getEmployeeID());
        Query query = session.createQuery("DELETE FROM Employee E WHERE E.employeeID in :employeeIDList");
        query.setParameterList("employeeIDList", employeeIDList);
        query.executeUpdate();
    }
}
