package com.openclassrooms.api.service.converter;

import com.openclassrooms.api.model.response.auth.AuthMeResponse;
import com.openclassrooms.api.model.entity.User;
import org.springframework.core.convert.converter.Converter;

public class UserToAuthMeResponse implements Converter<User, AuthMeResponse> {

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
