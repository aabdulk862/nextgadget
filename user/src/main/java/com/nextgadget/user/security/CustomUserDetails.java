package com.nextgadget.user.security;

import com.nextgadget.user.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Return user roles/authorities if you have them; empty list for now
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;  // Customize if needed
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;  // Customize if needed
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;  // Customize if needed
    }

    @Override
    public boolean isEnabled() {
        return true;  // Customize if needed
    }

    // Optionally expose the User entity
    public User getUser() {
        return user;
    }
}
