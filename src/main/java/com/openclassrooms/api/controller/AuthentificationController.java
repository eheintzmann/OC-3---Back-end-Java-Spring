package com.openclassrooms.api.controller;

import com.openclassrooms.api.model.request.auth.LoginRequest;
import com.openclassrooms.api.model.response.*;
import com.openclassrooms.api.model.request.auth.RegisterRequest;
import com.openclassrooms.api.model.response.auth.AuthMeResponse;
import com.openclassrooms.api.model.entity.User;
import com.openclassrooms.api.service.AuthentificationService;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth/")
public class AuthentificationController {
    private static final String ERROR_MESSAGE = "Invalid credentials";

    private final AuthentificationService authentificationService;

    private final ConversionService conversionService;

    private final Validator validator;

    @Autowired
    public AuthentificationController(
            AuthentificationService authentificationService,
            ConversionService conversionService
    ) {
        this.authentificationService = authentificationService;
        this.conversionService = conversionService;
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

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

        Optional<String> optToken = this.authentificationService.registerUser(
                request.getEmail(),
                request.getName(),
                request.getPassword()
        );

        return optToken.<ResponseEntity<Response>>map(token -> ResponseEntity
                .ok(new TokenResponse(token))).orElseGet(() -> new ResponseEntity<>(new EmptyResponse(), HttpStatus.BAD_REQUEST)
        );
    }

    @PostMapping(
            path = "login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )

    public ResponseEntity<Response> login(@RequestBody(required = false) Optional<LoginRequest> optRequest, BindingResult result)  {
        
        if (optRequest.isEmpty()) {
            return  new ResponseEntity<>(new MessageResponse(ERROR_MESSAGE), HttpStatus.UNAUTHORIZED);
        }
        LoginRequest request = optRequest.get();

        if (!validator.validate(request).isEmpty()) {
            return new ResponseEntity<>(new MessageResponse(ERROR_MESSAGE), HttpStatus.UNAUTHORIZED);
        }
        Optional<String> optToken = this.authentificationService.loginUser(
                request.getLogin(),
                request.getPassword()
        );
        return optToken.<ResponseEntity<Response>>map(token -> ResponseEntity
                .ok(new TokenResponse(token))).orElseGet(
                        () -> new ResponseEntity<>(new MessageResponse(ERROR_MESSAGE), HttpStatus.UNAUTHORIZED)
        );
    }

    @GetMapping("me")
    public ResponseEntity<Response> autMe(@RequestHeader(name = "Authorization", required = false) Optional<String> optAuthorizationHeader) {

        if (optAuthorizationHeader.isEmpty()) {
            return new ResponseEntity<>(new EmptyResponse(), HttpStatus.UNAUTHORIZED);
        }

        Optional<User> optUser = authentificationService.authUser(
                optAuthorizationHeader.get()
                        .replace("Bearer", "")
                        .trim()
        );
        return optUser.<ResponseEntity<Response>>map(user -> ResponseEntity
                .ok(conversionService.convert(user, AuthMeResponse.class))).orElseGet(
                () -> new ResponseEntity<>(new EmptyResponse(), HttpStatus.UNAUTHORIZED)
        );
    }

}
