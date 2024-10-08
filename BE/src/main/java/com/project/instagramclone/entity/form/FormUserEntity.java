package com.project.instagramclone.entity.form;

import com.project.instagramclone.entity.member.MemberEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "formUser")
@Getter
@Setter
@NoArgsConstructor
public class FormUserEntity {

    @Id
    @Column(name = "userId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;

    // 아이디
    @Column(name = "username")
    private String username;

    // 비밀번호
    @Column(name = "password")
    private String password;

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

    @ManyToOne
    @JoinColumn(name = "memberId", referencedColumnName = "memberId", nullable = false)
    private MemberEntity memberEntity;  // MemberEntity와의 관계 설정

    @Builder
    public FormUserEntity(String username, String password, String nickname, String email, boolean activated, String role, MemberEntity memberEntity) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.activated = activated;
        this.role = role;
        this.memberEntity = memberEntity;
    }

}
