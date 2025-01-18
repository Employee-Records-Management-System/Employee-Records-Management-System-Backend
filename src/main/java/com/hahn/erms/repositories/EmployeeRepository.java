package com.hahn.erms.repositories;

import com.hahn.erms.entities.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNullApi;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByDepartmentId(Long departmentId);
    Optional<Employee> findByIdAndDepartmentId(Long id, Long department_id);
    List<Employee> findByJobTitle(String jobTitle);
    @Query("SELECT e FROM Employee e WHERE " +
            "LOWER(e.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(e.employeeId) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(e.jobTitle) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(e.contactInfo.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(e.contactInfo.phoneNumber) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Employee> findByKeyword(@Param("keyword") String keyword, Pageable pageable);
    @Query("SELECT DISTINCT e.employmentStatus FROM Employee e")
    List<String> findEmploymentStatus();

}
