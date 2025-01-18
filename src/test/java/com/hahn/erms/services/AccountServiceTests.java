package com.hahn.erms.services;

import com.hahn.erms.entities.*;
import com.hahn.erms.repositories.AccountRepository;
import com.hahn.erms.services.impl.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTests {

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private AccountServiceImpl accountService;

    private Account testAccount;
    private Employee testEmployee;
    private Role testRole;

    @BeforeEach
    void setUp() {
        testEmployee = Employee.builder()
                .id(1L)
                .fullName("John Doe")
                .employeeId("EMP001")
                .build();

        testRole = Role.builder()
                .id(1L)
                .name("USER")
                .build();

        testAccount = Account.builder()
                .id(1L)
                .username("johndoe")
                .password("password123")
                .employee(testEmployee)
                .role(testRole)
                .build();
    }

    @Test
    void getAllAccounts_ShouldReturnListOfAccounts() {
        when(accountRepository.findAll()).thenReturn(Arrays.asList(testAccount));

        List<Account> accounts = accountService.getAllAccounts();

        assertNotNull(accounts);
        assertEquals(1, accounts.size());
        verify(accountRepository, times(1)).findAll();
    }

    @Test
    void getAccountById_WhenAccountExists_ShouldReturnAccount() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(testAccount));

        Optional<Account> result = accountService.getAccountById(1L);

        assertTrue(result.isPresent());
        assertEquals(testAccount.getUsername(), result.get().getUsername());
        verify(accountRepository, times(1)).findById(1L);
    }

    @Test
    void createAccount_ShouldReturnCreatedAccount() {
        when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");
        when(accountRepository.save(any(Account.class))).thenReturn(testAccount);

        Account created = accountService.createAccount(testAccount);

        assertNotNull(created);
        assertEquals(testAccount.getUsername(), created.getUsername());
        verify(passwordEncoder, times(1)).encode(any(String.class));
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void updateAccount_WhenAccountExists_ShouldReturnUpdatedAccount() {
        Account updatedAccount = Account.builder()
                .id(1L)
                .username("johndoe_updated")
                .password("newpassword")
                .employee(testEmployee)
                .role(testRole)
                .build();

        when(accountRepository.findById(1L)).thenReturn(Optional.of(testAccount));
        when(accountRepository.save(any(Account.class))).thenReturn(updatedAccount);

        Account result = accountService.updateAccount(1L, updatedAccount);

        assertNotNull(result);
        assertEquals("johndoe_updated", result.getUsername());
        verify(accountRepository, times(1)).findById(1L);
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void deleteAccount_ShouldCallRepositoryDelete() {
        Long accountId = 1L;

        doNothing().when(accountRepository).deleteById(accountId);

        accountService.deleteAccount(accountId);

        verify(accountRepository).deleteById(accountId);
    }
}


