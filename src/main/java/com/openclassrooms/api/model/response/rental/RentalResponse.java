package com.openclassrooms.api.model.response.rental;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.openclassrooms.api.model.response.Response;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * RentalResponse DTO
 */
@Data
@Builder
public class RentalResponse implements Response {

    private int id;

    private String name;

    private BigDecimal surface;

    private BigDecimal price;

    private String picture;

    private String description;

    @JsonProperty(value = "owner_id")
    private int ownerId;

    @JsonProperty(value = "created_at")
    @JsonFormat(pattern = "yyyy/MM/dd", timezone = "UTC")
    @Schema(
            type = "string",
            example = "1970/01/01",
            pattern = "yyyy/MM/dd"
    )
    private Instant createdAt;

    @JsonProperty(value = "updated_at")
    @JsonFormat(pattern = "yyyy/MM/dd", timezone = "UTC")
    @Schema(
            type = "string",
            example = "2023/02/03",
            pattern = "yyyy/MM/dd"
    )
    private Instant updatedAt;

}