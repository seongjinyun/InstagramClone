package com.project.instagramclone.controller.post;

import com.project.instagramclone.dto.post.PostDTO;
import com.project.instagramclone.entity.posts.PostImage;
import com.project.instagramclone.entity.posts.Posts;
import com.project.instagramclone.jwt.JWTUtil;
import com.project.instagramclone.service.post.PostsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name="Post service", description="Post service API")
public class PostController {

    private final PostsService postsService;


    @PostMapping("/create/posts")
    public ResponseEntity<Posts> CreatePost(@RequestHeader("Authorization") String token,
                                            @RequestParam("content") String content,
                                            @RequestParam("mediaFiles") MultipartFile[] mediaFiles) throws IOException {
        // 데이터 처리
        PostDTO postDTO = new PostDTO();
        postDTO.setContent(content);
        postDTO.setMediaFiles(Arrays.asList(mediaFiles));
        Posts posts = postsService.savePost(token, postDTO);
        return ResponseEntity.ok(posts);
    }
}
