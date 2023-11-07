package com.openclassrooms.api.controller;

import com.openclassrooms.api.model.request.message.MessageRequest;
import com.openclassrooms.api.model.response.MessageResponse;
import com.openclassrooms.api.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.AccessDeniedException;

@Slf4j
@RestController
@RequestMapping("/api/messages/")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping(
            path = "",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public MessageResponse post(@RequestBody MessageRequest request) throws AccessDeniedException {

        messageService.sendMessage(request.getMessage(), request.getUserId(), request.getRentalId());

        return new MessageResponse("Message sent with success");
    }

}

