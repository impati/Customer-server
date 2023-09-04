package com.example.customerserver.security.oauth2;

import java.util.Map;
import java.util.Optional;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.transaction.annotation.Transactional;

import com.example.customerserver.domain.Customer;
import com.example.customerserver.domain.ProviderType;
import com.example.customerserver.repository.CustomerRepository;
import com.example.customerserver.security.CustomerPrincipal;

import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
public class OAuth2CustomerService extends DefaultOAuth2UserService {

	private final CustomerRepository clientRepository;

	@Override
	public OAuth2User loadUser(final OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		final OAuth2User oAuth2User = super.loadUser(userRequest);

		final Map<String, Object> attributes = oAuth2User.getAttributes();

		final ProviderCustomer providerCustomer = ProviderCustomerFactory.create(
			getProviderType(userRequest),
			oAuth2User.getAttributes()
		);

		final Optional<Customer> client = clientRepository.findByUserId(providerCustomer.getId());

		if (client.isPresent()) {
			return CustomerPrincipal.create(client.get(), attributes);
		}

		return returnAfterSaveClient(providerCustomer, attributes);
	}

	private ProviderType getProviderType(final OAuth2UserRequest userRequest) {
		return ProviderType.valueOf(userRequest
			.getClientRegistration()
			.getRegistrationId()
			.toUpperCase());
	}

	private OAuth2User returnAfterSaveClient(
		final ProviderCustomer providerUser,
		final Map<String, Object> attributes
	) {
		final Customer client = providerUser.toCustomer();
		clientRepository.save(client);
		return CustomerPrincipal.create(client, attributes);
	}
}
