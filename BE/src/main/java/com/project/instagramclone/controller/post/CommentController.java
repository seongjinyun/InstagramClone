package com.project.instagramclone.controller.post;

import com.project.instagramclone.dto.post.CommentDTO;
import com.project.instagramclone.entity.posts.Comments;
import com.project.instagramclone.jwt.JWTUtil;
import com.project.instagramclone.service.post.CommentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name="Post Comment service", description="Post Comment service API")
public class CommentController {

    private final CommentService commentService;

    private final JWTUtil jwtUtil;

    // 게시글에 댓글 작성
    @PostMapping("/comment/{postId}")
    public ResponseEntity<Void> createComment(@PathVariable String postId,
                                              @RequestParam("comment") String comment,
                                              @RequestHeader("Authorization") String token) {

        // 토큰에서 Bearer 부분을 제거
        String actualToken = token.replace("Bearer ", "");

        // 토큰에서 닉네임 추출
        String nickname = jwtUtil.getNickname(actualToken);

        // CommentDTO 생성 및 값 설정
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setPostId(postId); // 게시글 ID
        commentDTO.setNickname(nickname); // 작성자 닉네임
        commentDTO.setComment(comment); // 댓글 내용

        // 댓글 생성 서비스 호출
        commentService.createComment(commentDTO);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 게시글에 대댓글 작성
    @PostMapping("/reply/{postId}")
    public ResponseEntity<Void> createReply(@PathVariable String postId,
                                            @RequestParam("reply") String reply,
                                            @RequestParam("parentCommentId") String parentCommentId,
                                            @RequestHeader("Authorization") String token) {

        // 토큰에서 Bearer 부분을 제거
        String actualToken = token.replace("Bearer ", "");

        // 토큰에서 닉네임 추출
        String nickname = jwtUtil.getNickname(actualToken);

        // CommentDTO 생성 및 값 설정
        CommentDTO replyDTO = new CommentDTO();
        replyDTO.setPostId(postId); // 게시글 ID
        replyDTO.setNickname(nickname); // 작성자 닉네임
        replyDTO.setComment(reply); // 대댓글 내용
        replyDTO.setParentCommentId(parentCommentId); // 부모 댓글 ID

        // 대댓글 생성 서비스 호출
        commentService.createReply(replyDTO);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 게시글의 댓글 및 대댓글 조회
    @GetMapping("/comments/{postId}")
    public ResponseEntity<List<Comments>> getComments(@PathVariable String postId) {
        List<Comments> comments = commentService.getCommentsForPost(postId);
        return ResponseEntity.ok(comments);
    }
}
