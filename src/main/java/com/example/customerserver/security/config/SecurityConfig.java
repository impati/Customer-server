package com.example.customerserver.security.config;

import com.example.customerserver.repository.CustomerRepository;
import com.example.customerserver.security.filter.CodeValidationFilter;
import com.example.customerserver.security.oauth2.OAuth2CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomerRepository customerRepository;
    private final CodeValidationFilter codeValidationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.csrf().disable();

        httpSecurity.anonymous();

        httpSecurity.authorizeHttpRequests()
                .anyRequest().permitAll();

        httpSecurity.addFilterAt(codeValidationFilter, AnonymousAuthenticationFilter.class);

        httpSecurity
                .oauth2Login()
                .loginPage("/login")
                .userInfoEndpoint()
                .userService(customOAuth2UserService());

        return httpSecurity.build();
    }

    @Bean
    public OAuth2CustomerService customOAuth2UserService() {
        return new OAuth2CustomerService(customerRepository);
    }


}
