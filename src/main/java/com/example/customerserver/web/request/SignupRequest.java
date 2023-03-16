package com.example.customerserver.web.request;

import com.example.customerserver.web.validation.KeycloakUnique;
import com.example.customerserver.web.validation.UniqueType;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public record SignupRequest(
        @KeycloakUnique(uniqueType = UniqueType.USERNAME)
        String username,
        @KeycloakUnique(uniqueType = UniqueType.EMAIL)
        @Email
        String email,
        @NotBlank
        @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}", message = "비밀번호는 영문 대,소문자와 숫자, 특수기호가 적어도 1개 이상씩 포함된 8자 ~ 20자의 비밀번호여야 합니다.")
        String password,
        @NotBlank
        String repeatPassword
) {
    public void validPassword() {
        if (!password.equals(repeatPassword)) throw new IllegalStateException();
    }
}
