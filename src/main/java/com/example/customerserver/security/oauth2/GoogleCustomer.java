package com.example.customerserver.security.oauth2;

import java.util.Map;

import com.example.customerserver.config.AppConfig;
import com.example.customerserver.domain.Customer;
import com.example.customerserver.domain.ProviderType;

public class GoogleCustomer extends ProviderCustomer {

	protected GoogleCustomer(final Map<String, Object> attributes, final ProviderType providerType) {
		super(attributes, providerType);
	}

	@Override
	public String getId() {
		return getAttributeByName("sub");
	}

	@Override
	public String getName() {
		return getAttributeByName("name");
	}

	@Override
	public String getEmail() {
		return getAttributeByName("email");
	}

	@Override
	public Customer toCustomer() {
		return Customer.builder()
			.userId(getId())
			.username(getName())
			.nickname(getEmail())
			.email(getEmail())
			.role("ROLE_USER")
			.providerType(providerType)
			.blogUrl(AppConfig.getHost() + "/client")
			.profileImageUrl(AppConfig.getHost() + "/profile/default.png")
			.build();
	}
}
