package com.example.customerserver.security.oauth2;

import com.example.customerserver.config.AppConfig;
import com.example.customerserver.domain.Customer;
import com.example.customerserver.domain.ProviderType;

import java.util.Map;

public class NaverCustomer extends ProviderCustomer {

    protected NaverCustomer(Map<String, Object> attributes, ProviderType providerType) {
        super(attributes, providerType);
    }

    @Override
    public String getId() {
        return getAttributeByName("id");
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

    @Override
    protected String getAttributeByName(String attributeName) {
        return getUsernameAttribute("response").get(attributeName).toString();
    }
}

