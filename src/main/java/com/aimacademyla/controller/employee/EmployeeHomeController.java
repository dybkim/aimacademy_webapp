package com.aimacademyla.controller.employee;


import com.aimacademyla.dao.EmployeeDAO;
import com.aimacademyla.model.Employee;
import com.aimacademyla.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        List<Employee> inactiveEmployeeList = employeeService.getInactiveList();

        model.addAttribute("employeeList", employeeList);
        model.addAttribute("inactiveEmployeeList", inactiveEmployeeList);
        return "employee/employeeList";
    }

    @RequestMapping(value="/addEmployee")
    public String addEmployee(Model model){
        Employee employee = new Employee();

        model.addAttribute("employee", employee);
        return "employee/addEmployee";
    }

    @RequestMapping(value="/addEmployee", method= RequestMethod.POST)
    public String addEmployee(@ModelAttribute("employee") Employee employee, BindingResult result, RedirectAttributes redirectAttributes){
        if(hasErrors(result)) {
            addErrorMessages(result.getFieldErrors(), redirectAttributes);
            return "redirect:/admin/employee/addEmployee";
        }

        employeeService.add(employee);
        return "redirect:/admin/employee";
    }

    @RequestMapping(value="/editEmployee/{id}")
    public String editEmployee(@PathVariable("id") int employeeID, Model model){
        Employee employee = employeeService.get(employeeID);

        model.addAttribute("employee", employee);

        return "employee/editEmployee";
    }

    @RequestMapping(value="/editEmployee", method=RequestMethod.POST)
    public String editEmployee(@ModelAttribute("employee") Employee employee, BindingResult result, RedirectAttributes redirectAttributes){
        if(hasErrors(result)) {
            addErrorMessages(result.getFieldErrors(), redirectAttributes);
            return "redirect:/admin/employee/editEmployee" + employee.getEmployeeID();
        }

        employeeService.update(employee);

        return "redirect:/admin/employee";
    }

    @RequestMapping(value="/{id}")
    public String getEmployeeInfo(@PathVariable("id") int employeeID, Model model){
        Employee employee = employeeService.get(employeeID);

        model.addAttribute("employee", employee);

        return "employee/employeeInfo";
    }

    private boolean hasErrors(BindingResult result){
        if(result.hasErrors()) {
            List<FieldError> errors = result.getFieldErrors();

            for (FieldError error : errors) {
                if (error.getField().equals("dateEmployed"))
                    return true;
            }
        }
        return false;
    }

    private boolean hasDateFormatErrors(List<FieldError> errorList){
        for (FieldError error : errorList) {
            if (error.getField().equals("dateEmployed"))
                return true;
        }
        return false;
    }

    private RedirectAttributes addErrorMessages(List<FieldError> errorList, RedirectAttributes redirectAttributes){
        if(hasDateFormatErrors(errorList))
            addDateFormateErrorMessage(redirectAttributes);

        return redirectAttributes;
    }

    private RedirectAttributes addDateFormateErrorMessage(RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("dateEmployedErrorMessage", "Date must be in valid MM/DD/YYYY format");
        return redirectAttributes;
    }

}
