package com.project.instagramclone.controller;

import com.project.instagramclone.dto.account.AccountUpdateDto;
import com.project.instagramclone.dto.account.PasswordChangeDto;
import com.project.instagramclone.dto.form.CustomUserDetails;
import com.project.instagramclone.dto.oauth2.CustomOAuth2User;
import com.project.instagramclone.service.account.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user")
public class AccountController {

    private final AccountService accountService;

    // 사용자 정보 수정
    @PutMapping("/update")
    public ResponseEntity<String> updateAccount(@RequestBody AccountUpdateDto accountUpdateDto, @AuthenticationPrincipal Object principal) {
        Long memberId = extractMemberId(principal);
        accountService.updateAccount(memberId, accountUpdateDto);
        return ResponseEntity.ok("계정 정보가 성공적으로 업데이트되었습니다.");
    }

    // 비밀번호 변경
    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody PasswordChangeDto passwordChangeDto, @AuthenticationPrincipal Object principal) {
        Long memberId = extractMemberId(principal);
        boolean isChanged = accountService.changePassword(memberId, passwordChangeDto);
        if (isChanged) {
            return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("현재 비밀번호가 일치하지 않습니다.");
        }
    }

    // memberId 추출
    private Long extractMemberId(Object principal) {
        if (principal instanceof CustomUserDetails) {
            return ((CustomUserDetails) principal).getMemberId();
        } else if (principal instanceof CustomOAuth2User) {
            return ((CustomOAuth2User) principal).getMemberId();
        }
        throw new IllegalArgumentException("알 수 없는 사용자 타입");
    }

}
