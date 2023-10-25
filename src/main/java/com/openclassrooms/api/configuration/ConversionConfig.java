package com.openclassrooms.api.configuration;

import com.openclassrooms.api.service.converter.UserToAuthMeResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.converter.Converter;

import java.util.HashSet;

@Configuration
public class ConversionConfig {

    /** @See <a href="https://docs.spring.io/spring-framework/reference/core/validation/convert.html#core-convert-Spring-config">Configuring a ConversionService</a> */
    @Bean
    public ConversionServiceFactoryBean conversionService() {
        ConversionServiceFactoryBean conversionService = new ConversionServiceFactoryBean();
        HashSet<Converter<?, ?>> converters = new HashSet<>();
        converters.add(new UserToAuthMeResponse());
        conversionService.setConverters(converters);
        return conversionService;
    }
}
