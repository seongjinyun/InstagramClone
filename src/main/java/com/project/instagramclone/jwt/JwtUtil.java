package com.project.instagramclone.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

// JWT 유틸리티 클래스
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    // JWT 토큰 만료 시간 (1일)
    private final long EXPIRATION_TIME = 86400000;

    // JWT 토큰 생성 메서드
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username) // 토큰에 사용자 이름 설정
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // 만료 시간 설정
                .signWith(SignatureAlgorithm.HS512, secretKey) // 서명 알고리즘 및 비밀 키 설정
                .compact();
    }

    // JWT 토큰에서 사용자 이름 추출 메서드
    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    // JWT 토큰에서 Claims 객체 추출 메서드
    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey) // 비밀 키 설정
                .parseClaimsJws(token) // 토큰 파싱
                .getBody();
    }
}