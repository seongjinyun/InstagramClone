package com.project.instagramclone.jwt;

import com.project.instagramclone.dto.form.CustomUserDetails;
import com.project.instagramclone.entity.form.FormUserEntity;
import com.project.instagramclone.service.form.CustomUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// 이미 access token이 존재할 경우,
// 내부에서 사용할 authentication 정보를 set

@Component
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String uri = request.getRequestURI();

        // 회원가입이나 로그인 등 인증이 필요 없는 엔드포인트는 필터를 거치지 않음
        if (uri.equals("/api/v1/join") || uri.equals("/api/v1/login")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Authorization 또는 access 헤더에서 토큰 추출
        String header = request.getHeader("Authorization");
        String accessHeader = request.getHeader("access");

        String token = null;

        if (header != null && header.startsWith("Bearer ")) {
            token = header.substring(7); // Bearer {token} 형식에서 토큰 추출
        } else if (accessHeader != null) {
            token = accessHeader; // access 헤더에서 토큰 가져오기
        }

        if (token == null) {
            logger.debug("request에서 access token 찾을 수 없음.");
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // 토큰이 만료되었는지 확인
            jwtUtil.isExpired(token);
        } catch (ExpiredJwtException e) {
            logger.debug("access token 만료.");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String category = jwtUtil.getCategory(token);

        if (!category.equals("access")) {
            logger.debug("access token이 아님.");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // 정상적인 토큰인 경우 사용자 정보 추출
        logger.debug("정상적인 access token 조회.");
        String username = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // DB에서 사용자 정보 조회
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);  // DB 조회

            if (jwtUtil.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // authToken의 정상적인 생성이 진행되는지 확인을 위한 로그
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            logger.debug("SecurityContext에 인증 정보가 없음.");
        } else {
            logger.debug("인증된 사용자: " + authentication.getName());
        }

        filterChain.doFilter(request, response);
    }
}