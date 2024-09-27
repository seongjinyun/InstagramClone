package com.project.instagramclone.controller.post;

import com.project.instagramclone.dto.post.CommentDTO;
import com.project.instagramclone.jwt.JWTUtil;
import com.project.instagramclone.service.post.CommentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name="Post Comment service", description="Post Comment service API")
public class CommentController {

    private final CommentService commentService;

    private final JWTUtil jwtUtil;

    @PostMapping("/comment/{postId}")
    public ResponseEntity<Void> postComment(String postId,
                                            @RequestParam("content") String comment,
                                            @RequestHeader("Authorization") String token){

        // 토큰에서 Bearer 부분을 제거
        String actualToken = token.replace("Bearer ", "");

        // 토큰에서 정보 추출
        String nickname = jwtUtil.getNickname(actualToken);

        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setPostId(postId); //게시글 id
        commentDTO.setNickname(nickname); //작성자 명
        commentDTO.setComment(comment); //댓글 내용


        commentService.postComment(commentDTO);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
