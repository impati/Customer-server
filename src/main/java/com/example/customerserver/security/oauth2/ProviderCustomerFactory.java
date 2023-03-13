package com.example.customerserver.security.oauth2;

import com.example.customerserver.domain.ProviderType;

import java.util.Map;

public abstract class ProviderCustomerFactory {

    public static ProviderCustomer create(ProviderType providerType , Map<String, Object> attributes){
        switch (providerType){
            case KEYCLOAK: return new KeycloakCustomer(attributes,providerType);
            case KAKAO: return new KakaoCustomer(attributes,providerType);
            case NAVER: return new NaverCustomer(attributes,providerType);
            case GOOGLE: return new GoogleCustomer(attributes,providerType);
        }
        throw new IllegalStateException("Provider 타입을 잘못 넘겼습니다.");
    }

    private ProviderCustomerFactory(){
    }
}
