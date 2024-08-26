package com.project.instagramclone.entity.test;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Entity
@Getter @Setter
public class OAuth2UserEntity {
    @Id
    @Column(name = "oauthId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="username", length = 50)
    private String username; // provider + provider id

    @Column(name = "name", length = 50)
    private String name; // 실제 이름

    @Column(name = "email", length = 50)
    private String email;

    @Column(name = "role", length = 50)
    private String role;

    @Builder
    public OAuth2UserEntity(String username, String email, String name, String role) {
        this.username = username;
        this.email = email;
        this.name = name;
        this.role = role;
    }
}