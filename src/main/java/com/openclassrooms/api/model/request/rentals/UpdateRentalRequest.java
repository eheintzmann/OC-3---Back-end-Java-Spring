package com.openclassrooms.api.model.request.rentals;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigInteger;

@Data
@Builder
public class UpdateRentalRequest {

    @NotBlank
    private String name;

    @NotNull
    private BigInteger surface;

    @NotNull
    private BigInteger price;

    @NotNull
    private String description;
}
