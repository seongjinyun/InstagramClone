package com.project.instagramclone.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "userDetail")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDetail {

    // 식별 1:1 관계 설정
    @Id
    @OneToOne
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private User user;

    @Column(name = "password")
    private String password;

    @Column(name = "profileImgUrl")
    private String profileImgUrl;

}
