package com.example.customerserver.web.argument;

import com.example.customerserver.exception.UnAuthenticationException;
import com.example.customerserver.security.CustomerPrincipal;
import com.example.customerserver.web.request.CodeRequest;
import com.example.customerserver.web.token.TokenGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@RequiredArgsConstructor
public class CodeArgumentResolver implements HandlerMethodArgumentResolver {

    private final TokenGenerator codeGenerator;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().isAssignableFrom(CodeRequest.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        CustomerPrincipal customerPrincipal = getPrincipal();
        String code = codeGenerator.createToken(String.valueOf(customerPrincipal.getId()));
        return new CodeRequest(code);
    }

    private CustomerPrincipal getPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) throw new UnAuthenticationException();
        if (!isPossibleCasting(authentication.getPrincipal())) throw new UnAuthenticationException();
        return (CustomerPrincipal) authentication.getPrincipal();
    }

    private boolean isPossibleCasting(Object principal) {
        return principal.getClass().isAssignableFrom(CustomerPrincipal.class);
    }
}
