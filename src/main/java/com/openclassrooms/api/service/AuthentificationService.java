package com.openclassrooms.api.service;

import com.openclassrooms.api.configuration.jwt.JwtService;
import com.openclassrooms.api.model.entity.User;
import com.openclassrooms.api.repository.UserRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Authentification service
 */
@Slf4j
@Data
@Service
public class AuthentificationService {

    private AuthenticationManager authManager;
    private JwtService jwtService;
    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private UserDetailsService userDetailsService;

    /**
     * Constructor for AuthentificationService class
     *
     * @param authManager AuthenticationManager
     * @param jwtService JwtService
     * @param passwordEncoder PasswordEncoder
     * @param userRepository UserRepository
     */
    @Autowired
    AuthentificationService(
            AuthenticationManager authManager,
            JwtService jwtService,
            PasswordEncoder passwordEncoder,
            UserRepository userRepository
    ) {
        this.authManager = authManager;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    /**
     * Register a new user
     *
     * @param email user email
     * @param name username
     * @param password user password
     * @return Optional token
     */
    public Optional<String> registerUser(String email, String name, String password) {

        // If user exists, stop registration, do not return a token
        if (userRepository.existsByEmail(email)) {
            return Optional.empty();
        }

        // If user doesn't exist, register it

        // Create new user
        User user = User.builder()
                .name(name)
                .email(email)
                .password(passwordEncoder.encode(password))
                .build();

        // Save user in db
        userRepository.saveAndFlush(user);

        // Return token
        return Optional.of(jwtService.generateAccessToken(user));
    }

    /**
     * Log in an existing user
     *
     * @param email user email
     * @param password user password
     * @return Optional token
     */
    public Optional<String> loginUser(String email, String password) {

        Authentication authentication;
        try {
            authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );
        } catch (AuthenticationException ex) {
            log.error(ex.getMessage());
            return Optional.empty();
        }

        User user = (User) authentication.getPrincipal();

        return Optional.of(jwtService.generateAccessToken(user));
    }

    /**
     * Return details of a given user
     *
     * @param email user email
     * @return Optional User
     */
    public Optional<User> authUser(String email) {

        return this.userRepository.findByEmail(email);
    }

}
