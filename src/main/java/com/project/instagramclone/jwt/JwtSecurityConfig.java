package com.project.instagramclone.jwt;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// TokenProvider, JwtFilter를 SecurityConfig에 적용시 사용할 Config 클래스
public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private final TokenProvider tokenProvider;
    public JwtSecurityConfig(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider; // tokenProvider 주입
    }

    @Override
    public void configure(HttpSecurity http) {
        http.addFilterBefore(
            new JwtFilter(tokenProvider), // JwtFilter를 통해
            UsernamePasswordAuthenticationFilter.class // Security 로직에 필터 등록
        );
    }
}
