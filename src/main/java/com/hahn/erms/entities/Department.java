package com.hahn.erms.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CurrentTimestamp;
import org.hibernate.generator.EventType;

import java.time.Instant;

import static org.hibernate.generator.EventType.INSERT;
import static org.hibernate.generator.EventType.UPDATE;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Department name should not be empty")
    @Column(unique = true, nullable = false)
    private String name;

    @CurrentTimestamp(event = INSERT)
    public Instant createdAt;

    @CurrentTimestamp(event = {INSERT, UPDATE})
    public Instant lastUpdatedAt;


}
