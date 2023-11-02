package com.openclassrooms.api.controller;

import com.openclassrooms.api.model.entity.User;
import com.openclassrooms.api.model.response.EmptyResponse;
import com.openclassrooms.api.model.response.Response;
import com.openclassrooms.api.model.response.user.UserResponse;
import com.openclassrooms.api.service.UserService;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users/")
public class UserController {

    private final UserService userService;
    private final ConversionService conversionService;

    public UserController(UserService userService, ConversionService conversionService) {
        this.userService = userService;
        this.conversionService = conversionService;
    }

    @GetMapping("{id}")
    public ResponseEntity<Response> getUser(@PathVariable int id) {
        Optional<User> optUser = userService.getUser(id);

        return optUser.<ResponseEntity<Response>>map(user ->
                ResponseEntity.ok(conversionService.convert(user, UserResponse.class)))
                .orElseGet(() -> ResponseEntity.ok(new EmptyResponse()));
    }
}
