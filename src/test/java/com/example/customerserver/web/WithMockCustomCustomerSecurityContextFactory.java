package com.example.customerserver.web;

import java.util.Collections;
import java.util.HashMap;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import com.example.customerserver.domain.ProviderType;
import com.example.customerserver.domain.RoleType;
import com.example.customerserver.security.CustomerPrincipal;

public class WithMockCustomCustomerSecurityContextFactory
	implements WithSecurityContextFactory<WithMockCustomCustomer> {

	private final static String prefix = "ROLE_";

	@Override
	public SecurityContext createSecurityContext(WithMockCustomCustomer customUser) {
		final String username = customUser.username();
		final String role = prefix + customUser.role();
		final CustomerPrincipal clientPrincipal = createCustomerPrincipalFrom(customUser);
		final Authentication authentication = UsernamePasswordAuthenticationToken.authenticated(
			clientPrincipal,
			clientPrincipal.getPassword(),
			clientPrincipal.getAuthorities()
		);
		final SecurityContext context = SecurityContextHolder.createEmptyContext();
		context.setAuthentication(authentication);
		return context;
	}

	private CustomerPrincipal createCustomerPrincipalFrom(WithMockCustomCustomer customer) {
		return new CustomerPrincipal(
			customer.id(),
			customer.username(),
			customer.name(),
			"test",
			ProviderType.KEYCLOAK,
			RoleType.of(customer.role()),
			Collections.singletonList(new SimpleGrantedAuthority(customer.role())),
			new HashMap<>(),
			"test",
			"test",
			"test"
		);
	}
}
