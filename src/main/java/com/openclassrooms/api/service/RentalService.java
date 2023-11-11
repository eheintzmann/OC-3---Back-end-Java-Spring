package com.openclassrooms.api.service;

import com.openclassrooms.api.exception.StorageException;
import com.openclassrooms.api.model.entity.Rental;
import com.openclassrooms.api.model.entity.User;
import com.openclassrooms.api.repository.RentalRepository;
import com.openclassrooms.api.repository.UserRepository;
import com.openclassrooms.api.service.storage.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigInteger;
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


    public boolean saveRental(
            String name,
            BigInteger surface,
            BigInteger price,
            MultipartFile picture,
            String description,
            String username
    ) {

        final String contentType = picture.getContentType();
        if ( contentType == null || !contentType.startsWith("image") ) {
            log.error(picture.getOriginalFilename() + " is not an image !");
            return false;
        }

        Optional<User> optUser = userRepository.findByEmail(username);

        if (optUser.isEmpty()) {
            log.error("Invalid username");
            return false;
        }

        final String subdir = "images";
        String imageUrl;
        try {
            final String filename = storageService.store(picture, subdir);
            imageUrl = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .pathSegment(subdir, filename)
                    .toUriString();
        } catch (IllegalArgumentException | StorageException ex) {
            log.error(ex.getMessage());
            return false;
        }

        Rental rental = Rental.builder()
                .name(name)
                .surface(surface)
                .price(price)
                .picture(imageUrl)
                .description(description)
                .owner(optUser.get())
                .build();

        rentalRepository.saveAndFlush(rental);

        return true;
    }


    public boolean updateRental(int id, String name, BigInteger surface, BigInteger price, String description) {

        Optional<Rental> optRental = rentalRepository.findById(id);

        if (optRental.isEmpty()) {
            log.error("Rental " + id + " does not exist !");
            return false;
        }
        Rental rental = optRental.get();
        rental.setName(name);
        rental.setSurface(surface);
        rental.setPrice(price);
        rental.setDescription(description);

        rentalRepository.save(rental);
        return true;
    }
}
