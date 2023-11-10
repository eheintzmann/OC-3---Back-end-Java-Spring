package com.openclassrooms.api.controller;

import com.openclassrooms.api.model.request.message.MessageRequest;
import com.openclassrooms.api.model.response.EmptyResponse;
import com.openclassrooms.api.model.response.MessageResponse;
import com.openclassrooms.api.model.response.TokenResponse;
import com.openclassrooms.api.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.AccessDeniedException;

@Tag( name = "message", description = "Messages operations" )
@RestController
@RequestMapping("/api/messages/")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @Operation(summary = "message", description = "Send new message")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponse( responseCode = "200", content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = TokenResponse.class)
    ))
    @ApiResponse( responseCode = "400", content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = EmptyResponse.class )
    ))
    @ApiResponse( responseCode = "401", content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = MessageResponse.class)
    ))
    @PostMapping(
            path = "",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public MessageResponse post(@Valid @RequestBody MessageRequest request) throws AccessDeniedException {

        messageService.sendMessage(request.getMessage(), request.getUserId(), request.getRentalId());

        return new MessageResponse("Message sent with success");
    }

}

