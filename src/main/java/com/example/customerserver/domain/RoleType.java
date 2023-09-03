package com.example.customerserver.domain;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleType {

	ADMIN("ROLE_ADMIN"),
	USER("ROLE_USER"),
	GUEST("ROLE_GUEST");

	private final String name;

	public static RoleType of(final String code) {
		return Arrays.stream(RoleType.values())
			.filter(role -> role.name.equals(code))
			.findFirst()
			.orElse(GUEST);
	}
}
