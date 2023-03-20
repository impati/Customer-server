package com.example.customerserver.exception;

public class HasNoAccessTokenException extends CustomerException {

    private static final String STATUS_CODE = "400";
    private static final String CODE_NAME = "C002";
    private static final String MESSAGE = "엑세스 토큰은 필수입니다.";
    private static final String SOLUTION = "엑세스 토큰을 담아서 요청합니다.";

    public HasNoAccessTokenException() {
        super(STATUS_CODE, CODE_NAME, MESSAGE, SOLUTION);
    }
}
