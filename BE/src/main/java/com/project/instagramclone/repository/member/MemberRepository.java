package com.project.instagramclone.repository.member;

import com.project.instagramclone.entity.member.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    // memberId로 존재 여부를 확인하는 메서드
    boolean existsByMemberId(Long memberId);
}
