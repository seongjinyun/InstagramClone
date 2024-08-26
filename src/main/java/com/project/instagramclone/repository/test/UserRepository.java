package com.project.instagramclone.repository.test;

import com.project.instagramclone.entity.test.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Boolean existsByUsername(String username);
    UserEntity findByUsername(String username);
}