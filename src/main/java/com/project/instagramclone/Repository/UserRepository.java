package com.project.instagramclone.Repository;

import com.project.instagramclone.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// User 엔티티의 JPA 리포지토리 인터페이스
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // 닉네임으로 사용자 찾기 메서드 정의
    Optional<User> findByNickname(String nickname);
}