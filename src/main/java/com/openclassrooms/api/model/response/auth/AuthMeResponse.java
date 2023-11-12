package com.openclassrooms.api.model.response.auth;

import com.fasterxml.jackson.annotation.*;
import com.openclassrooms.api.model.response.Response;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.Instant;

@Builder
@Data
public class AuthMeResponse implements Response {

    @Positive
    private int id;

    @NotBlank
    private String name;

    @Email
    private String email;

    @NotNull
    @JsonProperty(value = "created_at")
    @JsonFormat(pattern = "yyyy/MM/dd", timezone = "UTC")
    @Schema(
            type = "string",
            example = "1970/01/01",
            pattern = "yyyy/MM/dd"
    )
    private Instant createdAt;

    @NotNull
    @JsonProperty(value = "updated_at")
    @JsonFormat(pattern = "yyyy/MM/dd", timezone = "UTC" )
    @Schema(
            type = "string",
            example = "2023/02/03",
            pattern = "yyyy/MM/dd"
    )
    private Instant updateAt;
}
