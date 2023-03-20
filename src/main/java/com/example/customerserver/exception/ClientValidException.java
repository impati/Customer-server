package com.example.customerserver.exception;

public class ClientValidException extends CustomerException {

    private static final String STATUS_CODE = "401";
    private static final String CODE_NAME = "C001";
    private static final String MESSAGE = "클라이언트 ID 가 유효하지 않습니다.";
    private static final String SOLUTION = "클라이언트를 등록하여 ID를 발급 받아야합니다.";

    public ClientValidException() {
        super(STATUS_CODE, CODE_NAME, MESSAGE, SOLUTION);
    }
}
