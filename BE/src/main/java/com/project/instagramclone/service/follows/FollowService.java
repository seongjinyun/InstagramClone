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
import java.util.Optional;
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
    public void follow(String followerUsername, String memberUsername) {
        Long followerId = getMemberIdByUsername(followerUsername);
        Long memberId = getMemberIdByUsername(memberUsername);

        // 팔로우 대상과 팔로워가 존재하는지 확인
        MemberEntity follower = memberRepository.findById(followerId)
                .orElseThrow(() -> new IllegalArgumentException("팔로워를 찾을 수 없습니다."));
        MemberEntity member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

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
    public void unfollow(String followerUsername, String memberUsername) {
        Long followerId = getMemberIdByUsername(followerUsername);
        Long memberId = getMemberIdByUsername(memberUsername);

        // 팔로우 대상과 팔로워가 존재하는지 확인
        MemberEntity follower = memberRepository.findById(followerId)
                .orElseThrow(() -> new IllegalArgumentException("팔로워를 찾을 수 없습니다."));
        MemberEntity member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 팔로우 상태가 아닌지 확인
        if (!followsRepository.existsByFollowerAndMember(follower, member)) {
            throw new IllegalStateException("팔로우 관계가 존재하지 않습니다.");
        }

        // 예외사항에 해당되지 않으면 언팔로우 진행
        followsRepository.deleteByFollowerAndMember(follower, member);
    }

    // {memberId}를 팔로우하는 팔로워 계정 목록 조회
    public List<FollowDto> getFollowers(String memberUsername) {
        // memberId를 통해 팔로워 조회
        MemberEntity member = formUserRepository.findByUsername(memberUsername)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다"))
                .getMemberEntity();

        List<FollowsEntity> followsEntities = followsRepository.findAllByMember(member);

        // follower와 member의 닉네임을 조회하여 DTO에 담기
        List<FollowDto> followerDTOs = followsEntities.stream().map(f -> {
            String followerNickname = formUserRepository.findById(f.getFollower().getMemberId())
                    .orElseThrow(() -> new IllegalArgumentException("팔로워 정보가 없습니다"))
                    .getNickname();

            String memberNickname = formUserRepository.findById(f.getMember().getMemberId())
                    .orElseThrow(() -> new IllegalArgumentException("회원 정보가 없습니다"))
                    .getNickname();

            return new FollowDto(f.getFollower().getMemberId(), f.getMember().getMemberId(), followerNickname, memberNickname);
        }).toList();

        return followerDTOs;
    }

    // {followerId}가 팔로우하는 계정 목록 조회
    public List<FollowDto> getFollowing(String memberUsername) {
        // memberId를 통해 팔로워 조회
        MemberEntity member = formUserRepository.findByUsername(memberUsername)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다"))
                .getMemberEntity();

        List<FollowsEntity> followsEntities = followsRepository.findAllByFollower(member);

        // follower와 member의 닉네임을 조회하여 DTO에 담기
        List<FollowDto> followerDTOs = followsEntities.stream().map(f -> {
            String followerNickname = formUserRepository.findById(f.getFollower().getMemberId())
                    .orElseThrow(() -> new IllegalArgumentException("팔로워 정보가 없습니다"))
                    .getNickname();

            String memberNickname = formUserRepository.findById(f.getMember().getMemberId())
                    .orElseThrow(() -> new IllegalArgumentException("회원 정보가 없습니다"))
                    .getNickname();

            return new FollowDto(f.getFollower().getMemberId(), f.getMember().getMemberId(), followerNickname, memberNickname);
        }).toList();

        return followerDTOs;
    }

    private FollowDto createFollowDTO(FollowsEntity follows) {
        String followerUsername = formUserRepository.findByMemberEntity(follows.getFollower())
                .map(FormUserEntity::getUsername)
                .orElseGet(() ->
                        oAuth2UserRepository.findByMemberEntity(follows.getFollower())
                                .map(OAuth2UserEntity::getUsername)
                                .orElseThrow(() -> new IllegalArgumentException("팔로워 정보를 찾을 수 없습니다."))
                );

        String memberUsername = formUserRepository.findByMemberEntity(follows.getMember())
                .map(FormUserEntity::getUsername)
                .orElseGet(() ->
                        oAuth2UserRepository.findByMemberEntity(follows.getMember())
                                .map(OAuth2UserEntity::getUsername)
                                .orElseThrow(() -> new IllegalArgumentException("팔로우 대상 정보를 찾을 수 없습니다."))
                );

        return new FollowDto(follows.getFollower().getMemberId(), follows.getMember().getMemberId(), followerUsername, memberUsername);
    }

    // username을 통해 memberId를 조회하는 메서드
    private Long getMemberIdByUsername(String username) {
        return formUserRepository.findByUsername(username)
                .map(FormUserEntity::getMemberEntity)
                .map(MemberEntity::getMemberId)
                .orElseGet(() ->
                        oAuth2UserRepository.findByUsername(username)
                        .map(OAuth2UserEntity::getMemberEntity)
                        .map(MemberEntity::getMemberId)
                        .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."))
                );
    }
}
