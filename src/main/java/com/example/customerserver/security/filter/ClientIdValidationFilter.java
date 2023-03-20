package com.example.customerserver.security.filter;

import com.example.customerserver.domain.Client;
import com.example.customerserver.exception.ClientValidException;
import com.example.customerserver.repository.ClientRepository;
import com.example.customerserver.security.handler.ClientValidationFailureHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ClientIdValidationFilter extends OncePerRequestFilter {

    private final static String CLIENT_VALID_HEADER_NAME = "clientId";
    private final static String[] cookieNeedUri = {"/auth/login/**", "/sign/**"};
    private final static String[] freePassUri = {"/auth/code/**", "/client/register/**", "/error/**"};

    private final ClientRepository clientRepository;
    private final ClientValidationFailureHandler clientValidationFailureHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            validClient(request, response);
            filterChain.doFilter(request, response);
        } catch (ClientValidException e) {
            clientValidationFailureHandler.onClientValidFailure(request, response);
        }
    }

    private void validClient(HttpServletRequest request, HttpServletResponse response) {
        if (isFreePass(request)) return;
        String clientId = request.getHeader(CLIENT_VALID_HEADER_NAME);
        Client client = clientRepository.findClientByClientId(clientId).orElseThrow(ClientValidException::new);
        if (isNeedRedirect(request)) addRedirectInCookie(response, client.getRedirectUrl());
    }

    private boolean isFreePass(HttpServletRequest request) {
        if (request.getRequestURI().equals("/signup") && request.getMethod().equals(HttpMethod.POST.name()))
            return true;
        RequestMatcher matcher = new OrRequestMatcher(getMatchers(freePassUri));
        return matcher.matches(request);
    }

    private boolean isNeedRedirect(HttpServletRequest request) {
        RequestMatcher matcher = new OrRequestMatcher(getMatchers(cookieNeedUri));
        return matcher.matches(request);
    }

    private List<RequestMatcher> getMatchers(String[] matcherUri) {
        List<RequestMatcher> requestMatchers = new ArrayList<>();
        for (int i = 0; i < matcherUri.length; i++) {
            requestMatchers.add(new AntPathRequestMatcher(matcherUri[i]));
        }
        return requestMatchers;
    }

    private void addRedirectInCookie(HttpServletResponse response, String redirectUrl) {
        response.addCookie(new Cookie("redirectUrl", redirectUrl));
    }
}
