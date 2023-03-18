package com.example.customerserver.web.controller;

import com.example.customerserver.service.ClientAdminister;
import com.example.customerserver.service.ClientSteps;
import com.example.customerserver.web.data.SimplePrincipal;
import com.example.customerserver.web.token.AccessTokenGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AccessTokenGenerator accessTokenGenerator;

    @Autowired
    private ClientAdminister clientAdminister;

    private ClientSteps clientSteps;

    @BeforeEach
    void setUp() {
        clientSteps = new ClientSteps(clientAdminister);
    }


    @Test
    @DisplayName("사용자 정보 응답 테스트")
    public void customerTest() throws Exception {

        String clientId = clientSteps.clientRegisterWithDefaultStep();

        String nickname = "test";
        String username = "impati";
        Long id = 1L;
        String email = "yongs170@naver";

        String accessToken = makeAccessToken(id, username, nickname, email);

        mockMvc.perform(post("/api/v1/customer")
                        .header("Authorization", "Bearer " + accessToken)
                        .header("clientId", clientId)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.username").value(username))
                .andExpect(jsonPath("$.nickname").value(nickname))
                .andExpect(jsonPath("$.email").value(email))
                .andDo(print());
    }

    private String makeAccessToken(Long id, String username, String nickname, String email) throws JsonProcessingException {
        SimplePrincipal principal = SimplePrincipal.builder()
                .id(id)
                .nickname(nickname)
                .username(username)
                .email(email)
                .build();
        return accessTokenGenerator.createToken(objectMapper.writeValueAsString(principal));
    }
}