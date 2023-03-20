package com.example.customerserver.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class CustomerException extends RuntimeException {

    private final String codeName;
    private final String statusCode;
    private final String message;
    private final String solution;
    private final Map<String, String> validate = new HashMap<>();

    public CustomerException(String statusCode, String codeName, String message, String solution) {
        this.statusCode = statusCode;
        this.codeName = codeName;
        this.message = message;
        this.solution = solution;
    }

    public int statusCode() {
        return Integer.parseInt(statusCode);
    }
}
