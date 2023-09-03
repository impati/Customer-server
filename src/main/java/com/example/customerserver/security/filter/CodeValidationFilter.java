package com.example.customerserver.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.customerserver.domain.Customer;
import com.example.customerserver.exception.InvalidCodeException;
import com.example.customerserver.repository.CustomerRepository;
import com.example.customerserver.security.CustomerPrincipal;
import com.example.customerserver.security.handler.CodeValidationFailureHandler;
import com.example.customerserver.web.token.CodeGenerator;

@Component
public class CodeValidationFilter extends OncePerRequestFilter {

    private static final String DEFAULT_REQUEST_URI = "/auth/gettoken";
    private static final String AUTHORIZATION = "Authorization";

    private final CustomerRepository customerRepository;
    private final CodeGenerator codeProvider;
    private final CodeValidationFailureHandler codeValidationFailureHandler;
    private final RequestMatcher requestMatcher;

    public CodeValidationFilter(
        final CustomerRepository customerRepository,
        final CodeGenerator codeProvider,
        final CodeValidationFailureHandler codeValidationFailureHandler
    ) {
        this.customerRepository = customerRepository;
        this.codeProvider = codeProvider;
        this.codeValidationFailureHandler = codeValidationFailureHandler;
        this.requestMatcher = new AntPathRequestMatcher(DEFAULT_REQUEST_URI);
    }

    @Override
    protected void doFilterInternal(
        final HttpServletRequest request,
        final HttpServletResponse response,
        final FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            validCodeAndSetAuthentication(request);
            filterChain.doFilter(request, response);
        } catch (InvalidCodeException e) {
            codeValidationFailureHandler.onClientValidFailure(request, response);
        }
    }

    private void validCodeAndSetAuthentication(final HttpServletRequest request) {
        if (requestMatcher.matches(request)) {
            final Customer customer = validCodeAndGetCustomer(request.getHeader(AUTHORIZATION));
            SecurityContextHolder.getContext().setAuthentication(getAuthenticationFrom(customer));
        }
    }

    private Authentication getAuthenticationFrom(final Customer customer) {
        final CustomerPrincipal customerPrincipal = CustomerPrincipal.create(customer);
        return new OAuth2AuthenticationToken(customerPrincipal,
            customerPrincipal.getAuthorities(),
            customerPrincipal.getProviderType().toString());
    }

    private Customer validCodeAndGetCustomer(final String code) {
        if (!codeProvider.validateToken(code)) {
            throw new InvalidCodeException();
        }

        Long id = Long.parseLong(codeProvider.getPrincipal(code));
        return customerRepository.findById(id).orElseThrow(InvalidCodeException::new);
    }
}
