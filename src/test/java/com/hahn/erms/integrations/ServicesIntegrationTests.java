package com.hahn.erms.integrations;

import com.hahn.erms.entities.*;
import com.hahn.erms.enums.ContractType;
import com.hahn.erms.enums.Gender;
import com.hahn.erms.services.AccountService;
import com.hahn.erms.services.DepartmentService;
import com.hahn.erms.services.EmployeeService;
import com.hahn.erms.services.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ServicesIntegrationTests {

    @Autowired
    private AccountService accountService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private RoleService roleService;

    private Employee savedEmployee;
    private Role savedRole;

    @BeforeEach
    void setUp() {
        accountService.getAllAccounts().forEach(account ->
                accountService.deleteAccount(account.getId()));
        employeeService.getAllEmployees().forEach( employee ->
                employeeService.deleteEmployee(employee.getId()));
        departmentService.getAllDepartments().forEach(department ->
                departmentService.deleteDepartment(department.getId()));
        roleService.getAllRoles().forEach(role ->
                roleService.deleteRole(role.getId()));

        Department department = Department.builder()
                .name("IT")
                .build();
        Department savedDepartment = departmentService.createDepartment(department);

        Employee employee = Employee.builder()
                .fullName("John Doe")
                .employeeId("EMP001")
                .gender(Gender.MALE)
                .contractType(ContractType.CONTRACT)
                .address("Address")
                .contactInfo(ContactInfo.builder()
                        .email("email@email.com")
                        .phoneNumber("0666666666")
                        .build())
                .department(savedDepartment)
                .hireDate(LocalDate.of(2000,10,20))
                .jobTitle("Developer")
                .employmentStatus("Active")
                .build();
        savedEmployee = employeeService.createEmployee(employee);

        Role role = Role.builder()
                .name("USER")
                .permissions(new HashSet<>())
                .build();
        savedRole = roleService.createRole(role);
    }

    @Test
    void createAndRetrieveAccount() {
        Account account = Account.builder()
                .username("johndoe")
                .password("password123")
                .employee(savedEmployee)
                .role(savedRole)
                .build();

        Account savedAccount = accountService.createAccount(account);

        assertNotNull(savedAccount.getId());

        Optional<Account> retrieved = accountService.getAccountById(savedAccount.getId());
        assertTrue(retrieved.isPresent());
        assertEquals("johndoe", retrieved.get().getUsername());
        assertEquals(savedEmployee.getId(), retrieved.get().getEmployee().getId());
        assertEquals(savedRole.getId(), retrieved.get().getRole().getId());
    }

    @Test
    void updateAccount() {
        Account account = Account.builder()
                .username("johndoe")
                .password("password123")
                .employee(savedEmployee)
                .role(savedRole)
                .build();

        Account savedAccount = accountService.createAccount(account);
        Account accountDetails = Account.builder()
                .username("johndoe-new")
                .employee(Employee.builder().fullName("James Gosling").build())
                .build();
        Account updatedAccount = accountService.updateAccount(savedAccount.getId(), accountDetails);

        assertEquals("johndoe-new", updatedAccount.getUsername());
        assertEquals(savedEmployee.getId(), updatedAccount.getEmployee().getId());

        Optional<Account> retrievedAccount = accountService.getAccountById(updatedAccount.getId());
        assertTrue(retrievedAccount.isPresent());
        assertEquals("johndoe-new", retrievedAccount.get().getUsername());
    }

    @Test
    void deleteAccount() {
        Account account = Account.builder()
                .username("johndoe")
                .password("password123")
                .employee(savedEmployee)
                .role(savedRole)
                .build();

        Account savedAccount = accountService.createAccount(account);

        accountService.deleteAccount(savedAccount.getId());

        Optional<Account> deletedAccount = accountService.getAccountById(savedAccount.getId());
        assertFalse(deletedAccount.isPresent());
    }

    @Test
    void getAccountByUsername() {
        Account account = Account.builder()
                .username("johndoe")
                .password("password123")
                .employee(savedEmployee)
                .role(savedRole)
                .build();

        accountService.createAccount(account);

        Optional<Account> retrieved = accountService.getAccountByUsername("johndoe");
        assertTrue(retrieved.isPresent());
        assertEquals("johndoe", retrieved.get().getUsername());
        assertEquals(savedEmployee.getId(), retrieved.get().getEmployee().getId());
    }

    @Test
    void createAccountWithInvalidEmployee_ShouldFail() {
        Employee nonExistentEmployee = Employee.builder()
                .id(999L)
                .fullName("Non Existent")
                .build();

        Account account = Account.builder()
                .username("johndoe")
                .password("password123")
                .employee(nonExistentEmployee)
                .role(savedRole)
                .build();

        assertThrows(Exception.class, () -> accountService.createAccount(account));
    }
}