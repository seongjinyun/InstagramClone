package com.project.instagramclone.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
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
    public JWTUtil(@Value("${jwt.secret}") String secret) {
        byte[] byteSecretKey = Decoders.BASE64.decode(secret);
        key = Keys.hmacShaKeyFor(byteSecretKey);
    }

    public String getUsername(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().get("username", String.class);
    }

//    public String getRole(String token) {
//        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().get("role", String.class);
//    }

//    public Boolean isExpired(String token) {
//        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getExpiration().before(new Date(System.currentTimeMillis()));
//    }

    public Boolean isExpired(String token) {
        Date now = new Date(System.currentTimeMillis());
        Date expiration = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();

        System.out.println("현재 시간: " + now);
        System.out.println("JWT 만료 시간: " + expiration);

        return expiration.before(now);
    }

    public String createJwt(String username, /*String role,*/ Long expiredMs) {
        Claims claims = Jwts.claims();
        claims.put("username", username);
        // claims.put("role", role);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}