package com.hahn.erms.integrations;

import com.hahn.erms.dtos.LoginResponse;
import com.hahn.erms.entities.*;
import com.hahn.erms.enums.ContractType;
import com.hahn.erms.enums.Gender;
import com.hahn.erms.repositories.AccountRepository;
import com.hahn.erms.repositories.DepartmentRepository;
import com.hahn.erms.repositories.EmployeeRepository;
import com.hahn.erms.repositories.RoleRepository;
import com.hahn.erms.services.JwtService;
import com.hahn.erms.services.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class AuthServiceIntegrationTests {

    @Autowired
    private AuthServiceImpl authService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private EmployeeRepository employeeRepository;


    @Mock
    private JwtService jwtService;

    private Account account;
    private String username;
    private String password;


    @BeforeEach
    void setUp() {
        accountRepository.deleteAll();
        employeeRepository.deleteAll();
        departmentRepository.deleteAll();
        roleRepository.deleteAll();

        username = "johndoe";
        password = "password123";
        Department department = Department.builder()
                .name("IT")
                .build();
        Department savedDepartment = departmentRepository.save(department);

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
        Employee savedEmployee = employeeRepository.save(employee);

        Role role = Role.builder()
                .name("USER")
                .permissions(new HashSet<>())
                .build();

        Role savedRole = roleRepository.save(role);
        PasswordEncoder encoder = new BCryptPasswordEncoder();

        account = Account.builder().username(username).password(encoder.encode(password)).role(savedRole).employee(savedEmployee).build();
        accountRepository.save(account);
    }

    @Test
    void login_WhenCredentialsAreValid_ShouldReturnLoginResponse() {
        LoginResponse response = authService.login(username, password);

        assertNotNull(response);
        assertNotNull(response.token());
        assertNotNull(response.refreshToken());
    }

    @Test
    void login_WhenCredentialsAreInvalid_ShouldThrowBadCredentialsException() {
        assertThrows(org.springframework.security.authentication.BadCredentialsException.class, () -> {
            authService.login("johndoe", "wrongpassword");
        });
    }


    @Test
    void login_WhenUsernameIsNull_ShouldThrowIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authService.login(null, password);
        });
        assertEquals("Username and password must not be null", exception.getMessage());
    }

    @Test
    void login_WhenPasswordIsNull_ShouldThrowIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authService.login(username, null);
        });
        assertEquals("Username and password must not be null", exception.getMessage());
    }
}
