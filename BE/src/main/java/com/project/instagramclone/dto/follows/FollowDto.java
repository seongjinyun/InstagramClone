package com.project.instagramclone.dto.follows;

import com.project.instagramclone.entity.follows.FollowsEntity;
import com.project.instagramclone.entity.form.FormUserEntity;
import com.project.instagramclone.entity.member.MemberEntity;
import com.project.instagramclone.entity.oauth2.OAuth2UserEntity;
import com.project.instagramclone.repository.form.FormUserRepository;
import com.project.instagramclone.repository.member.MemberRepository;
import com.project.instagramclone.repository.oauth2.OAuth2UserRepository;
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
