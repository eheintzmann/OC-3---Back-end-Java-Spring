package com.openclassrooms.api.service;

import com.openclassrooms.api.model.entity.User;
import com.openclassrooms.api.repository.UserRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Data
@Service
public class AuthentificationService {

    private UserRepository userRepository;

    @Autowired
    AuthentificationService(UserRepository userRepository) {
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
                .password(password)
                .build();
        userRepository.saveAndFlush(user);

        // Return token
        return Optional.of(email);
    }

    public Optional<String> loginUser(String email, String password) {

        // Try to Retrieve user
        Optional<User> optUser = userRepository.findByEmail(email);

        if (optUser.isEmpty()) {
            // If user doesn't exist, do not return token
            return Optional.empty();
        }

        // If user exists, verify credentials
        User user = optUser.get();
        if ((user.getEmail().equals(email)) && (user.getPassword().equals(password))) {
            // if credentials are valid, return token
            return Optional.of(email);
        }

        // if credentials are not valid, do not return token
        return Optional.empty();
    }

    public Optional<User> authUser(String token) {
        return this.userRepository.findByEmail(token);
    }

}
