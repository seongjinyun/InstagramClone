package com.project.instagramclone.controller.post;

import com.project.instagramclone.dto.post.PostDTO;
import com.project.instagramclone.entity.posts.PostImage;
import com.project.instagramclone.entity.posts.Posts;
import com.project.instagramclone.jwt.JWTUtil;
import com.project.instagramclone.service.post.PostsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name="Post service", description="Post service API")
public class PostController {

    private final PostsService postsService;

    //api 설계 오류 추후에 create 제외
    @PostMapping("/create/posts")
    public ResponseEntity<Void> createPost(@RequestHeader("Authorization") String token,
                                            @RequestParam("content") String content,
                                            @RequestParam("mediaFiles") MultipartFile[] mediaFiles) throws IOException {
        // 데이터 처리
        PostDTO postDTO = new PostDTO();
        postDTO.setContent(content);
        postDTO.setMediaFiles(Arrays.asList(mediaFiles));
        postsService.savePost(token, postDTO);

        // 201 Created 응답 반환
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/posts")
    public ResponseEntity<List<Posts>> getPost(@RequestHeader("Authorization") String token){
        List<Posts> posts = postsService.getPosts(token);
        return ResponseEntity.ok(posts);
    }
}
