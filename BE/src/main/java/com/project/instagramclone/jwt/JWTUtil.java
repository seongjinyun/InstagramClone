package com.project.instagramclone.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

// JWT 발급 및 검증을 위한 클래스
// LoginFIlter에서 주입받아 로그인 성공 시 사용
// JWT 0.11.5 버전
@Component
public class JWTUtil {

    // application.yml에서 key 주입
    private Key key;

    public JWTUtil(@Value("${spring.jwt.secret}") String secret) {
        byte[] byteSecretKey = Decoders.BASE64.decode(secret);
        key = Keys.hmacShaKeyFor(byteSecretKey);
    }

    private Claims getPayload(String token){
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String getUsername(String token){
        return getPayload(token).get("username", String.class);
    }

    public String getNickname(String token){
        return getPayload(token).get("nickname", String.class);
    }

    public String getRole(String token){
        return getPayload(token).get("role", String.class);
    }

    public String getCategory(String token){
        return getPayload(token).get("category", String.class);
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsername(token);  // 토큰에서 username 추출
        return (username.equals(userDetails.getUsername()) && !isExpired(token));  // username 일치 여부 및 만료 여부 확인
    }

    public Boolean isExpired(String token){
        return getPayload(token).getExpiration().before(new Date());
    }

    public String createJwt(String category, String username, String nickname, String role, Long expiredMs) {
        Claims claims = Jwts.claims();
        claims.put("category", category);
        claims.put("username", username);
        claims.put("nickname", nickname);
        claims.put("role", role);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}