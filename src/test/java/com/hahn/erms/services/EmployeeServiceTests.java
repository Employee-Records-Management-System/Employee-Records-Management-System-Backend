package com.hahn.erms.services;

import com.hahn.erms.entities.*;
import com.hahn.erms.enums.ContractType;
import com.hahn.erms.enums.Gender;
import com.hahn.erms.errors.NotAuthorizedException;
import com.hahn.erms.models.UserDetailsModel;
import com.hahn.erms.repositories.EmployeeRepository;
import com.hahn.erms.services.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTests {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private AccountService accountService;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;
    private Department department;
    private Account account;
    private UserDetailsModel userDetails;

    @BeforeEach
    void setUp() {
        department = Department.builder()
                .id(1L)
                .name("IT")
                .build();

        ContactInfo contactInfo = ContactInfo.builder()
                .email("john.doe@company.com")
                .phoneNumber("+1234567890")
                .build();
        Role role = Role.builder()
                .name("ROLE_EMPLOYEE")
                .permissions(Set.of(new Permission(1L, "CREATE", Instant.now(), Instant.now())))
                .build();
        employee = Employee.builder()
                .id(1L)
                .fullName("John Doe")
                .employeeId("EMP001")
                .jobTitle("Software Engineer")
                .gender(Gender.MALE)
                .department(department)
                .hireDate(LocalDate.now().minusYears(1))
                .employmentStatus("ACTIVE")
                .contractType(ContractType.FREELANCE)
                .contactInfo(contactInfo)
                .address("123 Main St, City")
                .build();

        account = new Account();
        account.setId(1L);
        account.setRole(role);
        account.setUsername("johndoe");
        account.setEmployee(employee);

        userDetails = new UserDetailsModel(account);
    }

    @Test
    void getAllEmployees_ShouldReturnPagedEmployees() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "fullName"));
        List<Employee> employeeList = Collections.singletonList(employee);
        Page<Employee> employeePage = new PageImpl<>(employeeList);
        when(employeeRepository.findAll(pageable)).thenReturn(employeePage);

        // Act
        List<Employee> result = employeeService.getAllEmployees(0, 10, "fullName", Sort.Direction.ASC);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getFullName());
        assertEquals("EMP001", result.get(0).getEmployeeId());
        verify(employeeRepository).findAll(pageable);
    }

    @Test
    void getAllDepartmentEmployees_ShouldReturnEmployeesList() {
        // Arrange
        when(employeeRepository.findByDepartmentId(1L))
                .thenReturn(Collections.singletonList(employee));

        // Act
        List<Employee> result = employeeService.getAllDepartmentEmployees(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(department.getId(), result.get(0).getDepartment().getId());
    }

    @Test
    void getEmployeeByManagerIdAndDepartmentId_WhenAuthorized_ShouldReturnEmployee() {
        // Arrange
        when(employeeRepository.findByIdAndDepartmentId(1L, 1L))
                .thenReturn(Optional.of(employee));

        // Act
        Employee result = employeeService.getEmployeeByManagerIdAndDepartmentId(1L, 1L);

        // Assert
        assertNotNull(result);
        assertEquals(employee.getId(), result.getId());
        assertEquals(employee.getDepartment().getId(), result.getDepartment().getId());
    }

    @Test
    void getEmployeeByManagerIdAndDepartmentId_WhenUnauthorized_ShouldThrowException() {
        // Arrange
        when(employeeRepository.findByIdAndDepartmentId(1L, 2L))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotAuthorizedException.class,
                () -> employeeService.getEmployeeByManagerIdAndDepartmentId(1L, 2L));
    }

    @Test
    void createEmployee_ShouldReturnSavedEmployee() {
        // Arrange
        Employee newEmployee = Employee.builder()
                .fullName("Jane Smith")
                .employeeId("EMP002")
                .jobTitle("Business Analyst")
                .gender(Gender.FEMALE)
                .department(department)
                .hireDate(LocalDate.now())
                .employmentStatus("ACTIVE")
                .contractType(ContractType.FREELANCE)
                .address("456 Oak St, City")
                .build();

        when(employeeRepository.save(any(Employee.class))).thenReturn(newEmployee);

        // Act
        Employee result = employeeService.createEmployee(newEmployee);

        // Assert
        assertNotNull(result);
        assertEquals("Jane Smith", result.getFullName());
        assertEquals("EMP002", result.getEmployeeId());
        assertEquals("Business Analyst", result.getJobTitle());
        verify(employeeRepository).save(newEmployee);
    }

    @Test
    void updateEmployeeWithRoleCheck_WhenManagerUpdatingOwnDepartment_ShouldSucceed() {
        // Arrange
        Employee updatedEmployee = employee.builder()
                .jobTitle("Senior Software Engineer")
                .employmentStatus("PROMOTED")
                .build();

        UserDetailsModel managerDetails = new UserDetailsModel(account);
        managerDetails.getAuthorities().add(new SimpleGrantedAuthority("ROLE_MANAGER"));

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(updatedEmployee);

        // Act
        Employee result = employeeService.updateEmployeeWithRoleCheck(1L, updatedEmployee, managerDetails);

        // Assert
        assertNotNull(result);
        assertEquals("Senior Software Engineer", result.getJobTitle());
        assertEquals("PROMOTED", result.getEmploymentStatus());
        verify(employeeRepository).save(any(Employee.class));
    }

    @Test
    void getAllEmploymentStatus_ShouldReturnStatusList() {
        // Arrange
        List<String> statuses = List.of("ACTIVE", "ON_LEAVE", "TERMINATED");
        when(employeeRepository.findEmploymentStatus()).thenReturn(statuses);

        // Act
        List<String> result = employeeService.getAllEmploymentStatus();

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.contains("ACTIVE"));
        assertTrue(result.contains("ON_LEAVE"));
        assertTrue(result.contains("TERMINATED"));
    }
}