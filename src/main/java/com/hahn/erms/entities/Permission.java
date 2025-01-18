package com.hahn.erms.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.GenerationType;
import lombok.*;
import org.hibernate.annotations.CurrentTimestamp;

import java.time.Instant;
import java.util.Set;

import static org.hibernate.generator.EventType.INSERT;
import static org.hibernate.generator.EventType.UPDATE;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@With
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @CurrentTimestamp(event = INSERT)
    public Instant createdAt;
    @CurrentTimestamp(event = {INSERT, UPDATE})
    public Instant lastUpdatedAt;


}