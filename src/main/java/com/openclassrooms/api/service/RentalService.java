package com.openclassrooms.api.service;

import com.openclassrooms.api.model.entity.Rental;
import com.openclassrooms.api.model.entity.User;
import com.openclassrooms.api.repository.RentalRepository;
import com.openclassrooms.api.repository.UserRepository;
import com.openclassrooms.api.service.storage.StorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigInteger;
import java.nio.file.AccessDeniedException;
import java.util.Optional;

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

    public void saveRental(
            String name,
            BigInteger surface,
            BigInteger price,
            MultipartFile picture,
            String description,
            String username
    ) throws AccessDeniedException {

        final String contentType = picture.getContentType();
        if ( contentType == null || !contentType.startsWith("image") ) {
            throw new AccessDeniedException(picture.getOriginalFilename() + " is not an image !");
        }

        Optional<User> optUser = userRepository.findByEmail(username);

        if (optUser.isEmpty()) {
            throw new AccessDeniedException("Invalid username");
        }

        final String subdir = "images";
        final String filename = storageService.store(picture, subdir);
        final String imageUrl = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .pathSegment(subdir, filename)
                .toUriString();

        Rental rental = Rental.builder()
                .name(name)
                .surface(surface)
                .price(price)
                .picture(imageUrl)
                .description(description)
                .owner(optUser.get())
                .build();

        rentalRepository.saveAndFlush(rental);
    }

    public void updateRental(int id, String name, BigInteger surface, BigInteger price, String description) throws AccessDeniedException {

        Optional<Rental> optionalRental = rentalRepository.findById(id);

        if (optionalRental.isEmpty()) {
            throw new AccessDeniedException("No Rental with id : " + id);
        }
        Rental rental = optionalRental.get();
        rental.setName(name);
        rental.setSurface(surface);
        rental.setPrice(price);
        rental.setDescription(description);

        rentalRepository.save(rental);
    }

}
