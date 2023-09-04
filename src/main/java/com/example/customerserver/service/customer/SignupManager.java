package com.example.customerserver.service.customer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.example.customerserver.web.request.SignupRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class SignupManager {

	@Value("${spring.security.oauth2.client.provider.keycloak.token-uri}")
	private String accessTokenEndPointUrl;

	@Value("${spring.security.oauth2.client.registration.keycloak.client-id}")
	private String clientId;

	@Value("${spring.security.oauth2.client.registration.keycloak.client-secret}")
	private String clientSecret;

	@Value("${keycloak.create.user}")
	private String createUserEndPoint;

	public void signup(final SignupRequest signupRequest) {
		final RestTemplate restTemplate = new RestTemplate();
		final HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		headers.setBearerAuth(returnAccessToken());

		restTemplate.postForEntity(
			createUserEndPoint,
			new HttpEntity<>(signUpFormMapping(signupRequest), headers),
			Void.class
		);
	}

	private String returnAccessToken() {
		final RestTemplate restTemplate = new RestTemplate();
		final HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/x-www-form-urlencoded");
		headers.add("Accept", "application/json");
		final MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
		parameters.add("grant_type", AuthorizationGrantType.CLIENT_CREDENTIALS.getValue());
		parameters.add("client_id", clientId);
		parameters.add("client_secret", clientSecret);

		ResponseEntity<ClientCredentialsFlowResponse> response = restTemplate.postForEntity(
			accessTokenEndPointUrl,
			new HttpEntity<>(parameters, headers),
			ClientCredentialsFlowResponse.class
		);

		return response.getBody().getAccess_token();
	}

	private KeycloakSignupForm signUpFormMapping(final SignupRequest signupRequest) {
		return new KeycloakSignupForm(signupRequest.getUsername(),
			signupRequest.getEmail(),
			signupRequest.getPassword()
		);
	}

	@Data
	@NoArgsConstructor
	private static class KeycloakSignupForm {

		private String username;
		private String email;
		private boolean enabled = true;
		private List<Credential> credentials = new ArrayList<>();

		public KeycloakSignupForm(final String username, final String email, final String password) {
			this.username = username;
			this.email = email;
			credentials.add(new Credential(password));
		}
	}

	@Data
	@NoArgsConstructor
	static class Credential {
		private String type = "password";
		private String value;
		private boolean temporary = false;

		public Credential(final String value) {
			this.value = value;
		}
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	static class ClientCredentialsFlowResponse {
		private String access_token;
		private String token_type;
	}
}
