package com.openclassrooms.api.model.request.rentals;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigInteger;

@Data
@Builder
public class CreateRentalRequest {

    @NotBlank
    private String name;

    @NotNull
    private BigInteger surface;

    @NotNull
    private BigInteger price;

    private MultipartFile picture;

    @NotNull
    private String description;
}
