package com.openclassrooms.api.model.response.rental;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.openclassrooms.api.model.response.Response;
import lombok.Builder;
import lombok.Data;

import java.math.BigInteger;
import java.time.Instant;

@Data
@Builder
public class RentalResponse implements Response {

    private int id;

    private String name;

    private BigInteger surface;

    private BigInteger price;

    private String picture;

    private String description;

    @JsonProperty(value = "owner_id")
    private int ownerId;

    @JsonProperty(value = "created_at")
    @JsonFormat(pattern = "yyyy/MM/dd", timezone = "UTC")
    private Instant createdAt;

    @JsonProperty(value = "updated_at")
    @JsonFormat(pattern = "yyyy/MM/dd", timezone = "UTC")
    private Instant updatedAt;

}