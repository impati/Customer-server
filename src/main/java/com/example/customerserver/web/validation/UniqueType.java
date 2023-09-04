package com.example.customerserver.web.validation;

import lombok.Getter;

@Getter
public enum UniqueType {

    USERNAME("username"), EMAIL("email");

    private final String name;

    UniqueType(String name) {
        this.name = name;
    }
}
