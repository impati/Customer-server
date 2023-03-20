package com.example.customerserver.exception;

public class InvalidCodeException extends CustomerException {

    private static final String STATUS_CODE = "400";
    private static final String CODE_NAME = "C005";
    private static final String MESSAGE = "유효하지 않은 코드입니다.";
    private static final String SOLUTION = "로그인을 통해 코드를 재발급 받아주세요.";

    public InvalidCodeException() {
        super(STATUS_CODE, CODE_NAME, MESSAGE, SOLUTION);
    }
}
