package com.project.instagramclone.repository.oauth2;

import com.project.instagramclone.entity.member.MemberEntity;
import com.project.instagramclone.entity.oauth2.OAuth2UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OAuth2UserRepository extends JpaRepository<OAuth2UserEntity, Long> {

    Optional<OAuth2UserEntity> findByUsername(String username);
    Optional<OAuth2UserEntity> findByMemberEntity(MemberEntity memberEntity);
    Optional<OAuth2UserEntity> findByNickname(String nickname);
}
