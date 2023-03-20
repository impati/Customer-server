package com.example.customerserver.exception;

public class InvalidAccessTokenException extends CustomerException {

    private static final String STATUS_CODE = "401";
    private static final String CODE_NAME = "C003";
    private static final String MESSAGE = "엑세스 토큰이 유효하지 않습니다.";
    private static final String SOLUTION = "엑세스 토큰을 재발급 받아야합니다.";

    public InvalidAccessTokenException() {
        super(STATUS_CODE, CODE_NAME, MESSAGE, SOLUTION);
    }
}
