package com.project.instagramclone.controller;

import com.project.instagramclone.dto.UserDto;
import com.project.instagramclone.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signup(
            @Valid @RequestBody UserDto userDto
    ) {
        return ResponseEntity.ok(userService.signup(userDto));
    }

    @GetMapping("/oauth/google/login")
    public ResponseEntity<String> login(
            @RequestParam String sns_id,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        // OAuth 로그인 성공시 UserDto를 반환
        return ResponseEntity.ok("완료");
        //return null;
    }
}