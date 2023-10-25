package com.openclassrooms.api.controller.unit;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.openclassrooms.api.controller.AuthentificationController;
import com.openclassrooms.api.model.response.auth.AuthMeResponse;
import com.openclassrooms.api.model.entity.User;
import com.openclassrooms.api.service.AuthentificationService;

import com.openclassrooms.api.service.converter.UserToAuthMeResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.Optional;


@WebMvcTest(controllers = AuthentificationController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthentificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private GenericConversionService conversionService;

    @MockBean
    private AuthentificationService authentificationService;

    @BeforeEach
    void init() {

        Mockito.when(authentificationService.registerUser("test@test.com", "test TEST", "test!31"))
                .thenReturn(Optional.of("jwt"));

        Mockito.when(authentificationService.loginUser("test@test.com", "test!31"))
                .thenReturn(Optional.of("jwt"));

        Mockito.when(this.authentificationService.authUser( "jwt"))
                .thenReturn(Optional.of(new User()));

        AuthMeResponse authMeResponse = AuthMeResponse.builder()
                .id(0)
                .name("test TEST")
                .email("test@test.com")
                .createdAt(Instant.now())
                .updateAt(Instant.now())
                .build();

        conversionService.addConverter(new UserToAuthMeResponse());
        Mockito.when(conversionService.convert(new User(), AuthMeResponse.class))
                .thenReturn(authMeResponse);
    }


    @Test
    void shouldRegisterUser() throws Exception {
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"email\": \"test@test.com\",\"name\": \"test TEST\",\"password\": \"test!31\"}")
        ).andExpect(status().isOk());
    }

    @Test
    void shouldNotRegisterUser1() throws Exception {
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{}")
        ).andExpect(status().isBadRequest());
    }

    @Test
    void shouldNotRegisterUser2() throws Exception {
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"email\": \"test@test.com\",\"name\": \"test TEST\",\"password\": \"wrong\"}")
        ).andExpect(status().isBadRequest());
    }

    @Test
    void shouldLogUserIn() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"login\": \"test@test.com\", \"password\": \"test!31\"}")
        ).andExpect(status().isOk());
    }

    @Test
    void shouldNotLoginUser() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{}")
                ).andExpect(status().isUnauthorized());
    }

    @Test
    void shouldShowUserDetails() throws Exception {
        mockMvc.perform(get("/api/auth/me")
                        .header("Authorization", "jwt")
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk());
    }

    @Test
    void shouldNotShowUserDetails() throws Exception {
        mockMvc.perform(get("/api/auth/me")
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnauthorized());
    }

}
