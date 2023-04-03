package com.example.customerserver.service.customer;

import com.example.customerserver.web.request.SignupRequest;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public class SignupManager {
    @Value("${spring.security.oauth2.client.provider.keycloak.token-uri}")
    private String accessTokenEndPointUrl;

    @Value("${spring.security.oauth2.client.registration.keycloak.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.keycloak.client-secret}")
    private String clientSecret;

    @Value("${keycloak.create.user}")
    private String createUserEndPoint;

    public void signup(SignupRequest signupRequest) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.setBearerAuth(returnAccessToken());
        restTemplate.postForEntity(createUserEndPoint,
                new HttpEntity<>(signUpFormMapping(signupRequest), headers), Void.class);

    }

    private String returnAccessToken() {

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        headers.add("Accept", "application/json");

        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("grant_type", AuthorizationGrantType.CLIENT_CREDENTIALS.getValue());
        parameters.add("client_id", clientId);
        parameters.add("client_secret", clientSecret);

        ResponseEntity<ClientCredentialsFlowResponse> response = restTemplate.postForEntity(
                accessTokenEndPointUrl, new HttpEntity<>(parameters, headers), ClientCredentialsFlowResponse.class);

        return response.getBody().getAccess_token();
    }

    private KeycloakSignupForm signUpFormMapping(SignupRequest signupRequest) {
        return new KeycloakSignupForm(signupRequest.getUsername(),
                signupRequest.getEmail(),
                signupRequest.getPassword());
    }

    @Data
    @NoArgsConstructor
    private static class KeycloakSignupForm {
        private String username;
        private String email;
        private boolean enabled = true;
        private List<Credential> credentials = new ArrayList<>();

        public KeycloakSignupForm(String username, String email, String password) {
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

        public Credential(String value) {
            this.value = value;
        }
    }


    @Data
    @NoArgsConstructor
    static class ClientCredentialsFlowResponse {
        private String access_token;
        private String token_type;
    }
}
