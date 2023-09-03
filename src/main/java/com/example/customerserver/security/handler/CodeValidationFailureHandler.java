package com.example.customerserver.security.handler;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.stereotype.Component;

import com.example.customerserver.config.AppConfig;

@Component
public class CodeValidationFailureHandler {

	private static final String DEFAULT_URI = "/error/code";

	private final RedirectStrategy redirectStrategy;

	public CodeValidationFailureHandler() {
		this.redirectStrategy = new DefaultRedirectStrategy();
	}

	public void onClientValidFailure(
		final HttpServletRequest request,
		final HttpServletResponse response
	) throws IOException {
		redirectStrategy.sendRedirect(request, response, AppConfig.getHost() + DEFAULT_URI);
	}
}
