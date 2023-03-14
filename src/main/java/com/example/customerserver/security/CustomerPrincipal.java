package com.example.customerserver.security;

import com.example.customerserver.domain.Customer;
import com.example.customerserver.domain.ProviderType;
import com.example.customerserver.domain.RoleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerPrincipal implements OAuth2User, UserDetails {

    private Long id;
    private String username;
    private String nickname;
    private String email;
    private ProviderType providerType;
    private RoleType roleType;
    private Collection<GrantedAuthority> authorities;
    private Map<String, Object> attributes;
    private String blogUrl;
    private String profileImageUrl;
    private String introduceComment;

    private CustomerPrincipal(Long id, String username, String nickname,
                              String email, ProviderType providerType, RoleType roleType,
                              Collection<GrantedAuthority> authorities, String blogUrl, String profileImageUrl, String introduceComment) {
        this.id = id;
        this.username = username;
        this.nickname = nickname;
        this.email = email;
        this.providerType = providerType;
        this.roleType = roleType;
        this.authorities = authorities;
        this.blogUrl = blogUrl;
        this.profileImageUrl = profileImageUrl;
        this.introduceComment = introduceComment;
    }

    public static CustomerPrincipal create(Customer customer) {
        return new CustomerPrincipal(
                customer.getId(),
                customer.getUsername(),
                customer.getNickname(),
                customer.getEmail(),
                customer.getProviderType(),
                customer.getRoleType(),
                Collections.singletonList(new SimpleGrantedAuthority(customer.getRoleType().getName())),
                customer.getBlogUrl(),
                customer.getProfileImageUrl(),
                customer.getIntroduceComment()
        );
    }

    public static CustomerPrincipal create(Customer customer, Map<String, Object> attributes) {
        CustomerPrincipal customerPrincipal = create(customer);
        customerPrincipal.setAttributes(attributes);
        return customerPrincipal;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    private void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getName() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }

    public void editNickname(String nickname) {
        this.nickname = nickname;
    }
}
