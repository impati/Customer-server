package com.example.customerserver.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.customerserver.exception.CustomerException;
import com.example.customerserver.web.response.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class CustomerExceptionHandler {

	@ResponseBody
	@ExceptionHandler(CustomerException.class)
	public ResponseEntity<ErrorResponse> customerException(final CustomerException e) {
		final ErrorResponse body = ErrorResponse.builder()
			.message(e.getMessage())
			.codeName(e.getCodeName())
			.solution(e.getSolution())
			.build();

		return ResponseEntity.status(e.statusCode()).body(body);
	}
}
