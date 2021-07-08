package com.mss.searchengine.dto;

import com.mss.searchengine.model.Cataloger;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

// implements UserDetails interface of spring security core

public class MyCatalogerDetails implements UserDetails {

    private final String userName;
    private final String password;

    public MyCatalogerDetails(Cataloger cataloger) {
        this.password = cataloger.getPassword();
        this.userName = cataloger.getEmail();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }


    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
