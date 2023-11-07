package com.openclassrooms.api.configuration.converter;

import com.openclassrooms.api.model.entity.Rental;
import com.openclassrooms.api.model.response.rental.RentalResponse;
import org.springframework.core.convert.converter.Converter;

public class RentalToRentalResponse implements Converter<Rental, RentalResponse> {

    @Override
    public RentalResponse convert(Rental rental) {
        return RentalResponse.builder()
                .id(rental.getId())
                .name(rental.getName())
                .surface(rental.getSurface())
                .price(rental.getPrice())
                .picture(rental.getPicture())
                .description(rental.getDescription())
                .ownerId(rental.getOwner().getId())
                .createdAt(rental.getCreatedAt())
                .updatedAt(rental.getUpdatedAt())
                .build();
    }
}
