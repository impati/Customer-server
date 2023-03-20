package com.example.customerserver.security.handler;

import com.example.customerserver.config.AppConfig;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class ClientValidationFailureHandler {

    private static final String DEFAULT_URI = "/error/client";

    private final RedirectStrategy redirectStrategy;

    public ClientValidationFailureHandler() {
        this.redirectStrategy = new DefaultRedirectStrategy();
    }

    public void onClientValidFailure(HttpServletRequest request, HttpServletResponse response) throws IOException {
        redirectStrategy.sendRedirect(request, response, AppConfig.getHost() + DEFAULT_URI);
    }
}
