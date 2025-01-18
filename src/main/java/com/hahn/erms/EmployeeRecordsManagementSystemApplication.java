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

}
