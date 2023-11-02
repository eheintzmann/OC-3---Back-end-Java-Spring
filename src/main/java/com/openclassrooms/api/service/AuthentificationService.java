package com.openclassrooms.api.service;

import com.openclassrooms.api.configuration.jwt.JwtService;
import com.openclassrooms.api.model.entity.User;
import com.openclassrooms.api.repository.UserRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Data
@Service
public class AuthentificationService {

    private AuthenticationManager authManager;
    private JwtService jwtService;
    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private UserDetailsService userDetailsService;

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

    public Optional<String> registerUser(String email, String name, String password) {

        // Try to Retrieve user
        // TODO: do not retrieve user, just verify existence
        Optional<User> optUser = userRepository.findByEmail(email);
        if (optUser.isPresent()) {
            // If user exists, stop registration, do not return a token
            return Optional.empty();
        }
        // If user doesn't exist, register it

        // Create a new one
        User user = User.builder()
                .name(name)
                .email(email)
                .password(passwordEncoder.encode(password))
                .build();
        userRepository.saveAndFlush(user);

        // Return token
        return Optional.of(jwtService.generateAccessToken(user));
    }

    public Optional<String> loginUser(String email, String password) {

         Authentication authentication = authManager.authenticate(

                 new UsernamePasswordAuthenticationToken(email, password)
         );

         User user = (User) authentication.getPrincipal();

        return Optional.of(jwtService.generateAccessToken(user));
    }

    public Optional<User> authUser(String username) {

        return this.userRepository.findByEmail(username);
    }

}
