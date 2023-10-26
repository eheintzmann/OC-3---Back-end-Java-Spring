package com.openclassrooms.api.controller.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
                .andExpect(jsonPath("$.token").value("new@test.com"));
    }

    public static String[][] notRegistrableTestData() {
        // @formatter:off
        return new String[][]{

//                { // Blank
//                    """
//                    """
//                },
//                { // Invalid
//                    """
//                    {
//                    """
//                },
                { // Empty body
                    """
                    {}
                    """
                },
                { // Additional field "login"
                    """
                    {"email":"test@test.com","login":"test@test.com","name":"test TEST","password":"test!31"}
                    """
                },
                { // Existing User
                    """
                    {"email":"test@test.com","name":"test TEST","password":"test!31"}
                    """
                },
                // Email
                { // Invalid Email
                    """
                    {"email":"new","name":"test TEST","password":"test!31"}
                    """
                },
                { // Blank Email
                    """
                    {"email":"","name":"test TEST","password":"test!31"}
                    """
                },
                { // Null Email
                    """
                    {"email": null,"name":"test TEST","password":"test!31"}
                    """
                },
                { // Missing Email
                    """
                    {"name":"test TEST","password":"test!31"}
                    """
                },
                // Name
                { // Blank Name
                    """
                    {"email":"test@test.com","name":"","password":"test!31"}
                    """
                },
                { // Null Name
                    """
                    {"email":"test@test.com","name":null,"password":"test!31"}
                    """
                },
                { // Missing Name
                    """
                    {"email":"new@test.com","password":"test!31"}
                    """
                },
                // Password
                { // Blank password
                    """
                    {"email":"test@test.com","name":"test TEST","password":""}
                    """
                },
                { // Null password
                    """
                    {"email":"test@test.com","name":"test TEST","password":null}
                    """
                },
                { // Missing Password
                    """
                    {"email":"new@test.com","name":"test TEST"}
                    """
                }
        };
        // @formatter:on
    }

    @ParameterizedTest
    @MethodSource("notRegistrableTestData")
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
            {"login":"test@test.com","password":"test!31"}
            """;

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("test@test.com"));
    }

    @Test
    void shouldNotLoginNoContent() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Invalid credentials"));
    }

    public static String[][] noLoginTestData() {
        // @formatter:off
        return new String[][]{

//                { // Blank
//                    """
//                    """
//                },
//                { // Invalid
//                    """
//                    {
//                    """
//                },
                { // Empty body
                    """
                    {}
                    """
                },
//                { // Additional field "name"
//                    """
//                    {"login":"test@test.com","suppl":"test TEST","password":"test!31"}
//                    """
//                },
                { // Non Existing User
                    """
                    {"login":"none@test.com","password":"test!31"}
                    """
                },
                // Login
                { // Invalid Login
                    """
                    {"login":"test","password":"test!31"}
                    """
                },
                { // Blank Login
                    """
                    {"login":"","password":"test!31"}
                    """
                },
                { // Null Login
                    """
                    {"login":null,"password":"test!31"}
                    """
                },
                { // Missing Login
                    """
                    {"password":"test!31"}
                    """
                },
                // Password
                { // Wrong Password
                    """
                    {"login":"test@test.com","password":"test31"}
                    """
                },
                { // Blank Password
                    """
                    {"login":"test@test.com","password":""}
                    """
                },
                { // Null Password
                    """
                    {"login":"test@test.com","password":null}
                    """
                },
                { // Missing Password
                    """
                    {"login":"new@test.com"}
                    """
                }
        };
        // @formatter:on
    }

    @ParameterizedTest
    @MethodSource("noLoginTestData")
    void shouldNotLoginUser(String jsonCredentials) throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(jsonCredentials))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Invalid credentials"));
    }

    @Test
    void shouldShowUserDetails() throws Exception {
        mockMvc.perform(get("/api/auth/me")
                        .header("Authorization", "test@test.com")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@test.com"));
    }

    @Test
    void shouldNotShowUserDetails() throws Exception {
        mockMvc.perform(get("/api/auth/me")
                        .header("Authorization", "none@test.com")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

}
