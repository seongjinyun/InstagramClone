package com.project.instagramclone.jwt;

import com.project.instagramclone.dto.LoginDto;
import com.project.instagramclone.dto.UserDto;
import com.project.instagramclone.entity.Member;
import com.project.instagramclone.entity.MemberDetail;
import com.project.instagramclone.oauth2.CustomOAuth2User;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // cookie들을 불러온 뒤 Authorization Key에 담긴 쿠키를 찾음
        String authorization = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            System.out.println(cookie.getName());
            if (cookie.getName().equals("Authorization")) {
                authorization = cookie.getValue();
            }
        }

//        request에서 Authorization 헤더를 찾음
//        String authorization= request.getHeader("Authorization");

        // Authorization 헤더 검증
        // if (authorization == null || !authorization.startsWith("Bearer ")) {
        if (authorization == null) {
            System.out.println("token null");
            filterChain.doFilter(request, response);
            // 조건이 해당되면 메소드 종료 (필수)
            return;
        }

//        Bearer 부분 제거 후 순수 토큰만 획득
//        String token = authorization.split(" ")[1];

        String token = authorization;

        // 토큰 소멸 시간 검증
        try {
            jwtUtil.isExpired(token);
        } catch (ExpiredJwtException e) {
            System.out.println("token expired");
            filterChain.doFilter(request, response);
            return;
        }

        // 토큰에서 username과 role 획득
        String username = jwtUtil.getUsername(token);
        // String role = jwtUtil.getRole(token);

        // userEntity를 생성하여 값 set
//        Member member = new Member();
//        MemberDetail memberDetail = new MemberDetail();
//        member.setUid(username);
//        memberDetail.setPassword("temppassword");
//        // userEntity.setRole(role);

        UserDto userDto = new UserDto();
        userDto.setUid(username);
        userDto.setPassword("temppassword");

        // UserDetails에 회원 정보 객체 담기
//        CustomUserDetails customUserDetails = new CustomUserDetails(Optional.of(member), Optional.of(memberDetail));
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDto);

        // 스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());
        // 세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}