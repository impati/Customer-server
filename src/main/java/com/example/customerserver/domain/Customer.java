package com.example.customerserver.domain;

import com.sun.istack.NotNull;
import lombok.*;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.Objects;

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

    @Column(unique = true, nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Enumerated(value = EnumType.STRING)
    private RoleType roleType;

    @Enumerated(EnumType.STRING)
    private ProviderType providerType;

    private String blogUrl;

    private String profileImageUrl;

    @Column(length = 1000)
    private String introduceComment;

    @Builder
    private Customer(String userId, String nickname, String username, String email, String role, ProviderType providerType, String blogUrl, String profileImageUrl) {
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

    public void update(String nickname, String introduceComment, String bolgUrl, String profileImageUrl) {
        if (StringUtils.hasText(nickname)) this.nickname = nickname;
        if (StringUtils.hasText(introduceComment)) this.introduceComment = introduceComment;
        if (StringUtils.hasText(bolgUrl)) this.blogUrl = bolgUrl;
        if (StringUtils.hasText(profileImageUrl)) this.profileImageUrl = profileImageUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer customer)) return false;
        return this.getId() != null && Objects.equals(id, customer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
