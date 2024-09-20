package com.project.instagramclone.controller;

import com.project.instagramclone.jwt.JWTUtil;
import com.project.instagramclone.service.oauth2.OAuth2JwtHeaderService;
import com.project.instagramclone.service.oauth2.OAuth2UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * OAuth2 로그인 시 바로 응답을 할 수 없기 때문에 리다이렉트 하여 쿠키로 토큰을 보냄 (액세스+리프레시)
 * httpOnly 쿠키 저장 시 xss 공격은 막을 수 있지만, csrf 공격에 취약하다
 * -> 백엔드 서버로 재요청하여 헤더에 담아서 보냄. 프론트엔드는 로컬스토리지에 액세스 토큰 저장
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name="OAuth2 service", description="OAuth2 service API")
public class OAuth2Controller {

    private final OAuth2JwtHeaderService oAuth2JwtHeaderService;
    private final OAuth2UserService oAuth2UserService;
    private final JWTUtil jwtUtil;

    @PostMapping("/oauth2-jwt-header")
    public String oauth2JwtHeader(HttpServletRequest request, HttpServletResponse response) {
        return oAuth2JwtHeaderService.oauth2JwtHeaderSet(request, response);
    }

    @PostMapping("/nickname/set")
    public ResponseEntity<String> setNickname(@RequestHeader("Authorization") String token, @RequestBody Map<String, String> request) {
        // 입력된 닉네임 가져오기
        String nickname = request.get("nickname");
        if (nickname == null || nickname.isEmpty()) {
            return ResponseEntity.badRequest().body("닉네임을 입력하세요.");
        }

        // JWT에서 username 추출 (JWTUtil 활용)
        String username = jwtUtil.getUsername(token.substring(7));  // Bearer 제거 후 JWT 추출
        try {
            oAuth2UserService.updateNickname(username, nickname);
            return ResponseEntity.ok("닉네임 설정 완료");
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자를 찾을 수 없습니다.");
        }
    }
}