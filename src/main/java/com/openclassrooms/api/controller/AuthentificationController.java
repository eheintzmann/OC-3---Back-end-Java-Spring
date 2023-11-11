package com.openclassrooms.api.controller;

import com.openclassrooms.api.exception.BadRequestException;
import com.openclassrooms.api.exception.InvalidCredentialsException;
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
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@Slf4j
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

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = TokenResponse.class)
            )),
            @ApiResponse(responseCode = "400", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = EmptyResponse.class)
            ))
    })
    @PostMapping(
            path = "register",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public TokenResponse register(@RequestBody(required = false) Optional<RegisterRequest> optRequest)
            throws  BadRequestException {

        if (optRequest.isEmpty()) {
            throw new BadRequestException();
        }
        RegisterRequest request = optRequest.get();

        if (!validator.validate(request).isEmpty()) {
            throw new BadRequestException();
        }
        Optional<String> optToken;
        optToken = this.authentificationService.registerUser(
                request.getEmail(),
                request.getName(),
                request.getPassword()
        );
        return optToken.map(TokenResponse::new)
                .orElseThrow(BadRequestException::new);
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

    public TokenResponse login(@RequestBody(required = false) Optional<LoginRequest> optRequest)
            throws InvalidCredentialsException {

        if (optRequest.isEmpty()) {
            throw new InvalidCredentialsException(ERROR_MESSAGE);
        }
        LoginRequest request = optRequest.get();

        if (!validator.validate(request).isEmpty()) {
            throw new InvalidCredentialsException(ERROR_MESSAGE);
        }
        Optional<String> optToken = this.authentificationService.loginUser(
                request.getLogin(),
                request.getPassword()
        );
        return optToken.map(TokenResponse::new)
                .orElseThrow(() -> new InvalidCredentialsException(ERROR_MESSAGE));
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
    public AuthMeResponse autMe(Principal principal) throws InvalidCredentialsException {

        if (principal == null) {
            log.error("Principal is null !");
            throw new InvalidCredentialsException();
        }
        Optional<User> optUser = authentificationService.authUser(principal.getName());

        return optUser.map(user -> conversionService.convert(user, AuthMeResponse.class))
                .orElseThrow(InvalidCredentialsException::new);
    }

}
