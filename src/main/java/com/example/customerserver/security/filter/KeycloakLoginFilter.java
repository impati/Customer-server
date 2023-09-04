package com.example.customerserver.security.filter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.ClientAuthorizationException;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.example.customerserver.security.oauth2.OAuth2CustomerService;

public class KeycloakLoginFilter extends AbstractAuthenticationProcessingFilter {

	private static final String DEFAULT_FILTER_PROCESSES_URI = "/password/keycloak/login/**";
	private static final String REGISTRATION_ID = "keycloak";

	private final DefaultOAuth2AuthorizedClientManager oAuth2AuthorizedClientManager;
	private final OAuth2CustomerService oAuth2CustomerService;

	public KeycloakLoginFilter(
		final DefaultOAuth2AuthorizedClientManager oAuth2AuthorizedClientManager,
		final OAuth2CustomerService customOAuth2UserService
	) {
		super(DEFAULT_FILTER_PROCESSES_URI);
		this.oAuth2AuthorizedClientManager = oAuth2AuthorizedClientManager;
		this.oAuth2CustomerService = customOAuth2UserService;
	}

	@Override
	public Authentication attemptAuthentication(
		final HttpServletRequest request,
		final HttpServletResponse response
	) throws AuthenticationException, ServletException, IOException {
		try {
			return attemptAuthenticationOrThrow(request, response);
		} catch (ClientAuthorizationException e) {
			getFailureHandler().onAuthenticationFailure(
				request,
				response,
				new InsufficientAuthenticationException("invalid username or password")
			);
		}
		return null;
	}

	private Authentication attemptAuthenticationOrThrow(
		final HttpServletRequest request,
		final HttpServletResponse response
	) {
		final OAuth2AuthorizedClient oAuth2AuthorizedClient = getOAuth2AuthorizedClient(request, response);
		if (isExist(oAuth2AuthorizedClient)) {
			OAuth2User oauth2User = oAuth2CustomerService.loadUser(new OAuth2UserRequest(
				oAuth2AuthorizedClient.getClientRegistration(),
				oAuth2AuthorizedClient.getAccessToken())
			);
			return new OAuth2AuthenticationToken(oauth2User, oauth2User.getAuthorities(), REGISTRATION_ID);
		}
		throw new ClientAuthorizationException(new OAuth2Error("keycloak Error"), REGISTRATION_ID);
	}

	private OAuth2AuthorizedClient getOAuth2AuthorizedClient(
		final HttpServletRequest request,
		final HttpServletResponse response
	) {
		return oAuth2AuthorizedClientManager.authorize(OAuth2AuthorizeRequest
			.withClientRegistrationId(REGISTRATION_ID)
			.principal(SecurityContextHolder.getContext().getAuthentication())
			.attribute(HttpServletRequest.class.getName(), request)
			.attribute(HttpServletResponse.class.getName(), response)
			.build());
	}

	private boolean isExist(final OAuth2AuthorizedClient oAuth2AuthorizedClient) {
		return oAuth2AuthorizedClient != null;
	}

	@Override
	public void setAuthenticationFailureHandler(final AuthenticationFailureHandler failureHandler) {
		super.setAuthenticationFailureHandler(failureHandler);
	}
}
