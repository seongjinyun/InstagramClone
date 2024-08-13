package com.project.instagramclone.controller;

import com.project.instagramclone.dto.UserDto;
import com.project.instagramclone.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signup(
            @Valid @RequestBody UserDto userDto
    ) {
        return ResponseEntity.ok(memberService.signup(userDto));
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