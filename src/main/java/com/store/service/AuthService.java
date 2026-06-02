package com.store.service;

import com.store.dto.AuthDtos;
import com.store.entity.Role;
import com.store.entity.User;
import com.store.repository.UserRepository;
import com.store.security.JwtTokenProvider;
import com.store.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Authenticates submitted credentials and returns a signed JWT response.
     */
    public AuthDtos.AuthResponse login(AuthDtos.LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        String token = jwtTokenProvider.generateToken(principal);

        return new AuthDtos.AuthResponse(token, principal.getUsername(), principal.getUser().getRole().name());
    }

    /**
     * Creates a standard user account after checking username and email uniqueness.
     */
    @Transactional
    public String register(AuthDtos.RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("That username is already taken. Please choose another.");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("An account with that email address already exists.");
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .role(Role.ROLE_USER)
                .enabled(true)
                .build();

        userRepository.save(user);
        return "Account created successfully! You can now log in.";
    }
}
