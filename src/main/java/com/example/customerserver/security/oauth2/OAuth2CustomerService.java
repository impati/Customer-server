package com.example.customerserver.security.oauth2;

import com.example.customerserver.domain.Customer;
import com.example.customerserver.domain.ProviderType;
import com.example.customerserver.repository.CustomerRepository;
import com.example.customerserver.security.CustomerPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;
import java.util.Optional;

@Transactional
@RequiredArgsConstructor
public class OAuth2CustomerService extends DefaultOAuth2UserService {

    private final CustomerRepository clientRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        Map<String,Object> attributes = oAuth2User.getAttributes();

        ProviderCustomer providerCustomer = ProviderCustomerFactory.create(getProviderType(userRequest), oAuth2User.getAttributes());

        Optional<Customer> client = clientRepository.findByUserId(providerCustomer.getId());

        if(client.isPresent())
            return CustomerPrincipal.create(client.get(),attributes);

        return returnAfterSaveClient(providerCustomer,attributes);
    }

    private ProviderType getProviderType(OAuth2UserRequest userRequest){
        return ProviderType.valueOf(userRequest
                .getClientRegistration()
                .getRegistrationId()
                .toUpperCase());
    }

    private OAuth2User returnAfterSaveClient(ProviderCustomer providerUser,Map<String,Object> attributes){
        Customer client = providerUser.toCustomer();
        clientRepository.save(client);
        return CustomerPrincipal.create(client,attributes);
    }

}
