package com.openclassrooms.api.model.response.rental;

import com.openclassrooms.api.model.response.Response;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * RentalsResponse DTO
 */
@Data
@Builder
public class RentalsResponse implements Response {

    List<RentalResponse> rentals;
}
