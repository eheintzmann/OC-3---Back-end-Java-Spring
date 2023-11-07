package com.openclassrooms.api.service;

import com.openclassrooms.api.model.entity.Rental;
import com.openclassrooms.api.model.entity.User;
import com.openclassrooms.api.repository.RentalRepository;
import com.openclassrooms.api.repository.UserRepository;
import com.openclassrooms.api.service.storage.StorageService;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.AccessDeniedException;
import java.util.Optional;

@Slf4j
@Service
public class RentalService {

    private final RentalRepository rentalRepository;
    private final UserRepository userRepository;
    private final StorageService storageService;

    public RentalService(
            RentalRepository rentalRepository,
            UserRepository userRepository,
            StorageService storageService
    ) {
        this.rentalRepository = rentalRepository;
        this.userRepository = userRepository;
        this.storageService = storageService;
    }

    public Optional<Rental> getRental(final int id) {
        return rentalRepository.findById(id);
    }

    public Iterable<Rental> listRentals() {
        return rentalRepository.findAll();
    }

    public void deleteRental(final int id) {
        rentalRepository.deleteById(id);
    }

    public void saveRental(
            String name,
            BigInteger surface,
            BigInteger price,
            MultipartFile picture,
            String description,
            String username
    ) throws IOException {

        Optional<User> optUser = userRepository.findByEmail(username);

        if (optUser.isEmpty()) {
            throw new AccessDeniedException("Invalid username");
        }
        Rental rental = Rental.builder()
                .name(name)
                .surface(surface)
                .price(price)
                .description(description)
                .owner(optUser.get())
                .build();

        rentalRepository.saveAndFlush(rental);

        Resource resource = storageService.store(picture, Integer.toString(rental.getId()));

        rental.setPicture(resource.getURL().toString());

        rentalRepository.save(rental);
    }

    public void updateRental(int id, String name, BigInteger surface, BigInteger price, String description) throws AccessDeniedException {

        Optional<Rental> optionalRental = rentalRepository.findById(id);

        log.info(optionalRental.toString(), optionalRental);

        if (optionalRental.isEmpty()) {
            throw new AccessDeniedException("");
        }
        Rental rental = optionalRental.get();
        rental.setName(name);
        rental.setSurface(surface);
        rental.setPrice(price);
        rental.setDescription(description);

        rentalRepository.save(rental);
    }

}
