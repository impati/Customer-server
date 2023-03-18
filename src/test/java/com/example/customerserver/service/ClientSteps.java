package com.example.customerserver.service;

import com.example.customerserver.web.request.ClientRequest;

public class ClientSteps {

    private final ClientAdminister clientAdminister;

    public ClientSteps(ClientAdminister clientAdminister) {
        this.clientAdminister = clientAdminister;
    }

    public String clientRegisterWithDefaultStep() {
        String clientName = "service-hub";
        String redirectUrl = "https://service-hub.org";
        ClientRequest clientRequest = new ClientRequest(clientName, redirectUrl);
        return clientAdminister.registerClient(clientRequest);
    }

    public String clientRegisterWithDefaultStep(String clientName, String redirectUrl) {
        ClientRequest clientRequest = new ClientRequest(clientName, redirectUrl);
        return clientAdminister.registerClient(clientRequest);
    }


}
