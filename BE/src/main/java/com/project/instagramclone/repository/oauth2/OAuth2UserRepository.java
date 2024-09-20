package com.project.instagramclone.repository.oauth2;

import com.project.instagramclone.entity.member.MemberEntity;
import com.project.instagramclone.entity.oauth2.OAuth2UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OAuth2UserRepository extends JpaRepository<OAuth2UserEntity, Long> {

    Boolean existsByUsername(String username);
    Optional<OAuth2UserEntity> findByUsername(String username);
    Optional<OAuth2UserEntity> findByNickname(String nickname);
    Optional<OAuth2UserEntity> findByMemberEntity_MemberId(long memberId);

    @Modifying
    @Transactional
    @Query("UPDATE OAuth2UserEntity u SET u.nickname = :nickname WHERE u.username = :username")
    int updateNickname(@Param("username") String username, @Param("nickname") String nickname);

}
