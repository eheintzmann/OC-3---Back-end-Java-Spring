package com.openclassrooms.api.service;

import com.openclassrooms.api.model.entity.User;
import com.openclassrooms.api.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;


/**
 * User service
 */
@Service
public class UserService {
    private final UserRepository userRepository;


    /**
     * Constructor for UserService class
     * @param userRepository UserRepository
     */
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Retrieve user by ID
     *
     * @param id User ID
     * @return Optional User
     */
    public Optional<User> getUser(int id) {
        return userRepository.findById(id);
    }
}
