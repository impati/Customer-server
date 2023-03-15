package com.example.customerserver.web.argument;

import com.example.customerserver.web.data.SimplePrincipal;
import com.example.customerserver.web.token.TokenGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@RequiredArgsConstructor
public class SimplePrincipalArgumentResolver implements HandlerMethodArgumentResolver {

    private final static String AUTHORIZATION = "Authorization";
    private final static String BEARER = "Bearer ";
    private final TokenGenerator tokenGenerator;
    private final ObjectMapper objectMapper;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().isAssignableFrom(SimplePrincipal.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String bearerToken = webRequest.getHeader(AUTHORIZATION);
        String accessToken = validateToken(bearerToken);
        return objectMapper.readValue(tokenGenerator.getPrincipal(accessToken), SimplePrincipal.class);
    }

    private String validateToken(String bearerToken) {
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER)) {
            return bearerToken.replace(BEARER, "");
        }
        throw new IllegalStateException();
    }
}
