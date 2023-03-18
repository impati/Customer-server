package com.example.customerserver.web.request;

import com.example.customerserver.domain.Client;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientRequest {
    private String clientName;
    private String redirectUrl;

    public Client toEntity() {
        return Client.builder()
                .clientId(UUID.randomUUID().toString())
                .clientName(clientName)
                .redirectUrl(redirectUrl)
                .build();
    }
}
