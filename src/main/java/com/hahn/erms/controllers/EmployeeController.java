package com.hahn.erms.controllers;

import com.hahn.erms.entities.Employee;
import com.hahn.erms.models.UserDetailsModel;
import com.hahn.erms.services.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public List<Employee> getAllEmployees(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                          @RequestParam(value = "size", required = false, defaultValue = "10") int size,
                                          @RequestParam(value = "sortField", required = false, defaultValue = "id") String sortField,
                                          @RequestParam(value = "direction", required = false) Sort.Direction direction,
                                          @RequestParam(required = false) String keyword
                                          ) {
        if(keyword != null && !keyword.isEmpty()){
            return employeeService.getEmployeesByKeyword(page, size, sortField, direction, keyword);
        }
        direction = (direction != null) ? direction : Sort.Direction.ASC;
        return employeeService.getAllEmployees(page, size, sortField, direction);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    @GetMapping("/employment-status")
    public ResponseEntity<List<String>> getAllEmploymentStatus() {
        return ResponseEntity.ok(employeeService.getAllEmploymentStatus());
    }


    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_HR')")
    @PostMapping
    public ResponseEntity<Employee> createEmployee(@Valid @RequestBody Employee employee) {
        Employee createdEmployee = employeeService.createEmployee(employee);
        return new ResponseEntity<>(createdEmployee, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_HR', 'ROLE_MANAGER')")
    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee employeeDetails, Authentication authentication) {
        UserDetailsModel userDetails = (UserDetailsModel) authentication.getPrincipal();
        Employee updatedEmployee = employeeService.updateEmployeeWithRoleCheck(id, employeeDetails, userDetails);
        return ResponseEntity.ok(updatedEmployee);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_HR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }
}
