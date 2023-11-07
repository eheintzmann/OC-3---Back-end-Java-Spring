package com.openclassrooms.api.service;

import com.openclassrooms.api.model.entity.Message;
import com.openclassrooms.api.model.entity.Rental;
import com.openclassrooms.api.model.entity.User;
import com.openclassrooms.api.repository.MessageRepository;
import com.openclassrooms.api.repository.RentalRepository;
import com.openclassrooms.api.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.Optional;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final RentalRepository rentalRepository;

    public MessageService(MessageRepository messageRepository, UserRepository userRepository, RentalRepository rentalRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.rentalRepository = rentalRepository;
    }

    public void sendMessage(String messageBody, int userId, int rentalId) throws AccessDeniedException {

        Optional<User> optUser = userRepository.findById(userId);

        if (optUser.isEmpty()) {
            throw new AccessDeniedException("");
        }

        Optional<Rental> optRental = rentalRepository.findById(rentalId);

        if (optRental.isEmpty()) {
            throw new AccessDeniedException("");
        }

        Message message = Message.builder()
                .message(messageBody)
                .user(optUser.get())
                .rental(optRental.get())
                .build();

        messageRepository.save(message);
    }
}
