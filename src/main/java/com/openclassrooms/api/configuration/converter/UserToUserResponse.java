package com.openclassrooms.api.configuration.converter;

import com.openclassrooms.api.model.entity.User;
import com.openclassrooms.api.model.response.user.UserResponse;
import org.springframework.core.convert.converter.Converter;


public class UserToUserResponse implements Converter<User, UserResponse> {

    @Override
    public UserResponse convert(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
