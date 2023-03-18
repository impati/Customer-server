package com.example.customerserver.security.filter;

import com.example.customerserver.domain.Client;
import com.example.customerserver.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
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
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ClientIdValidationFilter extends OncePerRequestFilter {


    private final static String[] cookieNeedUri = {"/auth/login/**", "/sign/**"};
    private final static String[] freePassUri = {"/auth/code/**", "/client/register/**"};

    private final ClientRepository clientRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String clientId = request.getHeader("clientId");
        if (StringUtils.hasText(clientId)) {
            Client client = clientRepository.findClientByClientId(clientId).orElseThrow(IllegalStateException::new);
            if (isNeedCooke(request)) addCookie(response, client.getRedirectUrl());
            filterChain.doFilter(request, response);
        } else if (isFreePass(request)) {
            filterChain.doFilter(request, response);
        } else
            throw new IllegalStateException();
    }

    private boolean isFreePass(HttpServletRequest request) {
        if (request.getRequestURI().equals("/signup") && request.getMethod().equals(HttpMethod.POST.name()))
            return true;
        RequestMatcher matcher = new OrRequestMatcher(getMatchers(freePassUri));
        return matcher.matches(request);
    }

    private boolean isNeedCooke(HttpServletRequest request) {
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

    private void addCookie(HttpServletResponse response, String redirectUrl) {
        response.addCookie(new Cookie("redirectUrl", redirectUrl));
    }
}
