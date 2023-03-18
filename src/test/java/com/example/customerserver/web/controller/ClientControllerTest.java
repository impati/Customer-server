package com.example.customerserver.web.controller;

import com.example.customerserver.domain.Client;
import com.example.customerserver.repository.ClientRepository;
import com.example.customerserver.web.request.ClientRedirectUrlRequest;
import com.example.customerserver.web.request.ClientRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Client TEST")
@SpringBootTest
@AutoConfigureMockMvc
class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Client 등록 테스트")
    public void registerClientTest() throws Exception {

        mockMvc.perform(post("/client/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ClientRequest("service-hub", "https://service-hub.org"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clientId").exists())
                .andDo(MockMvcResultHandlers.print());

        assertThat(clientRepository.count())
                .isEqualTo(1);
    }

    @Test
    @DisplayName("Client Redirect Edit 테스트")
    @Transactional
    public void editClientRedirect() throws Exception {

        String clientId = UUID.randomUUID().toString();
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

}