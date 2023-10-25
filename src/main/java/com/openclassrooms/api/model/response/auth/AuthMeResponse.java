package com.openclassrooms.api.model.response.auth;

import com.fasterxml.jackson.annotation.*;
import com.openclassrooms.api.model.response.Response;
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
    private Instant createdAt;

    @NotNull
    @JsonProperty(value = "updated_at")
    @JsonFormat(pattern = "yyyy/MM/dd", timezone = "UTC" )
    private Instant updateAt;
}
