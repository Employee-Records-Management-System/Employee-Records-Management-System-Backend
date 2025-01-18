package com.hahn.erms.services;

import com.hahn.erms.entities.Department;
import com.hahn.erms.repositories.DepartmentRepository;
import com.hahn.erms.services.impl.DepartmentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceTests {

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private DepartmentServiceImpl departmentService;

    private Department testDepartment;

    @BeforeEach
    void setUp() {
        testDepartment = Department.builder()
                .id(1L)
                .name("IT")
                .build();
    }

    @Test
    void getAllDepartments_ShouldReturnListOfDepartments() {
        when(departmentRepository.findAll()).thenReturn(Arrays.asList(testDepartment));

        List<Department> departments = departmentService.getAllDepartments();

        assertNotNull(departments);
        assertEquals(1, departments.size());
        verify(departmentRepository, times(1)).findAll();
    }

    @Test
    void getDepartmentById_WhenDepartmentExists_ShouldReturnDepartment() {
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(testDepartment));

        Optional<Department> result = departmentService.getDepartmentById(1L);

        assertTrue(result.isPresent());
        assertEquals(testDepartment.getName(), result.get().getName());
        verify(departmentRepository, times(1)).findById(1L);
    }
}
