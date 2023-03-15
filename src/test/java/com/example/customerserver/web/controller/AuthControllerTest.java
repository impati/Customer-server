package com.example.customerserver.web.controller;

import com.example.customerserver.domain.Customer;
import com.example.customerserver.domain.ProviderType;
import com.example.customerserver.repository.CustomerRepository;
import com.example.customerserver.security.filter.CodeValidationFilter;
import com.example.customerserver.web.WithMockCustomCustomer;
import com.example.customerserver.web.argument.AccessTokenArgumentResolver;
import com.example.customerserver.web.argument.CodeArgumentResolver;
import com.example.customerserver.web.token.AccessTokenGenerator;
import com.example.customerserver.web.token.CodeGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.Cookie;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@DisplayName("AuthController 테스트")
@SpringBootTest
class AuthControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private CodeValidationFilter codeValidationFilter;
    @Autowired
    private CodeGenerator codeGenerator;
    @Autowired
    private AccessTokenGenerator accessTokenGenerator;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CustomerRepository customerRepository;

    private String redirectUrl;

    @BeforeEach
    void setUp() {
        redirectUrl = "https://service-hub.org";
        mockMvc = MockMvcBuilders
                .standaloneSetup(AuthController.class)
                .setCustomArgumentResolvers(new CodeArgumentResolver(codeGenerator))
                .setCustomArgumentResolvers(new AccessTokenArgumentResolver(codeGenerator, accessTokenGenerator, objectMapper))
                .addFilter(codeValidationFilter)
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

    @Test
    @DisplayName("인가 코드를 검증하고 엑세스 토큰을 발급한다.")
    @WithMockCustomCustomer
    public void validCodeAnd() throws Exception {

        Customer customer = saveCustomer();

        String code = codeGenerator.createToken(String.valueOf(customer.getId()));

        mockMvc.perform(post("/auth/gettoken").header("Authorization", code))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accessToken").exists())
                .andDo(MockMvcResultHandlers.print());
    }


    private Customer saveCustomer() {
        Customer customer = Customer.builder()
                .userId("123")
                .username("test")
                .providerType(ProviderType.KEYCLOAK)
                .nickname("test")
                .email("test")
                .profileImageUrl("test")
                .blogUrl("test")
                .role("ADMIN")
                .build();
        customerRepository.save(customer);
        return customer;
    }
}