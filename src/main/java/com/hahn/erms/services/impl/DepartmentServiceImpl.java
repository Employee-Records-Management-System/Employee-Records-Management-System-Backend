package com.hahn.erms.services.impl;

import com.hahn.erms.entities.Department;
import com.hahn.erms.repositories.DepartmentRepository;
import com.hahn.erms.services.DepartmentService;
import com.hahn.erms.utils.EntityUtils;
import com.hahn.erms.utils.ValidationUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Validator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public Optional<Department> getDepartmentById(Long id) {
        return departmentRepository.findById(id);
    }

    public Department createDepartment(Department department) {
        return departmentRepository.save(department);
    }

    public Department updateDepartment(Long id, Department departmentDetails) {
        Department department = departmentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Account not found"));
        EntityUtils.copyNonNullProperties(departmentDetails, department);
        ValidationUtils.validate(department);
        return departmentRepository.save(department);
    }

    public void deleteDepartment(Long id) {
        departmentRepository.deleteById(id);
    }
}

