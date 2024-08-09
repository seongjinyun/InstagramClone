package com.project.instagramclone.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "memberDetail")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberDetail {

    // 식별 1:1 관계 설정
    @Id
    @Column(name = "memberId")
    private Long memberId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "memberId")
    private Member member;

    @Column(name = "password")
    private String password;

    @Column(name = "profileImgUrl")
    private String profileImgUrl;

}
