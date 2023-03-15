package com.example.customerserver.security.filter;

import com.example.customerserver.domain.Customer;
import com.example.customerserver.repository.CustomerRepository;
import com.example.customerserver.security.CustomerPrincipal;
import com.example.customerserver.web.token.CodeGenerator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CodeValidationFilter extends OncePerRequestFilter {

    private static final String DEFAULT_REQUEST_URI = "/auth/gettoken";
    private static final String AUTHORIZATION = "Authorization";
    private final CustomerRepository customerRepository;
    private final CodeGenerator codeProvider;
    private final RequestMatcher requestMatcher;

    public CodeValidationFilter(CustomerRepository customerRepository, CodeGenerator codeProvider) {
        this.customerRepository = customerRepository;
        this.codeProvider = codeProvider;
        this.requestMatcher = new AntPathRequestMatcher(DEFAULT_REQUEST_URI);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (requestMatcher.matches(request)) {
            Customer customer = validCodeAndGetCustomer(request.getHeader(AUTHORIZATION));
            SecurityContextHolder.getContext().setAuthentication(getAuthenticationFrom(customer));
        }
        filterChain.doFilter(request, response);
    }

    private Authentication getAuthenticationFrom(Customer customer) {
        CustomerPrincipal customerPrincipal = CustomerPrincipal.create(customer);
        return new OAuth2AuthenticationToken(customerPrincipal,
                customerPrincipal.getAuthorities(),
                customerPrincipal.getProviderType().toString());
    }

    private Customer validCodeAndGetCustomer(String code) {
        Long id = Long.parseLong(codeProvider.getPrincipal(code));
        return customerRepository.findById(id).orElseThrow(IllegalStateException::new);
    }
}
