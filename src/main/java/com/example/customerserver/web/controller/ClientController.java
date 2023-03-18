package com.example.customerserver.web.controller;

import com.example.customerserver.service.ClientAdminister;
import com.example.customerserver.web.request.ClientRedirectUrlRequest;
import com.example.customerserver.web.request.ClientRequest;
import com.example.customerserver.web.response.ClientResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
public class ClientController {

    private final ClientAdminister clientAdminister;

    @PostMapping("/register")
    public ClientResponse registerClient(@RequestBody ClientRequest clientRequest) {
        String clientId = clientAdminister.registerClient(clientRequest);
        return new ClientResponse(clientId);
    }

    @PostMapping("/edit-redirect")
    public ResponseEntity<Void> editRedirectUrl(@RequestBody ClientRedirectUrlRequest request) {
        clientAdminister.editRedirectUrl(request);
        return ResponseEntity.ok().build();
    }
}
