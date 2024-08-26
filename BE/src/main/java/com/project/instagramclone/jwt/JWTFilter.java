package com.project.instagramclone.jwt;

import com.project.instagramclone.dto.form.CustomUserDetails;
import com.project.instagramclone.entity.form.FormUserEntity;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// 이미 access token이 존재할 경우,
// 내부에서 사용할 authentication 정보를 set

@Component
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // access token 조회
        String access = null;
        access = request.getHeader("access");

        // access token이 null일 경우
        if(access == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // access token이 만료되었을(expired) 경우
        try{
            jwtUtil.isExpired(access);
        } catch (ExpiredJwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String category = jwtUtil.getCategory(access);

        // access token이 아닐 경우
        if(!category.equals("access")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // 정상적인 access token일 경우, 정보를 가져온다
        String username = jwtUtil.getUsername(access);
        String role = jwtUtil.getRole(access);

        FormUserEntity formUserEntity = FormUserEntity.builder()
                .username(username)
                .role(role)
                .password("temp_password")
                .build();

        CustomUserDetails customUserDetails = new CustomUserDetails(formUserEntity);
        UsernamePasswordAuthenticationToken authtoken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authtoken);;

        filterChain.doFilter(request, response);
    }
}