package com.example.customerserver.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum RoleType {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER"),
    GUEST("ROLE_GUEST");

    private final String name;

    public static RoleType of(String code){
        return Arrays.stream(RoleType.values())
                .filter(role -> role.name.equals(code))
                .findFirst()
                .orElse(GUEST);
    }
}