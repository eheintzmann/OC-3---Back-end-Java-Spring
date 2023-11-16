package com.openclassrooms.api.configuration.converter;

import com.openclassrooms.api.model.response.auth.AuthMeResponse;
import com.openclassrooms.api.model.entity.User;
import org.springframework.core.convert.converter.Converter;

/**
 * Convert User to AuthMeResponse
 */
public class UserToAuthMeResponse implements Converter<User, AuthMeResponse> {

    /**
     * Convert User to AuthMeResponse
     *
     * @param user User
     * @return AuthMeResponse
     */
    @Override
    public AuthMeResponse convert(User user) {

        return AuthMeResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .updateAt(user.getUpdatedAt())
                .build();
    }

}
