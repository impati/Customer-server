package com.example.customerserver.web.controller;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.customerserver.service.customer.CustomerEditor;
import com.example.customerserver.web.data.SimplePrincipal;
import com.example.customerserver.web.request.CustomerEditRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CustomerController {

	private final CustomerEditor customerEditor;

	@PostMapping("/v1/customer")
	public SimplePrincipal getSimpleprincipal(final SimplePrincipal simplePrincipal) {
		return simplePrincipal;
	}

	@PatchMapping("/v1/customer")
	public void editCustomer(
		@RequestBody final CustomerEditRequest customerEditRequest,
		final SimplePrincipal simplePrincipal
	) {
		customerEditor.edit(simplePrincipal.id(), customerEditRequest);
	}
}
