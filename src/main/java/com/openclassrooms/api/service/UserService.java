package com.openclassrooms.api.service;

import com.openclassrooms.api.model.entity.User;
import com.openclassrooms.api.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UserService {
    private final UserRepository userRepository;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public Optional<User> getUser(int id) {
        return userRepository.findById(id);
    }
}
