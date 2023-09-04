package com.example.customerserver.service;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.example.customerserver.domain.Client;
import com.example.customerserver.repository.ClientRepository;
import com.example.customerserver.service.client.ClientAdminister;
import com.example.customerserver.web.request.ClientRedirectUrlRequest;

@SpringBootTest
@Transactional
class ClientAdministerTest {

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private ClientAdminister clientAdminister;

	private ClientSteps clientSteps;

	@BeforeEach
	private void setUp() {
		clientSteps = new ClientSteps(clientAdminister);
	}

	@Test
	@DisplayName("클라이언트 등록 테스트")
	void registerClientTest() {
		// given
		final String clientName = "service-hub";
		final String redirectUrl = "https://service-hub.org";

		// when
		final String clientId = clientSteps.clientRegisterWithDefaultStep(clientName, redirectUrl);

		// then
		final Optional<Client> client = clientRepository.findClientByClientId(clientId);
		assertThat(client).isPresent();
		assertThat(client.get().getClientName()).isEqualTo(clientName);
		assertThat(client.get().getRedirectUrl()).isEqualTo(redirectUrl);
	}

	@Test
	@DisplayName("클라이언트 리다이렉트 수정 테스트")
	void editClientRedirectUrl() {
		// given
		final String clientName = "service-hub";
		final String redirectUrl = "https://service-hub.org";
		final String clientId = clientSteps.clientRegisterWithDefaultStep(clientName, redirectUrl);
		final String newRedirectUrl = "https://naver.com";
		final ClientRedirectUrlRequest clientRedirectUrlRequest = new ClientRedirectUrlRequest(
			clientId,
			newRedirectUrl
		);

		// when
		clientAdminister.editRedirectUrl(clientRedirectUrlRequest);

		// then
		final Optional<Client> client = clientRepository.findClientByClientId(clientId);
		assertThat(client).isPresent();
		assertThat(client.get().getClientName()).isEqualTo(clientName);
		assertThat(client.get().getRedirectUrl()).isEqualTo(newRedirectUrl);
	}
}
