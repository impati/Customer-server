package com.example.customerserver.web.controller;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import com.example.customerserver.config.AppConfig;
import com.example.customerserver.domain.Client;
import com.example.customerserver.repository.ClientRepository;
import com.example.customerserver.web.request.ClientRedirectUrlRequest;
import com.example.customerserver.web.request.ClientRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

@DisplayName("Client TEST")
@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("[POST] [/client/register] Client 등록 테스트")
    void registerClientTest() throws Exception {
        final long previous = clientRepository.count();

        mockMvc.perform(post("/client/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new ClientRequest("service-hub", "https://service-hub.org"))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.clientId").exists())
            .andDo(MockMvcResultHandlers.print());

        assertThat(clientRepository.count()).isEqualTo(previous + 1);
    }

    @Test
    @DisplayName("[POST] [client/edit-redirect] Client Redirect Edit 테스트")
    @Transactional
    void editClientRedirect() throws Exception {
        final String clientId = UUID.randomUUID().toString();
        clientRepository.save(Client.builder()
            .redirectUrl("https://service-hub.org")
            .clientName("service-hub")
            .clientId(clientId).build());

        mockMvc.perform(post("/client/edit-redirect")
                .header("clientId", clientId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new ClientRedirectUrlRequest(clientId, "https://naver.com"))))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("[POST] [client/edit-redirect] Client Redirect Edit 테스트 시 유효하지 않는 client Id 가 넘어온 경우")
    void invalidClientIdWhenEditingRedirect() throws Exception {
        final String clientId = UUID.randomUUID().toString();

        mockMvc.perform(post("/client/edit-redirect")
                .header("clientId", clientId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new ClientRedirectUrlRequest(clientId, "https://naver.com"))))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl(AppConfig.getHost() + "/error/client"));

        mockMvc.perform(get("/error/client"))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.codeName").value("C001"))
            .andExpect(jsonPath("$.message").value("클라이언트 ID 가 유효하지 않습니다."))
            .andExpect(jsonPath("$.solution").value("클라이언트를 등록하여 ID를 발급 받아야합니다."));

    }
}
