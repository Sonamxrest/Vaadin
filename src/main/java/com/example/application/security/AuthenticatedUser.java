package com.example.application.security;

import com.example.application.data.dto.Customer;
import com.vaadin.flow.spring.security.AuthenticationContext;
import java.util.Optional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class AuthenticatedUser {

    private final UserDetailsService service;
    private final AuthenticationContext authenticationContext;

    public AuthenticatedUser(AuthenticationContext authenticationContext, UserDetailsService service) {
        this.service = service;
        this.authenticationContext = authenticationContext;
    }

    public Optional<UserDetails> get() {
        return authenticationContext.getAuthenticatedUser(UserDetails.class)
                .map(userDetails -> service.loadUserByUsername(userDetails.getUsername()));
    }

    public void logout() {
        authenticationContext.logout();
    }

}
