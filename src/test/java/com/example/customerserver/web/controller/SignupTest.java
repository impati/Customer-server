package com.example.customerserver.web.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

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

import com.example.customerserver.service.ClientSteps;
import com.example.customerserver.service.client.ClientAdminister;

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
	void keycloakSignupPage() throws Exception {
		final String clientId = clientSteps.clientRegisterWithDefaultStep();

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
}
