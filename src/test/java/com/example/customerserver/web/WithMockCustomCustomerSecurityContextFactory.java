package com.example.customerserver.web;

import com.example.customerserver.domain.Customer;
import com.example.customerserver.domain.ProviderType;
import com.example.customerserver.domain.RoleType;
import com.example.customerserver.security.CustomerPrincipal;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class WithMockCustomCustomerSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomCustomer> {

    private final static String prefix = "ROLE_";

    @Override
    public SecurityContext createSecurityContext(WithMockCustomCustomer customUser) {
        String username = customUser.username();

        String role = prefix + customUser.role();

        CustomerPrincipal clientPrincipal = createCustomerPrincipalFrom(customUser);
        Authentication authentication = UsernamePasswordAuthenticationToken.authenticated(clientPrincipal,clientPrincipal.getPassword(),clientPrincipal.getAuthorities());
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        return context;
    }

    private CustomerPrincipal createCustomerPrincipalFrom(WithMockCustomCustomer customer){
        return new CustomerPrincipal(
                customer.id(),
                customer.username(),
                customer.name(),
                "test",
                ProviderType.KEYCLOAK,
                RoleType.of(customer.role()),
                Collections.singletonList(new SimpleGrantedAuthority(customer.role())),
                new HashMap<>(),
                "test","test","test"
        );
    }
}
