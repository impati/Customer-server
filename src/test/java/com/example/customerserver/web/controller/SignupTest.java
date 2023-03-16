package com.example.customerserver.web.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
public class SignupTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("keycloak 오픈소스 회원 가입 페이지 테스트")
    public void keycloakSignupPage() throws Exception {

        mockMvc.perform(get("/auth/signup")
                        .param("redirectUrl", "https://service-hub.org"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("signupRequest"))
                .andExpect(MockMvcResultMatchers.view().name("signup"))
                .andDo(MockMvcResultHandlers.print());
    }

    //@Test 다른 방식의 테스트를 생각해볼 것.
    @DisplayName("keycloak 오픈소스 회원 가입 테스트")
    public void keycloakSignup() throws Exception {

        mockMvc.perform(
                        post("/auth/signup")
                                .param("username", "tester")
                                .param("email", "yongs170@naver.com")
                                .param("password", "test!234523@")
                                .param("repeatPassword", "test!234523@")
                )
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/auth/login"))
                .andDo(MockMvcResultHandlers.print());

    }
}
