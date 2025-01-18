package com.hahn.erms.services.impl;

import com.hahn.erms.entities.Account;
import com.hahn.erms.repositories.AccountRepository;
import com.hahn.erms.services.AccountService;
import com.hahn.erms.utils.EntityUtils;
import com.hahn.erms.utils.ValidationUtils;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public AccountServiceImpl(AccountRepository AccountRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = AccountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Optional<Account> getAccountById(Long id) {
        return accountRepository.findById(id);
    }

    public Optional<Account> getAccountByUsername(String username) {
        return accountRepository.findByUsername(username);
    }

    public Account createAccount(Account account) {
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        return accountRepository.save(account);
    }

    public Account updateAccount(Long id, Account accountDetails) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(String.format("Account with %d not found",id)));
        EntityUtils.copyNonNullProperties(accountDetails, account);
        ValidationUtils.validate(account);
        if (accountDetails.getPassword() != null && !accountDetails.getPassword().isEmpty()) {
            account.setPassword(passwordEncoder.encode(accountDetails.getPassword()));
        }
        return accountRepository.save(account);
    }

    public void deleteAccount(Long id) {
        accountRepository.deleteById(id);
    }
}

