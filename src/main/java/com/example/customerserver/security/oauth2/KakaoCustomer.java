package com.example.customerserver.security.oauth2;

import com.example.customerserver.config.AppConfig;
import com.example.customerserver.domain.Customer;
import com.example.customerserver.domain.ProviderType;

import java.util.Map;

public class KakaoCustomer extends ProviderCustomer{

    protected KakaoCustomer(Map<String, Object> attributes, ProviderType providerType) {
        super(attributes, providerType);
    }

    @Override
    public String getId() {
        return getAttributeByName("id");
    }

    @Override
    public String getName() {
        return getProfile().get("nickname").toString();
    }

    @Override
    public String getEmail() {
        return getKakaoAccount().get("email").toString();
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


    private Map<String,Object> getProfile(){
        try {
            return (Map<String, Object>) getKakaoAccount().get("profile");
        }catch (ClassCastException e){
            throw new IllegalStateException("usernameAttribute 정보가 일치하지 않습니다");
        }
    }

    private Map<String,Object> getKakaoAccount(){
        try {
            return (Map<String, Object>) attributes.get("kakao_account");
        }catch (ClassCastException e){
            throw new IllegalStateException("usernameAttribute 정보가 일치하지 않습니다");
        }
    }
}
