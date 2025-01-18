package com.hahn.erms.services;

import com.hahn.erms.entities.Department;

import java.util.List;
import java.util.Optional;

public interface DepartmentService {
    List<Department> getAllDepartments();
    Optional<Department> getDepartmentById(Long id);
    Department createDepartment(Department Department);
    Department updateDepartment(Long id, Department DepartmentDetails);
    void deleteDepartment(Long id);
}
