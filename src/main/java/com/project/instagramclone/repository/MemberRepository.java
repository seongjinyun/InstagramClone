package com.project.instagramclone.repository;

import com.project.instagramclone.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUid(String uid);
    Optional<Member> findByNickname(String nickname);
}