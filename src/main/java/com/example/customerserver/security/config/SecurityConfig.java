package com.example.customerserver.security.config;

import com.example.customerserver.repository.CustomerRepository;
import com.example.customerserver.security.filter.CodeValidationFilter;
import com.example.customerserver.security.filter.KeycloakLoginFilter;
import com.example.customerserver.security.handler.KeycloakLoginFailureHandler;
import com.example.customerserver.security.oauth2.OAuth2CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.OAuth2AuthorizationContext;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final ClientRegistrationRepository clientRegistrationRepository;
    private final OAuth2AuthorizedClientRepository authorizedClientRepository;
    private final CustomerRepository customerRepository;
    private final CodeValidationFilter codeValidationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.csrf().disable();

        httpSecurity.anonymous();

        httpSecurity.authorizeHttpRequests()
                .anyRequest().permitAll();

        httpSecurity.addFilterAt(codeValidationFilter, AnonymousAuthenticationFilter.class);
        httpSecurity.addFilterAfter(keycloakLoginFilter(), AnonymousAuthenticationFilter.class);

        httpSecurity
                .oauth2Login()
                .defaultSuccessUrl("/auth/code")
                .loginPage("/login")
                .userInfoEndpoint()
                .userService(customOAuth2UserService());

        return httpSecurity.build();
    }

    public KeycloakLoginFilter keycloakLoginFilter() {
        KeycloakLoginFilter keycloakLoginFilter = new KeycloakLoginFilter(authorizedClientManager(), customOAuth2UserService());
        keycloakLoginFilter.setAuthenticationFailureHandler(keycloakLoginFailureHandler());
        keycloakLoginFilter.setAuthenticationSuccessHandler(new SimpleUrlAuthenticationSuccessHandler("/auth/code"));
        return keycloakLoginFilter;
    }

    public KeycloakLoginFailureHandler keycloakLoginFailureHandler() {
        return new KeycloakLoginFailureHandler();
    }

    @Bean
    public OAuth2CustomerService customOAuth2UserService() {
        return new OAuth2CustomerService(customerRepository);
    }

    @Bean
    public DefaultOAuth2AuthorizedClientManager authorizedClientManager() {

        OAuth2AuthorizedClientProvider authorizedClientProvider =
                OAuth2AuthorizedClientProviderBuilder.builder()
                        .authorizationCode()
                        .clientCredentials()
                        .password(passwordGrantBuilder -> passwordGrantBuilder.clockSkew(Duration.ofSeconds(3600)))
                        .refreshToken(refreshTokenGrantBuilder -> refreshTokenGrantBuilder.clockSkew(Duration.ofSeconds(3600)))
                        .build();

        DefaultOAuth2AuthorizedClientManager authorizedClientManager =
                new DefaultOAuth2AuthorizedClientManager(
                        clientRegistrationRepository, authorizedClientRepository);

        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);
        authorizedClientManager.setContextAttributesMapper(contextAttributesMapper());

        return authorizedClientManager;
    }

    private Function<OAuth2AuthorizeRequest, Map<String, Object>> contextAttributesMapper() {
        return authorizeRequest -> {
            Map<String, Object> contextAttributes = Collections.emptyMap();
            HttpServletRequest servletRequest = authorizeRequest.getAttribute(HttpServletRequest.class.getName());
            String username = servletRequest.getParameter(OAuth2ParameterNames.USERNAME);
            String password = servletRequest.getParameter(OAuth2ParameterNames.PASSWORD);
            if (StringUtils.hasText(username) && StringUtils.hasText(password)) {
                contextAttributes = new HashMap<>();
                contextAttributes.put(OAuth2AuthorizationContext.USERNAME_ATTRIBUTE_NAME, username);
                contextAttributes.put(OAuth2AuthorizationContext.PASSWORD_ATTRIBUTE_NAME, password);
            }
            return contextAttributes;
        };
    }

}
