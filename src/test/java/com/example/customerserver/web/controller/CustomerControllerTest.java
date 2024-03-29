package com.example.customerserver.web.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.example.customerserver.service.ClientSteps;
import com.example.customerserver.service.client.ClientAdminister;
import com.example.customerserver.web.data.SimplePrincipal;
import com.example.customerserver.web.request.CustomerEditRequest;
import com.example.customerserver.web.token.AccessTokenGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@Transactional
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
	@DisplayName("[POST] [/api/v1/customer] 사용자 정보 응답 테스트")
	void customerTest() throws Exception {
		final String clientId = clientSteps.clientRegisterWithDefaultStep();
		final String nickname = "test";
		final String username = "impati";
		final Long id = 1L;
		final String email = "yongs170@naver";
		final String accessToken = makeAccessToken(id, username, nickname, email);

		mockMvc.perform(post("/api/v1/customer")
				.header("Authorization", "Bearer " + accessToken))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(id))
			.andExpect(jsonPath("$.username").value(username))
			.andExpect(jsonPath("$.nickname").value(nickname))
			.andExpect(jsonPath("$.email").value(email))
			.andDo(print());
	}

	@Test
	@DisplayName("[POST] [/api/v1/customer] 사용자 정보 요청 시 엑세스 토큰을 누락한 경우")
	void hasNoAccessTokenWhenRequestCustomer() throws Exception {
		final String clientId = clientSteps.clientRegisterWithDefaultStep();

		mockMvc.perform(post("/api/v1/customer"))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.codeName").value("C002"))
			.andExpect(jsonPath("$.message").value("엑세스 토큰은 필수입니다."))
			.andExpect(jsonPath("$.solution").value("엑세스 토큰을 담아서 요청합니다."))
			.andDo(print());
	}

	@Test
	@DisplayName("[POST] [/api/v1/customer] 사용자 정보 요청 시 엑세스 토큰이 유효하지 않은 경우")
	void invalidAccessTokenWhenRequestCustomer() throws Exception {
		final String clientId = clientSteps.clientRegisterWithDefaultStep();
		final String accessToken = makeAccessToken(1L, "username", "nickname", "email") + "noise";

		mockMvc.perform(post("/api/v1/customer")
				.header("Authorization", "Bearer " + accessToken))
			.andExpect(status().isUnauthorized())
			.andExpect(jsonPath("$.codeName").value("C003"))
			.andExpect(jsonPath("$.message").value("엑세스 토큰이 유효하지 않습니다."))
			.andExpect(jsonPath("$.solution").value("엑세스 토큰을 재발급 받아야합니다."))
			.andDo(print());
	}

	@Test
	@DisplayName("[PATCH] [/api/v1/customer] 사용자 정보 수정 테스트")
	void customerEditTest() throws Exception {
		final String accessToken = makeAccessToken(1L, "username", "nickname", "email");
		final CustomerEditRequest customerEditRequest = new CustomerEditRequest();
		customerEditRequest.setNickname("impti");

		mockMvc.perform(post("/api/v1/customer")
				.header("Authorization", "Bearer " + accessToken)
				.content(objectMapper.writeValueAsString(customerEditRequest)))
			.andExpect(status().isOk())
			.andDo(print());
	}

	private String makeAccessToken(
		final Long id,
		final String username,
		final String nickname,
		final String email
	) throws JsonProcessingException {
		final SimplePrincipal principal = SimplePrincipal.builder()
			.id(id)
			.nickname(nickname)
			.username(username)
			.email(email)
			.build();
		return accessTokenGenerator.createToken(objectMapper.writeValueAsString(principal));
	}
}
