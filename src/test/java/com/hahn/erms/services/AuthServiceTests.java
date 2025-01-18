package com.hahn.erms.services;

import com.hahn.erms.dtos.LoginResponse;
import com.hahn.erms.entities.Account;
import com.hahn.erms.entities.Permission;
import com.hahn.erms.entities.Role;
import com.hahn.erms.models.UserDetailsModel;
import com.hahn.erms.services.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTests {
    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserDetailsService userDetailsService;

    @InjectMocks
    private AuthServiceImpl authService;
    private Account account;
    private String username;
    private String password;

    @BeforeEach
    void setUp(){
        username = "johndoe";
        password = "password123";
        Role role = Role.builder().name("ROLE_HR").permissions(Set.of(new Permission().withName("CREATE"),
                new Permission().withName("UPDATE"),
                new Permission().withName("DELETE"),
                new Permission().withName("READ"))).build();
        account = Account.builder().username(username).role(role).password(password).build();
    }

    @Test
    void login_WhenCredentialsAreValid_ShouldReturnLoginResponse() {

        UserDetailsModel userDetailsModel = new UserDetailsModel(account);

        Authentication mockAuthentication = mock(Authentication.class);
        when(mockAuthentication.getPrincipal()).thenReturn(userDetailsModel);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mockAuthentication);
        when(jwtService.generateToken(any(UserDetails.class)))
                .thenReturn("valid.jwt.token");
        when(jwtService.generateRefreshToken(any(UserDetails.class)))
                .thenReturn("valid.refresh.token");

        LoginResponse response = authService.login(username, password);

        assertNotNull(response);
        assertEquals("valid.jwt.token", response.token());
        assertEquals("valid.refresh.token", response.refreshToken());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService).generateToken(any(UserDetails.class));
        verify(jwtService).generateRefreshToken(any(UserDetails.class));
    }

    @Test
    void login_WhenCredentialsAreInvalid_ShouldThrowAuthenticationException() {
        String username = "johndoe";
        String password = "wrongpassword";

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        assertThrows(AuthenticationException.class, () -> {
            authService.login(username, password);
        });

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService, never()).generateToken(any(UserDetails.class));
        verify(jwtService, never()).generateRefreshToken(any(UserDetails.class));
    }

    @Test
    void login_WhenUsernameIsNull_ShouldThrowIllegalArgumentException() {
        String password = "password123";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authService.login(null, password);
        });

        assertEquals("Username and password must not be null", exception.getMessage());
        verify(authenticationManager, never()).authenticate(any());
    }

    @Test
    void login_WhenPasswordIsNull_ShouldThrowIllegalArgumentException() {
        String username = "johndoe";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authService.login(username, null);
        });

        assertEquals("Username and password must not be null", exception.getMessage());
        verify(authenticationManager, never()).authenticate(any());
    }



    @Test
    void login_WhenJwtServiceFails_ShouldThrowRuntimeException() {

        UserDetailsModel userDetailsModel = new UserDetailsModel(account);

        Authentication mockAuthentication = mock(Authentication.class);
        when(mockAuthentication.getPrincipal()).thenReturn(userDetailsModel);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mockAuthentication);
        when(jwtService.generateToken(any(UserDetails.class)))
                .thenThrow(new RuntimeException("JWT generation failed"));

        assertThrows(RuntimeException.class, () -> {
            authService.login(username, password);
        });

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService).generateToken(any(UserDetails.class));
        verify(jwtService, never()).generateRefreshToken(any(UserDetails.class));
    }
}