package com.hahn.erms.services;

import com.hahn.erms.entities.Account;

import java.util.List;
import java.util.Optional;

public interface AccountService {
    List<Account> getAllAccounts();
    Optional<Account> getAccountById(Long id);
    Account createAccount(Account Account);
    Account updateAccount(Long id, Account AccountDetails);
    void deleteAccount(Long id);
    Optional<Account> getAccountByUsername(String username);
}
