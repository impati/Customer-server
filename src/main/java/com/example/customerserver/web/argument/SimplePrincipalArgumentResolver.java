package com.example.customerserver.web.argument;

import org.springframework.core.MethodParameter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.example.customerserver.exception.HasNoAccessTokenException;
import com.example.customerserver.exception.InvalidAccessTokenException;
import com.example.customerserver.web.data.SimplePrincipal;
import com.example.customerserver.web.token.TokenGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SimplePrincipalArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";

    private final TokenGenerator tokenGenerator;
    private final ObjectMapper objectMapper;

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.getParameterType().isAssignableFrom(SimplePrincipal.class);
    }

    @Override
    public Object resolveArgument(
        final MethodParameter parameter,
        final ModelAndViewContainer mavContainer,
        final NativeWebRequest webRequest,
        final WebDataBinderFactory binderFactory
    ) throws Exception {
        final String bearerToken = webRequest.getHeader(AUTHORIZATION);
        final String accessToken = validateToken(bearerToken);
        return objectMapper.readValue(tokenGenerator.getPrincipal(accessToken), SimplePrincipal.class);
    }

    private String validateToken(final String bearerToken) {
        if (!StringUtils.hasText(bearerToken) || !bearerToken.startsWith(BEARER)) {
            throw new HasNoAccessTokenException();
        }
        final String accessToken = bearerToken.replace(BEARER, "");
        if (!tokenGenerator.validateToken(accessToken)) {
            throw new InvalidAccessTokenException();
        }

        return accessToken;
    }
}
