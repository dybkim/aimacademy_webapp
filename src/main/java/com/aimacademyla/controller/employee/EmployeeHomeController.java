package com.aimacademyla.controller.employee;


import com.aimacademyla.model.Employee;
import com.aimacademyla.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Controller
@RequestMapping("/admin/employee")
public class EmployeeHomeController {

    private EmployeeService employeeService;

    @Autowired
    public EmployeeHomeController(EmployeeService employeeService){
        this.employeeService = employeeService;
    }

    @RequestMapping
    public String getEmployeeList(Model model){
        List<Employee> employeeList = employeeService.getList();
        List<Employee> inactiveEmployeeList = new ArrayList<>();

        Iterator it = employeeList.iterator();

        while(it.hasNext()){
            Employee employee = (Employee) it.next();

            if(!employee.isActive()){
                inactiveEmployeeList.add(employee);
                it.remove();
            }
        }

        model.addAttribute("employeeList", employeeList);
        model.addAttribute("inactiveEmployeeList", inactiveEmployeeList);
        return "employee/employeeList";
    }


}
