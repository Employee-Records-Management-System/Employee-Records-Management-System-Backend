package com.hahn.erms.services.impl;

import com.hahn.erms.dtos.LoginResponse;
import com.hahn.erms.models.UserDetailsModel;
import com.hahn.erms.services.AuthService;
import com.hahn.erms.services.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


@Service
public class AuthServiceImpl implements AuthService {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    public AuthServiceImpl(
            JwtService jwtService, AuthenticationManager authenticationManager) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public LoginResponse login(String username, String password) {
        if(username == null || password == null) { throw new IllegalArgumentException("Username and password must not be null"); }
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);

        Authentication authenticated = authenticationManager.authenticate(authentication);

        UserDetailsModel userDetails = (UserDetailsModel) authenticated.getPrincipal();

        String token = jwtService.generateToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        return new LoginResponse(token, refreshToken, userDetails.getAccount());
    }

}
