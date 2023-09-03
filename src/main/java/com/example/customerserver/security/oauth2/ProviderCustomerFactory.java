package com.example.customerserver.security.oauth2;

import java.util.Map;

import com.example.customerserver.domain.ProviderType;

public abstract class ProviderCustomerFactory {

	private ProviderCustomerFactory() {
	}

	public static ProviderCustomer create(final ProviderType providerType, final Map<String, Object> attributes) {
		return switch (providerType) {
			case KEYCLOAK -> new KeycloakCustomer(attributes, providerType);
			case KAKAO -> new KakaoCustomer(attributes, providerType);
			case NAVER -> new NaverCustomer(attributes, providerType);
			case GOOGLE -> new GoogleCustomer(attributes, providerType);
		};
	}
}
