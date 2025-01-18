package com.hahn.erms.utils;

import com.hahn.erms.entities.*;
import com.hahn.erms.enums.ContractType;
import com.hahn.erms.enums.Gender;
import com.hahn.erms.filters.JwtAuthenticationFilter;
import com.hahn.erms.services.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataSeeder implements CommandLineRunner {

    private final PermissionService permissionService;
    private final RoleService roleService;
    private final EmployeeService employeeService;
    private final AccountService accountService;
    private final DepartmentService departmentService;
    Logger logger = LoggerFactory.getLogger(DataSeeder.class);

    public DataSeeder(
            PermissionService permissionService,
            RoleService roleService,
            EmployeeService employeeService,
            AccountService accountService, DepartmentService departmentService) {
        this.permissionService = permissionService;
        this.roleService = roleService;
        this.employeeService = employeeService;
        this.accountService = accountService;
        this.departmentService = departmentService;
    }

    @Override
    public void run(String... args)  {

        try {
            // Seed permissions
            Permission readAll = permissionService.createPermission(Permission.builder().id(1L).name("READ_ALL_PRIVILEGE").build());
            Permission writeAll = permissionService.createPermission(Permission.builder().id(2L).name("WRITE_ALL_PRIVILEGE").build());
            Permission updateAll = permissionService.createPermission(Permission.builder().id(3L).name("UPDATE_ALL_PRIVILEGE").build());
            Permission writeDepartment = permissionService.createPermission(Permission.builder().id(4L).name("WRITE_DEP_PRIVILEGE").build());
            Permission updateDepartment = permissionService.createPermission(Permission.builder().id(5L).name("UPDATE_DEP_PRIVILEGE").build());
            Permission rootPermision = permissionService.createPermission(Permission.builder().id(6L).name("UPDATE_DEP_PRIVILEGE").build());

            // Seed roles
            Role hrRole = roleService.createRole(Role.builder().id(1L).name("ROLE_HR").build());
            Role managerRole = roleService.createRole(Role.builder().id(2L).name("ROLE_MANAGER").build());
            Role adminRole = roleService.createRole(Role.builder().id(3L).name("ROLE_ADMIN").build());

            // Map roles to permissions
            roleService.assignPermissionToRole(hrRole, readAll);
            roleService.assignPermissionToRole(hrRole, writeAll);
            roleService.assignPermissionToRole(hrRole, updateAll);
            roleService.assignPermissionToRole(managerRole, readAll);
            roleService.assignPermissionToRole(managerRole, writeDepartment);
            roleService.assignPermissionToRole(managerRole, updateDepartment);
            roleService.assignPermissionToRole(adminRole, rootPermision);
            // Add more role-permission mappings as needed...

            // Seed departments
            Department engineering = departmentService.createDepartment(Department.builder().id(1L).name("Engineering").build());
            Department hr = departmentService.createDepartment(Department.builder().id(2L).name("Human Resources").build());
            Department marketing = departmentService.createDepartment(Department.builder().id(3L).name("Marketing").build());
            Department it = departmentService.createDepartment(Department.builder().id(4L).name("Information Technology").build());


            Employee john = employeeService.createEmployee(Employee.builder()
                    .id(1L) // Ensure this ID is unique
                    .fullName("John Smith")
                    .employeeId("EMP001") // Unique employee ID
                    .jobTitle("Senior Software Engineer")
                    .department(engineering) // Ensure 'engineering' department exists
                    .hireDate(LocalDate.of(2010, 1, 12))
                    .employmentStatus("ACTIVE")
                    .address("123 Tech Street, Silicon Valley")
                    .contractType(ContractType.CONTRACT)
                    .contactInfo(ContactInfo.builder()
                            .email("john.smith@company.com") // Unique email
                            .phoneNumber("0666666666") // Unique phone number
                            .build())
                    .gender(Gender.MALE)
                    .build());

            Employee jack = employeeService.createEmployee(Employee.builder()
                    .id(2L)
                    .fullName("Jack Smith")
                    .employeeId("EMP002")
                    .jobTitle("DevOps Engineer")
                    .department(engineering)
                    .hireDate(LocalDate.of(2011, 3, 14))
                    .employmentStatus("ACTIVE")
                    .address("456 Tech Lane, Silicon Valley")
                    .contractType(ContractType.FULL_TIME)
                    .contactInfo(ContactInfo.builder()
                            .email("jack.smith@company.com")
                            .phoneNumber("0666666662")
                            .build())
                    .gender(Gender.MALE)
                    .build());

            Employee jane = employeeService.createEmployee(Employee.builder()
                    .id(3L)
                    .fullName("Jane Doe")
                    .employeeId("EMP003")
                    .jobTitle("Software Architect")
                    .department(engineering)
                    .hireDate(LocalDate.of(2000, 5, 2))
                    .employmentStatus("ACTIVE")
                    .address("789 Code Avenue, Tech City")
                    .contractType(ContractType.FREELANCE)
                    .contactInfo(ContactInfo.builder()
                            .email("jane.doe@company.com")
                            .phoneNumber("0666666661")
                            .build())
                    .gender(Gender.FEMALE)
                    .build());

            accountService.createAccount(Account.builder()
                    .username("admin")
                    .password("admin")
                    .employee(jack)
                    .role(adminRole)
                    .build());

            accountService.createAccount(Account.builder()
                    .username("hr")
                    .password("hr")
                    .employee(john)
                    .role(hrRole)
                    .build());

            accountService.createAccount(Account.builder()
                    .username("manager")
                    .password("manager")
                    .employee(jane)
                    .role(managerRole)
                    .build());
        }
        catch(Exception e){
            logger.warn("Error in intialization : {}", e.getLocalizedMessage());
        }
    }
}
