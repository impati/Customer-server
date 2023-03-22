package com.example.customerserver.web.controller;

import com.example.customerserver.service.ClientAdminister;
import com.example.customerserver.service.ClientSteps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class SignupTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClientAdminister clientAdminister;

    private ClientSteps clientSteps;

    @BeforeEach
    void setUp() {
        clientSteps = new ClientSteps(clientAdminister);
    }

    @Test
    @DisplayName("[GET] [/signup] keycloak 오픈소스 회원 가입 페이지 테스트")
    public void keycloakSignupPage() throws Exception {

        //then
        String clientId = clientSteps.clientRegisterWithDefaultStep();

        mockMvc.perform(get("/signup")
                        .param("clientId", clientId))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());

        mockMvc.perform(get("/signup")
                        .sessionAttr("clientId", clientId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("signupRequest"))
                .andExpect(MockMvcResultMatchers.view().name("signup"))
                .andDo(MockMvcResultHandlers.print());
    }

    //@Test  TODO : 다른 방법으로 테스트를 수행
    @DisplayName("[POST] [/signup] keycloak 오픈소스 회원 가입 테스트")
    public void keycloakSignup() throws Exception {

        mockMvc.perform(
                        post("/signup")
                                .param("username", "tester")
                                .param("email", "yongs170@naver.com")
                                .param("password", "test!234523@")
                                .param("repeatPassword", "test!234523@")
                )
                .andExpect(MockMvcResultMatchers.view().name("forward:/auth/login"))
                .andDo(MockMvcResultHandlers.print());

    }
}
