package com.example.customerserver.web.request;

import lombok.Data;

@Data
public class CustomerEditRequest {
    private String nickname;
    private String blogUrl;
    private String profileUrl;
    private String introduceComment;
}
