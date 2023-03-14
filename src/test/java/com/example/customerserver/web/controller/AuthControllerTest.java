package com.example.customerserver.web.controller;

import com.example.customerserver.web.WithMockCustomCustomer;
import com.example.customerserver.web.argument.CodeArgumentResolver;
import com.example.customerserver.web.config.TokenConfig;
import com.example.customerserver.web.config.WebConfig;
import com.example.customerserver.web.token.TokenGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.Cookie;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@DisplayName("AuthController 테스트")
@WebMvcTest
@Import({WebConfig.class, TokenConfig.class})
class AuthControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private TokenGenerator tokenGenerator;

    private String redirectUrl;

    @BeforeEach
    void setUp() {
        redirectUrl = "https://service-hub.org";
        mockMvc = MockMvcBuilders
                .standaloneSetup(AuthController.class)
                .setCustomArgumentResolvers(new CodeArgumentResolver(tokenGenerator))
                .build();
    }

    @Test
    @DisplayName("로그인 테스트")
    @WithMockCustomCustomer
    public void loginTest() throws Exception {

        mockMvc.perform(get("/auth/login")
                        .param("redirectUrl", redirectUrl))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("signin"))
                .andExpect(MockMvcResultMatchers.cookie().exists("redirectUrl"))
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    @DisplayName("code 와 함께 redirect 테스트")
    @WithMockCustomCustomer
    public void redirectWithCodeTest() throws Exception {
        mockMvc.perform(post("/auth/code")
                        .cookie(new Cookie("redirectUrl", redirectUrl)))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:" + redirectUrl))
                .andDo(MockMvcResultHandlers.print());
    }

}