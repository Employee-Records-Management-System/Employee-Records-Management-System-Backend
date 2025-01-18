package com.hahn.erms;

import com.hahn.erms.entities.Account;
import com.hahn.erms.entities.Employee;
import com.hahn.erms.entities.Role;
import com.hahn.erms.services.AccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class EmployeeRecordsManagementSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmployeeRecordsManagementSystemApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(AccountService accountService) {
        return args -> {
            accountService.createAccount(Account.builder()
                    .username("hr")
                    .password("hr")
                    .employee(Employee.builder().id(1L).build())
                    .role(Role.builder().id(1L).build())
                    .build());
            accountService.createAccount(Account.builder()
                    .username("manager")
                    .password("manager")
                    .employee(Employee.builder().id(2L).build())
                    .role(Role.builder().id(2L).build())
                    .build());
            accountService.createAccount(Account.builder()
                    .username("admin")
                    .password("admin")
                    .employee(Employee.builder().id(3L).build())
                    .role(Role.builder().id(3L).build())
                    .build());
        };
    }
}
