package com.example.customerserver.web.config;

import com.example.customerserver.web.token.AccessTokenGenerator;
import com.example.customerserver.web.token.CodeGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TokenConfig {

    @Bean(initMethod = "afterPropertiesSet")
    public CodeGenerator codeGenerator() {
        return new CodeGenerator();
    }

    @Bean(initMethod = "afterPropertiesSet")
    public AccessTokenGenerator tokenGenerator() {
        return new AccessTokenGenerator();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
