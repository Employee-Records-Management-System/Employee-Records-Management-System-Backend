package com.hahn.erms.models;

import com.hahn.erms.entities.Account;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class UserDetailsModel implements UserDetails {

    private final Account account;
    private final List<GrantedAuthority> authorities;

    public UserDetailsModel(Account account){
        this.account = account;
        this.authorities = account.getRole().getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getName()))
                .collect(Collectors.toList());
        this.authorities.add(new SimpleGrantedAuthority(account.getRole().getName()));
    }


    public String getPassword() {
        return this.account.getPassword();
    }

    public String getUsername() {
        return this.account.getUsername();
    }

    public boolean isAccountNonExpired() { return true; }

    public boolean isAccountNonLocked() { return true; }

    public boolean isCredentialsNonExpired() { return true; }

    public boolean isEnabled() { return true; }

}
