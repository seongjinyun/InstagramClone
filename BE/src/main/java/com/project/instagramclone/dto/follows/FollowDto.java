package com.project.instagramclone.dto.follows;

import lombok.*;

import java.util.Optional;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FollowDto {
    // 팔로우 / 팔로잉하는 회원의 정보를 담을 때 사용
    private Long followsId;
    private String followerUsername;
    private String memberUsername;
}
