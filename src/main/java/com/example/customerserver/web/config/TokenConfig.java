package com.example.customerserver.web.config;

import com.example.customerserver.web.token.CodeGenerator;
import com.example.customerserver.web.token.TokenGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TokenConfig {

    @Bean(initMethod = "afterPropertiesSet")
    public TokenGenerator codeGenerator() {
        return new CodeGenerator();
    }
}
