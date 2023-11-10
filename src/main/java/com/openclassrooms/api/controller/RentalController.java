package com.openclassrooms.api.controller;

import com.openclassrooms.api.model.entity.Rental;
import com.openclassrooms.api.model.request.rentals.CreateRentalRequest;
import com.openclassrooms.api.model.request.rentals.UpdateRentalRequest;
import com.openclassrooms.api.model.response.MessageResponse;
import com.openclassrooms.api.model.response.rental.RentalResponse;
import com.openclassrooms.api.model.response.rental.RentalsResponse;
import com.openclassrooms.api.service.RentalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Tag( name = "rental", description = "Rentals operations" )
@RestController
@RequestMapping("/api/rentals/")
public class RentalController {
    private final RentalService rentalService;
    private final ConversionService conversionService;

    public RentalController(
            RentalService rentalService,
            ConversionService conversionService
    ) {
        this.rentalService = rentalService;
        this.conversionService = conversionService;
    }

    @Operation(summary = "get all", description = "Get all rentals")
    @GetMapping(
            path = "",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public RentalsResponse getRentals() {
        
        
        Iterable<Rental> rentals = rentalService.listRentals();
        
        List<RentalResponse> rentalsList = new ArrayList<>();

        for ( Rental rental : rentals ) {
            rentalsList.add(conversionService.convert(rental, RentalResponse.class));
        }
        return RentalsResponse.builder()
                .rentals(rentalsList)
                .build();
    }

    @Operation(summary = "get", description = "Get rental by id")
    @GetMapping(
            path = "{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public RentalResponse getRental(@PathVariable int id) {

        Optional<Rental> optRental = rentalService.getRental(id);

        return optRental.map(
                rental -> conversionService.convert(rental, RentalResponse.class))
                .orElseThrow(() -> new AccessDeniedException(""));
    }

    @Operation(summary = "create", description = "Create new rental")
    @PostMapping(
            path = "",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public MessageResponse postRental(@ModelAttribute CreateRentalRequest request, Principal principal) throws IOException {

        rentalService.saveRental(
                request.getName(),
                request.getSurface(),
                request.getPrice(),
                request.getPicture(),
                request.getDescription(),
                principal.getName()
        );

        return new MessageResponse("Rental created !");
    }

    @Operation(summary = "update", description = "Update existing rental")
    @PutMapping(
            path = "{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public MessageResponse putRental(@PathVariable int id, @ModelAttribute UpdateRentalRequest request) throws java.nio.file.AccessDeniedException {

        rentalService.updateRental(
                id,
                request.getName(),
                request.getSurface(),
                request.getPrice(),
                request.getDescription()
                );

        return new MessageResponse("Rental updated !");
    }
}
