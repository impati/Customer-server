package com.example.customerserver.security.filter;

import com.example.customerserver.domain.Client;
import com.example.customerserver.exception.ClientValidException;
import com.example.customerserver.repository.ClientRepository;
import com.example.customerserver.security.handler.ClientValidationFailureHandler;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class ClientIdValidationFilter extends OncePerRequestFilter {

    private final static String CLIENT_VALID_NAME = "clientId";
    private final static String LOGIN_REQUEST_URI = "/auth/login/**";
    private final static String SIGNUP_REQUEST_URI = "/signup/**";
    private final static String REDIRECT_EDIT_REQUEST_URI = "/client/edit-redirect/**";

    private final ClientRepository clientRepository;
    private final ClientValidationFailureHandler clientValidationFailureHandler;
    private final RedirectStrategy redirectStrategy;
    private final RequestMatcher requestMatcher;

    public ClientIdValidationFilter(ClientRepository clientRepository, ClientValidationFailureHandler clientValidationFailureHandler) {
        this.clientRepository = clientRepository;
        this.clientValidationFailureHandler = clientValidationFailureHandler;
        this.redirectStrategy = new DefaultRedirectStrategy();
        this.requestMatcher = new OrRequestMatcher(
                new AntPathRequestMatcher(LOGIN_REQUEST_URI),
                new AntPathRequestMatcher(SIGNUP_REQUEST_URI),
                new AntPathRequestMatcher(REDIRECT_EDIT_REQUEST_URI)
        );
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            ifRedirectRequest(request, response);
            validClient(request, response);
            filterChain.doFilter(request, response);
        } catch (ClientValidException e) {
            clientValidationFailureHandler.onClientValidFailure(request, response);
        }
    }

    private void ifRedirectRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (requestMatcher.matches(request) && StringUtils.hasText(request.getParameter(CLIENT_VALID_NAME))) {
            String clientId = request.getParameter(CLIENT_VALID_NAME);
            request.getSession().setAttribute(CLIENT_VALID_NAME, clientId);
            redirectStrategy.sendRedirect(request, response, request.getRequestURI());
        }
    }

    private void validClient(HttpServletRequest request, HttpServletResponse response) {
        if (requestMatcher.matches(request)) {
            String clientId = getClientIdFrom(request);
            Client client = clientRepository.findClientByClientId(clientId).orElseThrow(ClientValidException::new);
            addRedirectInCookie(response, client.getRedirectUrl());
        }
    }

    private String getClientIdFrom(HttpServletRequest request) {
        if (request.getHeader(CLIENT_VALID_NAME) != null)
            return request.getHeader(CLIENT_VALID_NAME);
        return String.valueOf(request.getSession().getAttribute(CLIENT_VALID_NAME));
    }

    private void addRedirectInCookie(HttpServletResponse response, String redirectUrl) {
        response.addCookie(new Cookie("redirectUrl", redirectUrl));
    }
}
