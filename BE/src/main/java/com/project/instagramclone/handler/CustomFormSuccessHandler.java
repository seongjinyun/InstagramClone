package com.project.instagramclone.handler;


// form login 성공 이후 JWT 발급
// access token -> jeader
// refresh token -> cookie

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jwt.JWT;
import com.project.instagramclone.jwt.JWTUtil;
import com.project.instagramclone.service.token.RefreshTokenService;
import com.project.instagramclone.util.CookieUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class CustomFormSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        // JWT 생성을 위한 정보 가져오기
        String username = authentication.getName();
        String role = authentication.getAuthorities().iterator().next().getAuthority();

        // access token 생성
        String access = jwtUtil.createJwt("access", username, role, 60*10*1000L);
        response.setHeader("access", access);

        // refresh token 생성
        Integer expireS = 24 * 60 * 60;
        String refresh = jwtUtil.createJwt("refresh", username, role, expireS * 1000L);
        response.addCookie(CookieUtil.createCookie("refresh", refresh, expireS));

        // refresh token을 DB에 저장
        refreshTokenService.saveRefreshToken(username, expireS, refresh);

        // JOSN을 Objectmapper을 통해 직렬화하여 전달
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("name", username);

        new ObjectMapper().writeValue(response.getWriter(), responseData);
    }
}
