package com.hahn.erms.entities;

import com.hahn.erms.enums.ContractType;
import com.hahn.erms.enums.Gender;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.annotations.CurrentTimestamp;

import java.time.Instant;
import java.time.LocalDate;

import static org.hibernate.generator.EventType.INSERT;
import static org.hibernate.generator.EventType.UPDATE;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "FullName should not be empty")
    @Pattern(regexp = "^[a-zA-Z]+\\s+[a-zA-Z]+.*$", message = "Full name should be at least two words")
    private String fullName;

    @Column(unique = true)
    private String employeeId;

    @NotBlank(message = "Job title should not be empty")
    private String jobTitle;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Gender should not be null")
    private Gender gender;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @NotNull(message = "Hire date should not be null")
    @PastOrPresent(message = "Hire date must be in the past or today")
    private LocalDate hireDate;

    @NotBlank(message = "Employment status should not be empty")
    private String employmentStatus;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Contract Type must not be null")
    private ContractType contractType;

    @Embedded
    @Valid
    private ContactInfo contactInfo;

    @NotBlank(message = "Address should not be empty")
    private String address;

    @CurrentTimestamp(event = INSERT)
    public Instant createdAt;

    @CurrentTimestamp(event = {INSERT, UPDATE})
    public Instant lastUpdatedAt;


}

