package com.project.instagramclone.entity.test;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class RefreshEntity {
    @Id
    @Column(name="Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="username", length = 50)
    private String username;

    @Column(name="refresh", length = 300)
    private String refresh;

    @Column(name="expiration", length = 300)
    private String expiration;

    @Builder
    public RefreshEntity(String username, String refresh, String expiration) {
        this.username = username;
        this.refresh = refresh;
        this.expiration = expiration;
    }
}