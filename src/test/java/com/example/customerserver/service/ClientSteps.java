package com.example.customerserver.service;

import com.example.customerserver.service.client.ClientAdminister;
import com.example.customerserver.web.request.ClientRequest;

public class ClientSteps {

	private final ClientAdminister clientAdminister;

	public ClientSteps(final ClientAdminister clientAdminister) {
		this.clientAdminister = clientAdminister;
	}

	public String clientRegisterWithDefaultStep() {
		final String clientName = "service-hub";
		final String redirectUrl = "https://service-hub.org";
		final ClientRequest clientRequest = new ClientRequest(clientName, redirectUrl);
		return clientAdminister.registerClient(clientRequest);
	}

	public String clientRegisterWithDefaultStep(final String clientName, final String redirectUrl) {
		final ClientRequest clientRequest = new ClientRequest(clientName, redirectUrl);
		return clientAdminister.registerClient(clientRequest);
	}
}
