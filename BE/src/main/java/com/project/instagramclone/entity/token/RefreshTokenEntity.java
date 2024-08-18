package com.project.instagramclone.entity.token;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// @Table(name = "refreshToken")
@Getter
@Setter
@Entity
@NoArgsConstructor
public class RefreshTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String refresh;

    private String expiration;

    @Builder
    public RefreshTokenEntity(String username, String refresh, String expiration) {
        this.username = username;
        this.refresh = refresh;
        this.expiration = expiration;
    }
}
