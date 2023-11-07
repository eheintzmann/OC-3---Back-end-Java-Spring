package com.openclassrooms.api.model.response.rental;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RentalsResponse {

    List<RentalResponse> rentals;
}
