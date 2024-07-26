package com.project.instagramclone.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "users")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @Column(name = "userId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "uid")
    private String uid;

    @Column(name = "nickname", length = 20, unique = true)
    private String nickname;

    @Column(name = "email", length = 50)
    private String email;

    @Column(name = "activated")
    private boolean activated;

    // 비식별 1:N 관계 설정
    @ManyToOne
    @JoinColumn(name = "snsId")
    private Sns sns;
}
