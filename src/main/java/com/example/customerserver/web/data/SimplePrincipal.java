package com.example.customerserver.web.data;

import com.example.customerserver.domain.ProviderType;
import com.example.customerserver.domain.RoleType;
import com.example.customerserver.security.CustomerPrincipal;

import lombok.Builder;

@Builder
public record SimplePrincipal(
	Long id,
	String username,
	String nickname,
	String email,
	ProviderType providerType,
	RoleType roleType,
	String blogUrl,
	String profileImageUrl,
	String introduceComment
) {

	public static SimplePrincipal from(final CustomerPrincipal customerPrincipal) {
		return SimplePrincipal.builder()
			.id(customerPrincipal.getId())
			.username(customerPrincipal.getUsername())
			.nickname(customerPrincipal.getNickname())
			.email(customerPrincipal.getEmail())
			.providerType(customerPrincipal.getProviderType())
			.roleType(customerPrincipal.getRoleType())
			.blogUrl(customerPrincipal.getBlogUrl())
			.profileImageUrl(customerPrincipal.getProfileImageUrl())
			.introduceComment(customerPrincipal.getIntroduceComment())
			.build();
	}
}
