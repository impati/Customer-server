package com.example.customerserver.security.oauth2;

import java.util.Map;

import com.example.customerserver.domain.Customer;
import com.example.customerserver.domain.ProviderType;

public abstract class ProviderCustomer {

	protected final Map<String, Object> attributes;
	protected final ProviderType providerType;

	protected ProviderCustomer(final Map<String, Object> attributes, final ProviderType providerType) {
		this.attributes = attributes;
		this.providerType = providerType;
	}

	public abstract String getId();

	public abstract String getName();

	public abstract String getEmail();

	public abstract Customer toCustomer();

	protected String getAttributeByName(final String attributeName) {
		return attributes.get(attributeName).toString();
	}

	protected Map<String, Object> getUsernameAttribute(final String usernameAttribute) {
		try {
			return (Map<String, Object>)attributes.get(usernameAttribute);
		} catch (ClassCastException e) {
			throw new IllegalStateException("usernameAttribute 정보가 일치하지 않습니다");
		}
	}
}
