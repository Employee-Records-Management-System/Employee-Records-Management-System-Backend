package com.hahn.erms.services;

import com.hahn.erms.entities.Employee;
import com.hahn.erms.models.UserDetailsModel;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface EmployeeService {
    List<Employee> getAllEmployees();
    List<Employee> getAllEmployees(int page, int size, String sortField, Sort.Direction direction);
    List<Employee> getEmployeesByKeyword(int page, int size, String sortField, Sort.Direction direction, String keyword);
    Employee getEmployeeById(Long id);
    Employee createEmployee(Employee Employee);
    Employee updateEmployee(Long id, Employee EmployeeDetails);
    Employee updateEmployeeWithRoleCheck(Long id, Employee employeeDetails, UserDetailsModel userDetails);
    void deleteEmployee(Long id, UserDetailsModel userDetails);
    Employee getEmployeeByUsername(String username);
    List<String> getAllEmploymentStatus();
}
