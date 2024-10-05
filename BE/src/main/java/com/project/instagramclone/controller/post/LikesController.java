package com.project.instagramclone.controller.post;

import com.project.instagramclone.jwt.JWTUtil;
import com.project.instagramclone.service.post.LikesService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name="Post service", description="Post Likes service API")
public class LikesController {
    private final LikesService likesService;
    private final JWTUtil jwtUtil;

    @PostMapping("/likes/{postId}")
    public void likePost(@PathVariable String postId, HttpServletRequest request) {
        String nickname = extractUserIdFromToken(request);
        likesService.addLike(postId, nickname);
    }

    @DeleteMapping("/likes/{postId}")
    public void unlikePost(@PathVariable String postId, HttpServletRequest request) {
        String nickname = extractUserIdFromToken(request);
        likesService.removeLike(postId, nickname);
    }

    private String extractUserIdFromToken(HttpServletRequest request) {
        // Authorization 헤더에서 토큰 가져오기
        String token = request.getHeader("Authorization");

        // 토큰에서 Bearer 부분을 제거
        String actualToken = token.replace("Bearer ", "");

        // 토큰에서 닉네임 추출 후 리턴
        return jwtUtil.getNickname(actualToken);
    }
}
