package com.openclassrooms.api.configuration;

import com.openclassrooms.api.configuration.converter.RentalToRentalResponse;
import com.openclassrooms.api.configuration.converter.UserToAuthMeResponse;
import com.openclassrooms.api.configuration.converter.UserToUserResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.converter.Converter;

import java.util.HashSet;

/**
 * Configuration class for Conversion service
 * @See <a href="https://docs.spring.io/spring-framework/reference/core/validation/convert.html#core-convert-Spring-config">Configuring a ConversionService</a>
 */
@Configuration
public class ConversionConfig {

    /**
     * New ConversionService instance
     * @See <a href="https://docs.spring.io/spring-framework/reference/core/validation/convert.html">Spring Type Conversion</a>
     *
     * @return ConversionServiceFactoryBean
     */
    @Bean
    public ConversionServiceFactoryBean conversionService() {
        ConversionServiceFactoryBean conversionService = new ConversionServiceFactoryBean();
        HashSet<Converter<?, ?>> converters = new HashSet<>();

        converters.add(new UserToAuthMeResponse());
        converters.add(new UserToUserResponse());
        converters.add(new RentalToRentalResponse());

        conversionService.setConverters(converters);
        return conversionService;
    }
}
