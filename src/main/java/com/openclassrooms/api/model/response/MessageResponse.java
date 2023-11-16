package com.openclassrooms.api.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * MessageResponse DTO
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MessageResponse implements Response {

    private String message;
}
