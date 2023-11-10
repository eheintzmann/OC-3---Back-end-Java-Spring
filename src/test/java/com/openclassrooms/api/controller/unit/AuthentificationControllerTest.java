package com.openclassrooms.api.controller.unit;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.openclassrooms.api.controller.AuthentificationController;
import com.openclassrooms.api.configuration.jwt.JwtService;
import com.openclassrooms.api.model.response.auth.AuthMeResponse;
import com.openclassrooms.api.model.entity.User;
import com.openclassrooms.api.service.AuthentificationService;
import com.openclassrooms.api.configuration.converter.UserToAuthMeResponse;

import com.openclassrooms.api.service.storage.StorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

class AuthentificationControllerTest {

    @WebMvcTest(controllers = AuthentificationController.class)
    @AutoConfigureMockMvc(addFilters = false)
    @Nested
    class AuthentificationControllerPostMethodsTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private AuthentificationService authentificationService;

        @MockBean
        private JwtService jwtService;

        @MockBean
        private StorageService storageService;

        @BeforeEach
        void setup() {

            Mockito.when(authentificationService.registerUser("test@test.com", "test TEST", "test!31"))
                    .thenReturn(Optional.empty());

            Mockito.when(authentificationService.registerUser("new@test.com", "test TEST", "test!31"))
                    .thenReturn(Optional.of("jwt"));

            Mockito.when(authentificationService.loginUser("test@test.com", "test!31"))
                    .thenReturn(Optional.of("jwt"));
        }

        @Test
        void shouldReturnOkWhenRegisteringNewUser() throws Exception {
            mockMvc.perform(post("/api/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content("{\"email\": \"new@test.com\",\"name\": \"test TEST\",\"password\": \"test!31\"}")
            ).andExpect(status().isOk());
        }

        @ParameterizedTest
        @MethodSource("com.openclassrooms.api.controller.AuthentificationParams#badRequestWhenRegisterTestData")
        void shouldReturnBadRequestWhenRegisteringUser(String jsonUser) throws Exception {
            mockMvc.perform(post("/api/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(jsonUser)
            ).andExpect(status().isBadRequest());
        }

        @Test
        void shouldReturnOkWhenLoginUser() throws Exception {
            mockMvc.perform(post("/api/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content("{\"login\": \"test@test.com\", \"password\": \"test!31\"}"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.token").value("jwt"));
        }

        @Test
        void shouldReturnUnauthorizedWhenLoginUser() throws Exception {
            mockMvc.perform(post("/api/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content("{}"))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.message").exists());
        }
    }

    @WebMvcTest(controllers = AuthentificationController.class)
    @AutoConfigureMockMvc
    @Nested
    class AuthentificationControllerGetMethodsTest {

        @Autowired
        private MockMvc mockMvc;

        @SpyBean
        private GenericConversionService conversionService;

        @MockBean
        private AuthentificationService authentificationService;

        @MockBean
        private JwtService jwtService;

        @MockBean
        private UserToAuthMeResponse converter;

        @MockBean
        private StorageService storageService;

        @BeforeEach
        void setup() {

            User user0 = User.builder()
                    .id(0)
                    .name("test")
                    .email("test@test.com")
                    .build();

            Mockito.when(authentificationService.authUser("test@test.com"))
                    .thenReturn(Optional.of(user0));

            Mockito.when(converter.convert(user0))
                    .thenReturn(AuthMeResponse.builder()
                            .email("mocked@test.com")
                            .build());

            conversionService.addConverter(converter);

//            Mockito.when(conversionService.convert(any(User.class), notNull(AuthMeResponse.class)))
//                    .thenReturn(AuthMeResponse.builder().email("test@test.com").build());
        }

        @Test
        @WithMockUser(username = "test@test.com")
        void shouldShowUserDetails() throws Exception {
            mockMvc.perform(get("/api/auth/me")
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.email").value("mocked@test.com"));
        }

        @Test
        @WithMockUser(username = "none@test.com")
        void shouldNotShowUserDetails() throws Exception {
            mockMvc.perform(get("/api/auth/me")
                    .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isUnauthorized());
        }
    }

}
