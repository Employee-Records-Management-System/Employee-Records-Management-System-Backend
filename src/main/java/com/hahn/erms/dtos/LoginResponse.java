package com.hahn.erms.dtos;


import com.hahn.erms.entities.Account;

public record LoginResponse(String token, String refreshToken, Account account) {

}