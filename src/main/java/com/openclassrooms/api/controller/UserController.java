package com.openclassrooms.api.controller;

import com.openclassrooms.api.model.entity.User;
import com.openclassrooms.api.model.response.Response;
import com.openclassrooms.api.model.response.user.UserResponse;
import com.openclassrooms.api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.Optional;

@Tag( name = "user", description = "Users operations" )
@RestController
@RequestMapping("/api/users/")
public class UserController {

    private final UserService userService;
    private final ConversionService conversionService;

    public UserController(UserService userService, ConversionService conversionService) {
        this.userService = userService;
        this.conversionService = conversionService;
    }

    @Operation(summary = "get", description = "Get user by id")
    @GetMapping("{id}")
    public Response getUser(@PathVariable int id) throws AccessDeniedException {
        Optional<User> optUser = userService.getUser(id);

        return optUser.<Response>map(user ->
                conversionService.convert(user, UserResponse.class))
                .orElseThrow(() -> new AccessDeniedException(""));
    }
}
