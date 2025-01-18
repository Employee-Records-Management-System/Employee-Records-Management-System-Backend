package com.hahn.erms.dtos;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(@NotBlank(message = "Username should not be empty") String username, @NotBlank(message = "Password should not be empty") String password) {
}
