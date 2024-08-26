package com.project.instagramclone.repository.token;

import com.project.instagramclone.entity.token.RefreshTokenEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RefreshRepository extends JpaRepository<RefreshTokenEntity, Long> {
    List<RefreshTokenEntity> findByUsername(String username);
    Boolean existsByRefresh(String refresh);
    @Transactional
    void deleteByRefresh(String refresh);
}
