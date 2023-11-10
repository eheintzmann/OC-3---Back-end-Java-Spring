package com.openclassrooms.api.controller;

import com.openclassrooms.api.model.request.auth.LoginRequest;
import com.openclassrooms.api.model.response.*;
import com.openclassrooms.api.model.request.auth.RegisterRequest;
import com.openclassrooms.api.model.response.auth.AuthMeResponse;
import com.openclassrooms.api.model.entity.User;
import com.openclassrooms.api.service.AuthentificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@Tag( name =  "auth", description = "Authentification operations" )
@RestController
@RequestMapping("/api/auth/")
public class AuthentificationController {
    private static final String ERROR_MESSAGE = "Invalid credentials";
    private final AuthentificationService authentificationService;
    private final ConversionService conversionService;
    private final Validator validator;

    public AuthentificationController(
            AuthentificationService authentificationService,
            ConversionService conversionService
    ) {
        this.authentificationService = authentificationService;
        this.conversionService = conversionService;
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    @Operation(summary = "register", description = "Sign up")
    @ApiResponse( responseCode = "200", content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = TokenResponse.class)
    ))
    @ApiResponse( responseCode = "400", content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = EmptyResponse.class)
    ))
    @PostMapping(
            path = "register",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Response> register(@RequestBody(required = false) Optional<RegisterRequest> optRequest) {

        if (optRequest.isEmpty()) {
            return  new ResponseEntity<>(new EmptyResponse(), HttpStatus.BAD_REQUEST);
        }
        RegisterRequest request = optRequest.get();

        if (!validator.validate(request).isEmpty()) {
            return new ResponseEntity<>(new EmptyResponse(), HttpStatus.BAD_REQUEST);
        }

        Optional<String> optToken;
        try {
            optToken = this.authentificationService.registerUser(
                    request.getEmail(),
                    request.getName(),
                    request.getPassword()
            );
        } catch (BadCredentialsException ex) {

            return new ResponseEntity<>(new EmptyResponse(), HttpStatus.BAD_REQUEST);
        }
        return optToken.<ResponseEntity<Response>>map(token -> ResponseEntity
                .ok(new TokenResponse(token))).orElseGet(() -> new ResponseEntity<>(new EmptyResponse(), HttpStatus.BAD_REQUEST)
        );
    }

    @Operation(summary = "login", description = "Sign in")
    @ApiResponse( responseCode = "200", content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = TokenResponse.class)
    ))
    @ApiResponse( responseCode = "401", content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = MessageResponse.class)
    ))
    @PostMapping(
            path = "login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )

    public ResponseEntity<Response> login(@RequestBody(required = false) Optional<LoginRequest> optRequest) {

        if (optRequest.isEmpty()) {
            return  new ResponseEntity<>(new MessageResponse(ERROR_MESSAGE), HttpStatus.UNAUTHORIZED);
        }
        LoginRequest request = optRequest.get();

        if (!validator.validate(request).isEmpty()) {
            return new ResponseEntity<>(new MessageResponse(ERROR_MESSAGE), HttpStatus.UNAUTHORIZED);
        }
        Optional<String> optToken;
        try {
            optToken = this.authentificationService.loginUser(
                    request.getLogin(),
                    request.getPassword()
            );
        } catch (BadCredentialsException ex) {

            return new ResponseEntity<>(new MessageResponse(ERROR_MESSAGE), HttpStatus.UNAUTHORIZED);
        }
        return optToken.<ResponseEntity<Response>>map(token -> ResponseEntity
                .ok(new TokenResponse(token))).orElseGet(
                        () -> new ResponseEntity<>(new MessageResponse(ERROR_MESSAGE), HttpStatus.UNAUTHORIZED)
        );
    }

    @Operation(summary = "me", description = "Who am I")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponse( responseCode = "200", content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = AuthMeResponse.class)
    ))
    @ApiResponse( responseCode = "401", content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = EmptyResponse.class)
    ))
    @GetMapping(
            path = "me",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Response> autMe(Principal principalUser) {

        if (principalUser == null) {
            return new ResponseEntity<>(new EmptyResponse(), HttpStatus.UNAUTHORIZED);
        }
        Optional<User> optUser = authentificationService.authUser(principalUser.getName());

        return optUser.<ResponseEntity<Response>>map(user -> ResponseEntity
                .ok(conversionService.convert(user, AuthMeResponse.class)))
                .orElseGet(() -> new ResponseEntity<>(new EmptyResponse(), HttpStatus.UNAUTHORIZED));
    }

}
