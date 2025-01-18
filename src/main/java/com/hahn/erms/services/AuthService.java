package com.hahn.erms.services;

import com.hahn.erms.dtos.LoginResponse;

public interface AuthService {
    LoginResponse login(String username, String password);
}
