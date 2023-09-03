package com.example.customerserver.web.argument;

import org.springframework.core.MethodParameter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.example.customerserver.security.CustomerPrincipal;
import com.example.customerserver.web.data.SimplePrincipal;
import com.example.customerserver.web.response.TokenResponse;
import com.example.customerserver.web.token.AccessTokenGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AccessTokenArgumentResolver implements HandlerMethodArgumentResolver {

	private final AccessTokenGenerator accessTokenGenerator;
	private final ObjectMapper objectMapper;

	@Override
	public boolean supportsParameter(final MethodParameter parameter) {
		return parameter.getParameterType().isAssignableFrom(TokenResponse.class);
	}

	@Override
	public Object resolveArgument(
		final MethodParameter parameter,
		final ModelAndViewContainer mavContainer,
		final NativeWebRequest webRequest,
		final WebDataBinderFactory binderFactory
	) throws Exception {
		final String accessToken = accessTokenGenerator.createToken(getStringPrincipal());
		return new TokenResponse(accessToken);
	}

	private String getStringPrincipal() throws JsonProcessingException {
		return objectMapper.writeValueAsString(SimplePrincipal.from(getPrincipal()));
	}

	private CustomerPrincipal getPrincipal() {
		return (CustomerPrincipal)SecurityContextHolder
			.getContext()
			.getAuthentication()
			.getPrincipal();
	}
}
