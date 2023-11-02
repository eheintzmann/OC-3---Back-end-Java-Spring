package com.openclassrooms.api.model.response.user;

import com.openclassrooms.api.model.response.Response;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class UserResponse implements Response {

    private int id;

    private String email;

    private String name;

    private Instant createdAt;

    private Instant updatedAt;
}
