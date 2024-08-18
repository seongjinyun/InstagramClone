package com.project.instagramclone.entity.oauth2;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "oauth2User")
@Getter
@Setter
@NoArgsConstructor
public class OAuth2UserEntity {

    @Id
    @Column(name = "userId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;

    // provider + provider id
    @Column(name = "username")
    private String username;

    // OAuth2로 회원가입 시 비밀번호를 저장하지 않음

    // 회원 이름
    @Column(name = "nickname")
    private String nickname;

    // 이메일
    @Column(name = "email")
    private String email;

    // 활성화 여부
    @Column(name = "activated")
    private boolean activated;

    // 권한
    @Column(name = "Role")
    private String role;

    // 비식별 1:N 관계 설정
    @ManyToOne
    @JoinColumn(name = "idpId")
    private IdpEntity idpEntity;

    @Builder
    public OAuth2UserEntity(String username, String nickname, String email, boolean activated, String role, IdpEntity idpEntity) {
        this.username = username;
        this.nickname = nickname;
        this.email = email;
        this.activated = activated;
        this.role = role;
        this.idpEntity = idpEntity;
    }

}
