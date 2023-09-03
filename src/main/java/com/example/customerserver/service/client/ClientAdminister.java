package com.example.customerserver.service.client;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.customerserver.domain.Client;
import com.example.customerserver.exception.ClientValidException;
import com.example.customerserver.repository.ClientRepository;
import com.example.customerserver.web.request.ClientRedirectUrlRequest;
import com.example.customerserver.web.request.ClientRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ClientAdminister {

	private final ClientRepository clientRepository;

	public String registerClient(final ClientRequest clientRequest) {
		final Client client = clientRepository.save(clientRequest.toEntity());
		return client.getClientId();
	}

	public void editRedirectUrl(final ClientRedirectUrlRequest request) {
		final Client client = clientRepository.findClientByClientId(request.getClientId())
			.orElseThrow(ClientValidException::new);
		client.editRedirect(request.getRedirectUrl());
	}
}
