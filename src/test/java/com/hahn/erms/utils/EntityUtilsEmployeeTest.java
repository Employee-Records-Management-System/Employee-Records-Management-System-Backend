package com.hahn.erms.utils;


import com.hahn.erms.entities.ContactInfo;
import com.hahn.erms.entities.Department;
import com.hahn.erms.entities.Employee;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;


class EntityUtilsEmployeeTest {

    @Test
    void shouldCopyNonNullPropertiesForEmployee() {
        Department department1 = new Department();
        department1.setId(1L);
        department1.setName("Finance");

        Department department2 = new Department();
        department2.setId(2L);
        department2.setName("HR");

        ContactInfo contactInfo1 = new ContactInfo();
        contactInfo1.setPhoneNumber("+1234567890");
        contactInfo1.setEmail("old.email@example.com");

        ContactInfo contactInfo2 = new ContactInfo();
        contactInfo2.setPhoneNumber("+0987654321");

        Employee source = new Employee();
        source.setFullName("John Doe");
        source.setJobTitle("Senior Analyst");
        source.setDepartment(department2);
        source.setHireDate(LocalDate.of(2020, 5, 10));
        source.setContactInfo(contactInfo2);
        source.setEmploymentStatus(null); // Null field to test

        Employee target = new Employee();
        target.setFullName("Jane Smith");
        target.setJobTitle("Analyst");
        target.setDepartment(department1);
        target.setHireDate(LocalDate.of(2018, 3, 15));
        target.setContactInfo(contactInfo1);
        target.setEmploymentStatus("Active");

        // When
        EntityUtils.copyNonNullProperties(source, target);

        // Then
        assertEquals("John Doe", target.getFullName());
        assertEquals("Senior Analyst", target.getJobTitle());
        assertEquals("HR", target.getDepartment().getName());
        assertEquals(LocalDate.of(2020, 5, 10), target.getHireDate());
        assertEquals("+0987654321", target.getContactInfo().getPhoneNumber());
        assertEquals("old.email@example.com", target.getContactInfo().getEmail()); // Unchanged
        assertEquals("Active", target.getEmploymentStatus()); // Unchanged
    }

    @Test
    void shouldThrowExceptionWhenSourceOrTargetIsNull() {
        // Given
        Employee source = null;
        Employee target = new Employee();

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> EntityUtils.copyNonNullProperties(source, target)
        );
        assertEquals("Source and target must not be null", exception.getMessage());
    }

    @Test
    void shouldIgnoreNullFieldsFromSourceForEmployee() {
        // Given
        Employee source = new Employee();
        source.setFullName(null);
        source.setJobTitle("Updated Job Title");

        Employee target = new Employee();
        target.setFullName("Initial Name");
        target.setJobTitle("Initial Job Title");

        EntityUtils.copyNonNullProperties(source, target);

        assertEquals("Initial Name", target.getFullName()); // Unchanged
        assertEquals("Updated Job Title", target.getJobTitle());
    }
}

