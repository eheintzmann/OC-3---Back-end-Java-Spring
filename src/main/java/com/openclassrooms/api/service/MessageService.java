package com.openclassrooms.api.service;

import com.openclassrooms.api.model.entity.Message;
import com.openclassrooms.api.model.entity.Rental;
import com.openclassrooms.api.model.entity.User;
import com.openclassrooms.api.repository.MessageRepository;
import com.openclassrooms.api.repository.RentalRepository;
import com.openclassrooms.api.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Message service
 */
@Slf4j
@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final RentalRepository rentalRepository;

    /**
     * Constructor for MessageService class
     *
     * @param messageRepository MessageRepository
     * @param userRepository UserRepository
     * @param rentalRepository RentalRepository
     */
    public MessageService(MessageRepository messageRepository, UserRepository userRepository, RentalRepository rentalRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.rentalRepository = rentalRepository;
    }

    /**
     * Send message to a user
     *
     * @param messageBody String
     * @param userId recipient iD
     * @param rentalId associated rental
     * @return boolean
     */
    public boolean sendMessage(String messageBody, int userId, int rentalId) {

        Optional<User> optUser = userRepository.findById(userId);

        if (optUser.isEmpty()) {
            log.error("User " + userId + " not found !");
            return false;
        }
        Optional<Rental> optRental = rentalRepository.findById(rentalId);

        if (optRental.isEmpty()) {
            log.error("Rental " + rentalId + " not found !");
            return false;
        }
        Message message = Message.builder()
                .message(messageBody)
                .user(optUser.get())
                .rental(optRental.get())
                .build();
        messageRepository.save(message);

        return true;
    }
}
