package com.project.instagramclone.service.post;

import com.project.instagramclone.dto.post.PostDTO;
import com.project.instagramclone.entity.posts.PostImage;
import com.project.instagramclone.entity.posts.Posts;
import com.project.instagramclone.jwt.JWTUtil;
import com.project.instagramclone.repository.post.PostImageRepository;
import com.project.instagramclone.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostsService {

    // local, oauth2 service
    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;
    private final FileStorageService fileStorageService;
    private final JWTUtil jwtUtil;


    // 게시글 작성
    public Posts savePost(String token, PostDTO postDTO) throws IOException {
        // mediaFiles와 content를 DTO로 받아옴
        List<MultipartFile> mediaFiles = postDTO.getMediaFiles();
        String content = postDTO.getContent();

        if (mediaFiles.size() > 10) {
            throw new IllegalArgumentException("파일 10개이상 업로드 불가능");
        }

        // 토큰에서 Bearer 부분을 제거
        String actualToken = token.replace("Bearer ", "");

        // 토큰에서 정보 추출
        String userName = jwtUtil.getUsername(actualToken);
        String role = jwtUtil.getRole(actualToken);

        Posts posts = new Posts();
        posts.setContent(postDTO.getContent());
        posts.setUserName(userName);
        posts.setRegdate(System.currentTimeMillis()); // 게시글 등록 시간

        Posts savedPost = postRepository.save(posts);

        // 이미지 URL 저장 및 Post와 연결
        for (MultipartFile file : mediaFiles) {
            String fileUrl = fileStorageService.saveFile(file);

            PostImage postImage = new PostImage();
            postImage.setPostId(savedPost.getId());
            postImage.setMediaUrl(fileUrl);
            postImageRepository.save(postImage);
        }
        return savedPost;
    }
}
