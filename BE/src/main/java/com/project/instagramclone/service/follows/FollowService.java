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
    public void follow(String followerNickname, String followingNickname) {
        // 닉네임을 통해 username 가져오기
        String followerUsername = getUsernameByNickname(followerNickname);
        String followingUsername = getUsernameByNickname(followingNickname);

        // member table의 id 조회
        Long followerId = getMemberIdByUsername(followerUsername);
        Long followingId = getMemberIdByUsername(followingUsername);

        // 팔로우 대상과 팔로워가 존재하는지 확인
        MemberEntity follower = memberRepository.findById(followerId)
                .orElseThrow(() -> new IllegalArgumentException("팔로워를 찾을 수 없습니다."));
        MemberEntity following = memberRepository.findById(followingId)
                .orElseThrow(() -> new IllegalArgumentException("팔로우 대상 사용자를 찾을 수 없습니다."));

        // 이미 팔로우된 상태인지 확인
        if (followsRepository.existsByFollowerAndFollowing(follower, following)) {
            throw new IllegalStateException("이미 팔로우 중입니다.");
        }

        // 예외사항에 해당되지 않으면 팔로우 진행
        FollowsEntity followsEntity = new FollowsEntity();
        followsEntity.setFollower(follower);
        followsEntity.setFollowing(following);
        followsRepository.save(followsEntity);
    }

    // 언팔로우 기능
    @Transactional
    public void unfollow(String followerNickname, String followingNickname) {
        // 닉네임을 통해 username 가져오기
        String followerUsername = getUsernameByNickname(followerNickname);
        String followingUsername = getUsernameByNickname(followingNickname);

        // member table의 id 조회
        Long followerId = getMemberIdByUsername(followerUsername);
        Long followingId = getMemberIdByUsername(followingUsername);

        // 팔로우 대상과 팔로워가 존재하는지 확인
        MemberEntity follower = memberRepository.findById(followerId)
                .orElseThrow(() -> new IllegalArgumentException("팔로워를 찾을 수 없습니다."));
        MemberEntity following = memberRepository.findById(followingId)
                .orElseThrow(() -> new IllegalArgumentException("팔로우 대상 사용자를 찾을 수 없습니다."));

        // 팔로우 상태가 아닌지 확인
        if (!followsRepository.existsByFollowerAndFollowing(follower, following)) {
            throw new IllegalStateException("팔로우 관계가 존재하지 않습니다.");
        }

        // 예외사항에 해당되지 않으면 언팔로우 진행
        followsRepository.deleteByFollowerAndFollowing(follower, following);
    }

    // {nickname}을 팔로우하는 팔로워 계정 목록 조회
    public List<FollowDto> getFollowers(String nickname) {
        // nickname으로 username 조회
        String username = getUsernameByNickname(nickname);

        // member table의 id 조회
        Long followingId = getMemberIdByUsername(username);

        // follow table에서 {followingId}를 팔로우하고 있는 리스트 조회
        List<FollowsEntity> followsEntities = followsRepository.findAllByFollowing_MemberId(followingId);

        // follower와 following의 닉네임을 조회하여 DTO에 담기
        return followsEntities.stream()
                .map(follow -> {
                    // 팔로워의 닉네임을 가져옴
                    String followerNickname = getNicknameByMemberId(follow.getFollower().getMemberId());
                    String followingNickname = getNicknameByMemberId(follow.getFollowing().getMemberId());
                    return new FollowDto(follow.getFollower().getMemberId(), follow.getFollowing().getMemberId(), followerNickname, followingNickname);
                })
                .collect(Collectors.toList());
    }

    // {nickname}이 팔로우하는 계정 목록 조회
    public List<FollowDto> getFollowing(String nickname) {
        // nickname으로 username 조회
        String username = getUsernameByNickname(nickname);

        // member table의 id 조회
        Long followerId = getMemberIdByUsername(username);

        // follow table에서 {followerId}가 팔로우하고 있는 리스트 조회
        List<FollowsEntity> followsEntities = followsRepository.findAllByFollower_MemberId(followerId);

        // follower와 following의 닉네임을 조회하여 DTO에 담기
        return followsEntities.stream()
                .map(follow -> {
                    // 팔로워의 닉네임을 가져옴
                    String followerNickname = getNicknameByMemberId(follow.getFollower().getMemberId());
                    String followingNickname = getNicknameByMemberId(follow.getFollowing().getMemberId());
                    return new FollowDto(follow.getFollower().getMemberId(), follow.getFollowing().getMemberId(), followerNickname, followingNickname);
                })
                .collect(Collectors.toList());
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

    // nickname을 통해 username을 가져오는 메서드
    public String getUsernameByNickname(String nickname) {
        // FormUserEntity에서 nickname 검색
        Optional<FormUserEntity> formUser = formUserRepository.findByNickname(nickname);
        if(formUser.isPresent()) {
            return formUser.get().getUsername();
        }

        // OAuth2UserEntity에서 nickname 검색
        Optional<OAuth2UserEntity> oauth2User = oAuth2UserRepository.findByNickname(nickname);
        if(oauth2User.isPresent()) {
            return  oauth2User.get().getUsername();
        }

        // 닉네임을 찾을 수 없는 경우
        throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
    }

    private String getNicknameByMemberId(Long memberId) {
        Optional<FormUserEntity> formUser = formUserRepository.findByMemberEntity_MemberId(memberId);
        if (formUser.isPresent()) {
            return formUser.get().getNickname();
        }

        Optional<OAuth2UserEntity> oauth2User = oAuth2UserRepository.findByMemberEntity_MemberId(memberId);
        if (oauth2User.isPresent()) {
            return oauth2User.get().getNickname();
        }

        throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
    }

}
