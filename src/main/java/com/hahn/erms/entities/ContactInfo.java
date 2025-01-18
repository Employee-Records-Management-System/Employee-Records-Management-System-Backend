package com.hahn.erms.entities;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactInfo {

    @Column(unique = true)
    @Pattern(regexp = "^(\\+\\d{1,3}\\s?)?\\d{10}$", message = "Invalid phone number format")
    private String phoneNumber;

    @Column(unique = true)
    @Email(message = "Invalid email format")
    private String email;

}

