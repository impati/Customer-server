package com.example.customerserver.web.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.example.customerserver.web.validation.KeycloakUnique;
import com.example.customerserver.web.validation.UniqueType;

import lombok.Data;

@Data
public class SignupRequest {

    @KeycloakUnique(uniqueType = UniqueType.USERNAME)
    private String username;

    @KeycloakUnique(uniqueType = UniqueType.EMAIL)
    @Email
    private String email;

    @NotBlank
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}", message = "비밀번호는 영문 대,소문자와 숫자, 특수기호가 적어도 1개 이상씩 포함된 8자 ~ 20자의 비밀번호여야 합니다.")
    private String password;

    @NotBlank
    private String repeatPassword;

    public boolean isSamePassword() {
        return password.equals(repeatPassword);
    }
}
