package com.openclassrooms.api.controller.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.openclassrooms.api.model.entity.User;
import com.openclassrooms.api.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class AuthentificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    @BeforeEach
    void init() {

        userRepository.saveAndFlush(
                User.builder()
                        .email("test@test.com")
                        .name("test TEST")
                        .password(passwordEncoder.encode("test!31"))
                        .build()
        );
    }

    @AfterEach
    void teardown() {
        userRepository.deleteAll();
    }

    @Test
    void shouldRegisterNewUser() throws Exception {

        String jsonNewUser = """
            {"email":"new@test.com","name":"test TEST","password":"test!31"}
            """;

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(jsonNewUser))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }



    @ParameterizedTest
    @MethodSource("com.openclassrooms.api.controller.AuthentificationParams#badRequestWhenRegisterTestData")
    void shouldNotRegisterUser(String jsonUser) throws Exception {
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(jsonUser))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldNotRegisterNoContent() throws Exception {
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }

    @Test
    void shouldLoginUser() throws Exception {

        String jsonString = """
            {"email":"test@test.com","password":"test!31"}
            """;

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    void shouldNotLoginNoContent() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


    @ParameterizedTest
    @MethodSource("com.openclassrooms.api.controller.AuthentificationParams#unauthorizedWhenLoginTestData")
    void shouldNotLoginUser(String jsonCredentials) throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(jsonCredentials))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Invalid credentials"));
    }

}
