package com.project.instagramclone.jwt;

import org.springframework.stereotype.Component;

@Component
public class JWTUtil {
//
//    private Key key;
//
//    public JWTUtil(@Value("${spring.jwt.secret}")String secret) {
//
//
//        byte[] byteSecretKey = Decoders.BASE64.decode(secret);
//        key = Keys.hmacShaKeyFor(byteSecretKey);
//    }
//
//
//    //nickname으로 사용중 수정해야 함
//    public String getUsername(String token) {
//
//        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().get("username", String.class);
//    }
//
//    public Boolean isExpired(String token) {
//
//        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getExpiration().before(new Date());
//    }
//
//    public String createJwt(String username, String role, Long expiredMs) {
//
//        Claims claims = Jwts.claims();
//        claims.put("username", username);
//
//        return Jwts.builder()
//                .setClaims(claims)
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + expiredMs))
//                .signWith(key, SignatureAlgorithm.HS256)
//                .compact();
//    }
}