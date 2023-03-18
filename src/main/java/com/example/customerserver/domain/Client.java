package com.example.customerserver.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Client extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String clientName;

    @Column(unique = true)
    private String clientId;

    private String redirectUrl;

    @Builder
    public Client(String clientName, String clientId, String redirectUrl) {
        this.clientName = clientName;
        this.clientId = clientId;
        this.redirectUrl = redirectUrl;
    }

    public void editRedirect(String newRedirectUrl) {
        this.redirectUrl = newRedirectUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Client client)) return false;
        return this.getId() != null && Objects.equals(id, client.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
