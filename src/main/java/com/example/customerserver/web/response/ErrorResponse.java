package com.example.customerserver.web.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import org.springframework.util.Assert;

import java.util.Map;

@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public record ErrorResponse(
        String codeName,
        String message,
        String solution,
        Map<String, String> validation
) {
    @Builder
    public ErrorResponse {
        Assert.hasText(message, "응답 메시지는 필수입니다.");
        Assert.hasText(solution, "응답 메시지는 필수입니다.");
    }

    public void addValidation(String code, String message) {
        validation.put(code, message);
    }
}
