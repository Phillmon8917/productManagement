package com.store.security;

import com.store.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class UserPrincipal implements UserDetails {

    private final User user;

    /**
     * Returns the database id of the authenticated user.
     */
    public Long getId() {
        return user.getId();
    }

    /**
     * Exposes the wrapped user entity for application services.
     */
    public User getUser() {
        return user;
    }

    /**
     * Converts the user's role into a Spring Security authority.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(user.getRole().name()));
    }

    /**
     * Supplies the encoded password used during authentication.
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * Supplies the username used during authentication.
     */
    @Override
    public String getUsername() {
        return user.getUsername();
    }

    /**
     * Allows authentication to proceed because accounts do not expire.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Allows authentication to proceed because accounts are not lockable here.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Allows authentication to proceed because credentials do not expire here.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Reports whether the wrapped user account is enabled.
     */
    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }
}
