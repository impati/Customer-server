package com.example.customerserver.security.handler;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.example.customerserver.config.AppConfig;

public class KeycloakLoginFailureHandler implements AuthenticationFailureHandler {

	private final RedirectStrategy redirectStrategy;
	private final String defaultFailureUrl = AppConfig.getHost() + "/auth/login?error=";

	public KeycloakLoginFailureHandler() {
		this.redirectStrategy = new DefaultRedirectStrategy();
	}

	@Override
	public void onAuthenticationFailure(
		final HttpServletRequest request,
		final HttpServletResponse response,
		final AuthenticationException exception
	) throws IOException {
		redirectStrategy.sendRedirect(request, response, defaultFailureUrl + "true");
	}
}
