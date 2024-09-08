package com.project.instagramclone.service.follows;

import com.project.instagramclone.dto.follows.FollowDto;
import com.project.instagramclone.entity.follows.FollowsEntity;
import com.project.instagramclone.entity.form.FormUserEntity;
import com.project.instagramclone.entity.member.MemberEntity;
import com.project.instagramclone.entity.oauth2.OAuth2UserEntity;
import com.project.instagramclone.repository.follows.FollowsRepository;
import com.project.instagramclone.repository.form.FormUserRepository;
import com.project.instagramclone.repository.member.MemberRepository;
import com.project.instagramclone.repository.oauth2.OAuth2UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class FollowService {

    private final FollowsRepository followsRepository;
    private final MemberRepository memberRepository;

    private final FormUserRepository formUserRepository;
    private final OAuth2UserRepository oAuth2UserRepository;

    // 팔로우 기능
    @Transactional
    public void follow(long followerId, long memberId) {
        // 팔로우 대상과 팔로워가 존재하는지 확인
        MemberEntity follower = memberRepository.findById(followerId)
                .orElseThrow(() -> new IllegalArgumentException("팔로워가 존재하지 않습니다."));
        MemberEntity member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("팔로우 대상이 존재하지 않습니다."));

        // 이미 팔로우된 상태인지 확인
        if (followsRepository.existsByFollowerAndMember(follower, member)) {
            throw new IllegalStateException("이미 팔로우 중입니다.");
        }

        // 예외사항에 해당되지 않으면 팔로우 진행
        FollowsEntity followsEntity = new FollowsEntity();
        followsEntity.setFollower(follower);
        followsEntity.setMember(member);
        followsRepository.save(followsEntity);
    }

    // 언팔로우 기능
    @Transactional
    public void unfollow(long followerId, long memberId) {
        // 팔로우 대상과 팔로워가 존재하는지 확인
        MemberEntity follower = memberRepository.findById(followerId)
                .orElseThrow(() -> new IllegalArgumentException("팔로워가 존재하지 않습니다."));
        MemberEntity member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("팔로우 대상이 존재하지 않습니다."));

        // 팔로우 상태가 아닌지 확인
        if (!followsRepository.existsByFollowerAndMember(follower, member)) {
            throw new IllegalStateException("팔로우 관계가 존재하지 않습니다.");
        }

        // 예외사항에 해당되지 않으면 언팔로우 진행
        followsRepository.deleteByFollowerAndMember(follower, member);
    }

    // {memberId}를 팔로우하는 팔로워 계정 목록 조회
    public List<FollowDto> getFollowers(long memberId) {
        MemberEntity member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));
        List<FollowsEntity> followsEntities = followsRepository.findAllByMember(member);

        return followsEntities.stream()
                .map(follow -> createFollowDTO(follow))
                .collect(Collectors.toList());
    }

    // {followerId}가 팔로우하는 계정 목록 조회
    public List<FollowDto> getFollowing(Long memberId) {
        MemberEntity member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));
        List<FollowsEntity> followsEntities = followsRepository.findAllByFollower(member);

        return followsEntities.stream()
                .map(follow -> createFollowDTO(follow))
                .collect(Collectors.toList());
    }

    private FollowDto createFollowDTO(FollowsEntity follows) {
        Long followerId = follows.getFollower().getMemberId();
        Long memberId = follows.getMember().getMemberId();

        String followerUsername = getUsernameByMemberId(followerId);
        String memberUsername = getUsernameByMemberId(memberId);

        return new FollowDto(follows.getFollowsId(), followerUsername, memberUsername);
    }

    private String getUsernameByMemberId(Long memberId) {
        MemberEntity member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member를 찾을 수 없습니다."));

        return formUserRepository.findByMemberEntity(member)
                .map(FormUserEntity::getUsername)
                .orElseGet(() -> oAuth2UserRepository.findByMemberEntity(member)
                .map(OAuth2UserEntity::getUsername)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다.")));
    }

}
