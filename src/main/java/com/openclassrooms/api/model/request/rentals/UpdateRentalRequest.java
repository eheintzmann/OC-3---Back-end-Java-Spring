package com.openclassrooms.api.model.request.rentals;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * UpdateRentalRequest DTO
 */
@Data
@Builder
public class UpdateRentalRequest {

    @NotBlank
    private String name;

    @NotNull
    private BigDecimal surface;

    @NotNull
    private BigDecimal price;

    @NotNull
    private String description;
}
