package com.web.todo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class ObjectMapperConfig {

    @Bean("objectMapper")
    public ObjectMapper objectMapper(){
        return Jackson2ObjectMapperBuilder.json()
                .build();
    }
}
