package com.example.customerserver.web.controller;

import com.example.customerserver.config.AppConfig;
import com.example.customerserver.domain.Customer;
import com.example.customerserver.domain.ProviderType;
import com.example.customerserver.repository.CustomerRepository;
import com.example.customerserver.security.filter.ClientIdValidationFilter;
import com.example.customerserver.security.filter.CodeValidationFilter;
import com.example.customerserver.service.ClientSteps;
import com.example.customerserver.service.client.ClientAdminister;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("AuthController 테스트")
@SpringBootTest
@Transactional
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
    @Autowired
    private AuthController authController;
    @Autowired
    private ExceptionController exceptionController;
    @Autowired
    private ClientIdValidationFilter clientIdValidationFilter;
    @Autowired
    private ClientAdminister clientAdminister;
    private ClientSteps clientSteps;
    private String redirectUrl;


    @BeforeEach
    void setUp() {
        clientSteps = new ClientSteps(clientAdminister);
        redirectUrl = "https://service-hub.org";
        mockMvc = MockMvcBuilders
                .standaloneSetup(authController, exceptionController)
                .setCustomArgumentResolvers(new CodeArgumentResolver(codeGenerator), new AccessTokenArgumentResolver(accessTokenGenerator, objectMapper))
                .setControllerAdvice(new CustomerExceptionHandler())
                .addFilter(codeValidationFilter)
                .addFilter(clientIdValidationFilter)
                .build();
    }

    @Test
    @DisplayName("[GET] [/auth/login] 로그인 테스트")
    @WithMockCustomCustomer
    public void loginTest() throws Exception {

        //given
        String clientId = clientSteps.clientRegisterWithDefaultStep();

        //then
        mockMvc.perform(get("/auth/login")
                        .param("clientId", clientId)
                )
                .andExpect(status().is3xxRedirection())
                .andDo(print());

        mockMvc.perform(get("/auth/login")
                        .sessionAttr("clientId", clientId)
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("signin"))
                .andExpect(MockMvcResultMatchers.cookie().exists("redirectUrl"))
                .andDo(print());

    }


    @Test
    @DisplayName("[GET [/auth/code] code 와 함께 redirect 테스트")
    @WithMockCustomCustomer
    public void redirectWithCodeTest() throws Exception {
        mockMvc.perform(get("/auth/code")
                        .cookie(new Cookie("redirectUrl", redirectUrl)))
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:" + redirectUrl))
                .andDo(print());
    }

    @Test
    @DisplayName("[GET] [/auth/code] 인증되지 않은 요청 테스트")
    public void isUnauthorizedRequest() throws Exception {

        mockMvc.perform(get("/auth/code")
                        .cookie(new Cookie("redirectUrl", redirectUrl)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.codeName").value("C004"))
                .andExpect(jsonPath("$.message").value("인증되지 않은 요청입니다."))
                .andExpect(jsonPath("$.solution").value("로그인을 해주세요."))
                .andDo(print());
    }

    @Test
    @DisplayName("[POST] [/auth/gettoken] 인가 코드를 검증하고 엑세스 토큰을 발급한다.")
    @WithMockCustomCustomer
    @Transactional
    public void validCodeAnd() throws Exception {

        //given
        Customer customer = saveCustomer();
        String code = codeGenerator.createToken(String.valueOf(customer.getId()));
        String clientId = clientSteps.clientRegisterWithDefaultStep();

        //then
        mockMvc.perform(post("/auth/gettoken")
                        .header("Authorization", code))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andDo(print());
    }

    @Test
    @DisplayName("[POST] [/auth/gettoken] 인가 코드 검증 실패 테스트")
    @Transactional
    public void CodeValidFail() throws Exception {
        //given
        Customer customer = saveCustomer();
        String code = codeGenerator.createToken(String.valueOf(customer.getId())) + "noisy";
        String clientId = clientSteps.clientRegisterWithDefaultStep();

        //then
        mockMvc.perform(post("/auth/gettoken")
                        .header("Authorization", code))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(AppConfig.getHost() + "/error/code"));


        mockMvc.perform(get("/error/code"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.codeName").value("C005"))
                .andExpect(jsonPath("$.message").value("유효하지 않은 코드입니다."))
                .andExpect(jsonPath("$.solution").value("로그인을 통해 코드를 재발급 받아주세요."));

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