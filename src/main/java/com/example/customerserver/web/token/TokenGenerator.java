package com.example.customerserver.web.token;

public interface TokenGenerator {
    void afterPropertiesSet();
    String createToken(String authentication);
    boolean validateToken(String token);
    String getPrincipal(String token);
}
