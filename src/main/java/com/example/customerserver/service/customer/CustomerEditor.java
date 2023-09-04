package com.example.customerserver.service.customer;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.customerserver.config.AppConfig;
import com.example.customerserver.domain.Customer;
import com.example.customerserver.repository.CustomerRepository;
import com.example.customerserver.service.customer.profile.ProfileManager;
import com.example.customerserver.web.request.CustomerEditRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerEditor {

	private final CustomerRepository customerRepository;
	private final ProfileManager profileManager;

	public void edit(final Long customerId, final CustomerEditRequest customerEditRequest) {
		final Customer customer = customerRepository.findById(customerId)
			.orElseThrow(IllegalStateException::new);

		final String storeName = profileManager.download(customerEditRequest.getProfileUrl());

		customer.update(
			customerEditRequest.getNickname(),
			customerEditRequest.getIntroduceComment(),
			customerEditRequest.getBlogUrl(),
			convertImageUrl(customer, storeName)
		);
	}

	private String convertImageUrl(final Customer customer, final String storeName) {
		if (storeName.equals("default.png")) {
			return customer.getProfileImageUrl();
		}

		return AppConfig.getHost() + "/file/profile/" + storeName;
	}
}
