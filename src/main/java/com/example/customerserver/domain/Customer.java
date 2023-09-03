package com.example.customerserver.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.util.StringUtils;

import com.sun.istack.NotNull;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Customer extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "customer_id")
	private Long id;

	@Column(name = "sub", unique = true)
	@NotNull
	private String userId;

	@Column(name = "nickname", unique = true, nullable = false)
	private String nickname;

	@Column(name = "username", nullable = false)
	private String username;

	@Column(name = "email", unique = true, nullable = false)
	private String email;

	@Column(name = "role_type")
	@Enumerated(value = EnumType.STRING)
	private RoleType roleType;

	@Column(name = "provider_type")
	@Enumerated(EnumType.STRING)
	private ProviderType providerType;

	@Column(name = "blog_url")
	private String blogUrl;

	@Column(name = "profile_image_url")
	private String profileImageUrl;

	@Column(name = "introduce_comment", length = 1000)
	private String introduceComment;

	@Builder
	private Customer(
		final String userId,
		final String nickname,
		final String username,
		final String email,
		final String role,
		final ProviderType providerType,
		final String blogUrl,
		final String profileImageUrl
	) {
		this.userId = userId;
		this.nickname = nickname;
		this.username = username;
		this.email = email;
		this.roleType = RoleType.of(role);
		this.providerType = providerType;
		this.introduceComment = nickname + "님을 소개해주세요";
		this.profileImageUrl = profileImageUrl;
		this.blogUrl = blogUrl;
	}

	public void update(
		final String nickname,
		final String introduceComment,
		final String bolgUrl,
		final String profileImageUrl
	) {
		if (StringUtils.hasText(nickname)) {
			this.nickname = nickname;
		}
		if (StringUtils.hasText(introduceComment)) {
			this.introduceComment = introduceComment;
		}
		if (StringUtils.hasText(bolgUrl)) {
			this.blogUrl = bolgUrl;
		}
		if (StringUtils.hasText(profileImageUrl)) {
			this.profileImageUrl = profileImageUrl;
		}
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Customer customer)) {
			return false;
		}

		return this.getId() != null && Objects.equals(id, customer.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
