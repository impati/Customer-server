package com.example.customerserver.web.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.customerserver.repository.CustomerRepository;
import com.example.customerserver.web.argument.AccessTokenArgumentResolver;
import com.example.customerserver.web.argument.CodeArgumentResolver;
import com.example.customerserver.web.argument.SimplePrincipalArgumentResolver;
import com.example.customerserver.web.token.AccessTokenGenerator;
import com.example.customerserver.web.token.CodeGenerator;
import com.example.customerserver.web.validation.KeyCloakUniqueValidator;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

	private final CodeGenerator codeGenerator;
	private final AccessTokenGenerator accessTokenGenerator;
	private final ObjectMapper objectMapper;
	private final CustomerRepository customerRepository;

	@Override
	public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(new CodeArgumentResolver(codeGenerator));
		resolvers.add(new AccessTokenArgumentResolver(accessTokenGenerator, objectMapper));
		resolvers.add(new SimplePrincipalArgumentResolver(accessTokenGenerator, objectMapper));
	}

	@Bean
	public KeyCloakUniqueValidator keycloakUsernameUniqueValidator() {
		return new KeyCloakUniqueValidator(customerRepository);
	}
}
