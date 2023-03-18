package com.example.customerserver.service;

import com.example.customerserver.domain.Client;
import com.example.customerserver.repository.ClientRepository;
import com.example.customerserver.web.request.ClientRedirectUrlRequest;
import com.example.customerserver.web.request.ClientRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ClientAdminister {

    private final ClientRepository clientRepository;

    public String registerClient(ClientRequest clientRequest) {
        Client client = clientRepository.save(clientRequest.toEntity());
        return client.getClientId();
    }

    public void editRedirectUrl(ClientRedirectUrlRequest request) {
        Client client = clientRepository.findClientByClientId(request.getClientId())
                .orElseThrow(IllegalStateException::new);
        client.editRedirect(request.getRedirectUrl());
    }

}
