package com.example.customerserver.exception;

public class UnAuthenticationException extends CustomerException {

    private static final String STATUS_CODE = "401";
    private static final String CODE_NAME = "C004";
    private static final String MESSAGE = "인증되지 않은 요청입니다.";
    private static final String SOLUTION = "로그인을 해주세요.";

    public UnAuthenticationException() {
        super(STATUS_CODE, CODE_NAME, MESSAGE, SOLUTION);
    }
}
