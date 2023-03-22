package com.example.customerserver.service;

import com.example.customerserver.domain.Client;
import com.example.customerserver.repository.ClientRepository;
import com.example.customerserver.web.request.ClientRedirectUrlRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

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
    public void registerClientTest() throws Exception {
        // given
        String clientName = "service-hub";
        String redirectUrl = "https://service-hub.org";

        // when
        String clientId = clientSteps.clientRegisterWithDefaultStep(clientName, redirectUrl);

        // then
        Optional<Client> client = clientRepository.findClientByClientId(clientId);

        assertThat(client).isPresent();

        assertThat(client.get().getClientName())
                .isEqualTo(clientName);

        assertThat(client.get().getRedirectUrl())
                .isEqualTo(redirectUrl);

    }

    @Test
    @DisplayName("클라이언트 리다이렉트 수정 테스트")
    public void given_when_then() throws Exception {
        // given
        String clientName = "service-hub";
        String redirectUrl = "https://service-hub.org";

        String clientId = clientSteps.clientRegisterWithDefaultStep(clientName, redirectUrl);

        String newRedirectUrl = "https://naver.com";
        ClientRedirectUrlRequest clientRedirectUrlRequest =
                new ClientRedirectUrlRequest(clientId, newRedirectUrl);
        // when

        clientAdminister.editRedirectUrl(clientRedirectUrlRequest);
        // then

        Optional<Client> client = clientRepository.findClientByClientId(clientId);

        assertThat(client).isPresent();

        assertThat(client.get().getClientName())
                .isEqualTo(clientName);

        assertThat(client.get().getRedirectUrl())
                .isEqualTo(newRedirectUrl);

    }
}