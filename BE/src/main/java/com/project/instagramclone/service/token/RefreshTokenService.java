package com.project.instagramclone.service.token;

import com.project.instagramclone.entity.token.RefreshTokenEntity;
import com.project.instagramclone.repository.token.RefreshRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshRepository refreshRepository;

    @Transactional
    public void saveRefreshToken(String username, Integer expireS, String refresh) {
        RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.builder()
                .username(username)
                .refresh(refresh)
                .expiration(new Date(System.currentTimeMillis() + expireS * 1000L).toString())
                .build();

        refreshRepository.save(refreshTokenEntity);
    }

    public boolean existsByUsername(String username) {
        return refreshRepository.existsAllByUsername(username);
    }

}
