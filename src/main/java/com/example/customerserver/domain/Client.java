package com.example.customerserver.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Client extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "client_name")
	private String clientName;

	@Column(name = "client_id", unique = true)
	private String clientId;

	@Column(name = "redirect_url")
	private String redirectUrl;

	@Builder
	public Client(
		final String clientName,
		final String clientId,
		final String redirectUrl
	) {
		this.clientName = clientName;
		this.clientId = clientId;
		this.redirectUrl = redirectUrl;
	}

	public void editRedirect(final String newRedirectUrl) {
		this.redirectUrl = newRedirectUrl;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Client client)) {
			return false;
		}
		return this.getId() != null && Objects.equals(id, client.id);
	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}
}
