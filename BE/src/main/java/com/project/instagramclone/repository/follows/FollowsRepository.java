package com.project.instagramclone.repository.follows;

import com.project.instagramclone.entity.follows.FollowsEntity;
import com.project.instagramclone.entity.member.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowsRepository extends JpaRepository<FollowsEntity, Long> {

    // 팔로우 여부 확인
    boolean existsByFollowerAndMember(MemberEntity follower, MemberEntity member);

    // 팔로워 목록 조회 (나를 팔로우하는 계정 목록)
    // SELECT * from follows WHERE member_id = :memberId;
    // member가 나이고 follower가 팔로워 목록
    List<FollowsEntity> findAllByMember(MemberEntity member);

    // 팔로우 목록 조회 (내가 팔로우하고 있는 계정 목록)
    // SELECT * from follows WHERE follower_id = :followerId;
    // follower가 나이고 member가 내가 팔로우하는 목록
    List<FollowsEntity> findAllByFollower(MemberEntity follower);

    // 팔로우 관계 삭제 (언팔로우)
    void deleteByFollowerAndMember(MemberEntity follower, MemberEntity member);

}
