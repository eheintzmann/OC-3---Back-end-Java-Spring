package com.openclassrooms.api.controller;

import com.openclassrooms.api.exception.InvalidCredentialsException;
import com.openclassrooms.api.model.response.EmptyResponse;
import com.openclassrooms.api.model.response.user.UserResponse;
import com.openclassrooms.api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


@Tag( name = "user", description = "Users operations" )
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final ConversionService conversionService;

    public UserController(UserService userService, ConversionService conversionService) {
        this.userService = userService;
        this.conversionService = conversionService;
    }

    @Operation(summary = "get", description = "Get user by id")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponse( responseCode = "200", content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = UserResponse.class)
    ))
    @ApiResponse( responseCode = "401", content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = EmptyResponse.class)
    ))
    @GetMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public UserResponse getUser(@PathVariable int id) throws InvalidCredentialsException {

        return userService.getUser(id).map(user -> conversionService.convert(user, UserResponse.class))
                .orElseThrow(InvalidCredentialsException::new);
    }
}
