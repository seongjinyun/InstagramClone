package com.project.instagramclone.controller;

import com.project.instagramclone.dto.follows.FollowDto;
import com.project.instagramclone.entity.follows.FollowsEntity;
import com.project.instagramclone.entity.member.MemberEntity;
import com.project.instagramclone.repository.form.FormUserRepository;
import com.project.instagramclone.repository.oauth2.OAuth2UserRepository;
import com.project.instagramclone.service.follows.FollowService;
import com.project.instagramclone.service.form.CustomUserDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name="follow service", description="follow service API")
public class FollowController {

    private final FollowService followService;
    private final CustomUserDetailsService customUserDetailsService;

    @Operation(summary = "팔로우 요청", description = "특정 사용자를 팔로우합니다.")
    @PostMapping("/{username}/follow/{memberUsername}")
    public ResponseEntity<String> follow(@PathVariable String username, @PathVariable String memberUsername, Authentication authentication) {
        // username을 기반으로 현재 사용자와 일치하는지 확인할 수 있습니다.
        if (!username.equals(authentication.getName())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한이 없습니다.");
        }
        followService.follow(username, memberUsername);
        return ResponseEntity.ok("팔로우 성공");
    }

    @Operation(summary = "언팔로우 요청", description = "특정 사용자를 언팔로우합니다.")
    @DeleteMapping("/{username}/unfollow/{memberUsername}")
    public ResponseEntity<String> unfollow(@PathVariable String username, @PathVariable String memberUsername, Authentication authentication) {
        if (!username.equals(authentication.getName())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한이 없습니다.");
        }
        followService.unfollow(username, memberUsername);
        return ResponseEntity.ok("언팔로우 성공");
    }

    @Operation(summary = "팔로워 목록 조회", description = "특정 사용자를 팔로우하는 모든 팔로워 목록을 조회합니다.")
    @GetMapping("/{username}/followers")
    public ResponseEntity<List<FollowDto>> getFollowers(@PathVariable String username, Authentication authentication) {
        if (!username.equals(authentication.getName())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<FollowDto> followers = followService.getFollowers(username);
        return ResponseEntity.ok(followers);
    }

    @Operation(summary = "팔로우 목록 조회", description = "특정 사용자가 팔로우하는 모든 팔로우 목록을 조회합니다.")
    @GetMapping("/{username}/following")
    public ResponseEntity<List<FollowDto>> getFollowing(@PathVariable String username, Authentication authentication) {
        if (!username.equals(authentication.getName())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<FollowDto> following = followService.getFollowing(username);
        return ResponseEntity.ok(following);
    }
}
