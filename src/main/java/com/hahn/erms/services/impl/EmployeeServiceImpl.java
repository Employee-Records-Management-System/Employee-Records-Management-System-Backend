package com.hahn.erms.services.impl;

import com.hahn.erms.entities.Employee;
import com.hahn.erms.errors.NotAuthorizedException;
import com.hahn.erms.models.UserDetailsModel;
import com.hahn.erms.repositories.EmployeeRepository;
import com.hahn.erms.services.AccountService;
import com.hahn.erms.services.EmployeeService;
import com.hahn.erms.utils.EntityUtils;
import com.hahn.erms.utils.ValidationUtils;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final AccountService accountService;

    public EmployeeServiceImpl(EmployeeRepository AccountRepository, AccountService accountService) {
        this.employeeRepository = AccountRepository;
        this.accountService = accountService;
    }

    public List<Employee> getAllEmployees() {
        return this.employeeRepository.findAll();
    }

    public List<Employee> getAllEmployees(int page, int size, String sortField, Sort.Direction direction) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
        Page<Employee> employeesPage = employeeRepository.findAll(pageable);
        return employeesPage.getContent();
    }

    public List<Employee> getEmployeesByKeyword(int page, int size, String sortField, Sort.Direction direction, String keyword) {
        return List.of();
    }

    public List<Employee> getAllDepartmentEmployees(long departmentId) {
        return employeeRepository.findByDepartmentId(departmentId);
    }

    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Employee not found"));
    }

    public Employee getEmployeeByManagerIdAndDepartmentId(Long id, Long departmentId) {
        return employeeRepository.findByIdAndDepartmentId(id, departmentId).orElseThrow(() -> new NotAuthorizedException("A manager can't access employees from other departments"));
    }


    public Employee createEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public Employee updateEmployee(Long id, Employee employeeDetails) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Employee not found"));
        return employeeRepository.save(employee);
    }
    public Employee updateEmployeeWithRoleCheck(Long id, Employee employeeDetails, UserDetailsModel userDetails) {
        Employee existingEmployee = getEmployeeById(id);

        boolean isManager = userDetails.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_MANAGER"));

        if (isManager &&
                !Objects.equals(existingEmployee.getDepartment().getId(),
                        userDetails.getAccount()
                                .getEmployee()
                                .getDepartment()
                                .getId())) {

                throw new AccessDeniedException("Managers can only update employees within their own department.");
        }
            employeeDetails.setId(null);
            EntityUtils.copyNonNullProperties(employeeDetails, existingEmployee);
        ValidationUtils.validate(existingEmployee);
        return employeeRepository.save(existingEmployee);
    }

    public void deleteEmployee(Long id, UserDetailsModel userDetails) {
        Employee existingEmployee = getEmployeeById(id);

        boolean isManager = userDetails.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_MANAGER"));

        if (isManager &&
                !Objects.equals(existingEmployee.getDepartment().getId(),
                        userDetails.getAccount()
                                .getEmployee()
                                .getDepartment()
                                .getId())) {

            throw new AccessDeniedException("Managers can only update employees within their own department.");
        }
        employeeRepository.deleteById(id);
    }

    public Employee getEmployeeByUsername(String username) {
        return accountService.getAccountByUsername(username).orElseThrow(() -> new EntityNotFoundException("Employee not found")).getEmployee();
    }

    public List<String> getAllEmploymentStatus() {
        return employeeRepository.findEmploymentStatus();
    }
}

