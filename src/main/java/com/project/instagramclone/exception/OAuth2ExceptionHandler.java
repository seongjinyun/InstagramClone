package com.project.instagramclone.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

// Spring Security 예외를 핸들링하는 코드
@ControllerAdvice
public class OAuth2ExceptionHandler {

    @ExceptionHandler(OAuth2AuthenticationException.class)
    public ResponseEntity<String> handleOAuth2Exception(OAuth2AuthenticationException ex) {
        ex.printStackTrace();  // 디버그용으로 스택 트레이스를 출력
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("OAuth2 오류 발생: " + ex.getMessage());
    }
}
