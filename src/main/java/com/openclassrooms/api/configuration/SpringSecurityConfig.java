package com.openclassrooms.api.configuration;

import com.openclassrooms.api.configuration.jwt.JwtFilter;
import com.openclassrooms.api.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Slf4j
@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    private final JwtFilter jwtFilter;
    private final UserRepository userRepository;

    public SpringSecurityConfig(JwtFilter jwtFilter, UserRepository userRepository) {
        this.jwtFilter = jwtFilter;
        this.userRepository = userRepository;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByEmail(username)
                .orElseThrow(
                        () -> new UsernameNotFoundException("User " + username + " not found"));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // Use stateless session; session won't be used to store user's state.
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // CSRF is not needed
        http.csrf(AbstractHttpConfigurer::disable);

        http.authorizeHttpRequests(requestMatcherRegistry -> requestMatcherRegistry
                // Do not authenticate these requests
                .requestMatchers(
                        antMatcher(HttpMethod.POST, "/api/auth/login"),
                        antMatcher(HttpMethod.POST, "/api/auth/register"),
                        antMatcher(HttpMethod.GET, "/images/**"),
                        antMatcher(HttpMethod.GET, "/doc/**")
                ).permitAll()
                // Authenticate these requests
                .requestMatchers(antMatcher("/api/**")).authenticated()
                // Deny all other requests
                .anyRequest().denyAll()
        );

        // Send Http status 401 when error occurs
        http.exceptionHandling(handlingConfigurer -> handlingConfigurer
                .authenticationEntryPoint((request, response, exception) -> response
                        .sendError(
                                HttpServletResponse.SC_UNAUTHORIZED,
                                exception.getMessage()
                        )
                )
        );

        // Add a filter to validate the tokens with every request
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
