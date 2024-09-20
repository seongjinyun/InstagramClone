package com.project.instagramclone.service.post;

import com.project.instagramclone.dto.follows.FollowDto;
import com.project.instagramclone.dto.post.PostDTO;
import com.project.instagramclone.entity.posts.PostImage;
import com.project.instagramclone.entity.posts.Posts;
import com.project.instagramclone.jwt.JWTUtil;
import com.project.instagramclone.repository.post.PostImageRepository;
import com.project.instagramclone.repository.post.PostRepository;
import com.project.instagramclone.service.follows.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostsService {

    // local, oauth2 service
    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;
    private final FileStorageService fileStorageService;
    private final FollowService followService;
    private final JWTUtil jwtUtil;


    // 게시글 작성
    public Posts savePost(String token, PostDTO postDTO) throws IOException {
        // mediaFiles와 content를 DTO로 받아옴
        List<MultipartFile> mediaFiles = postDTO.getMediaFiles();
        String content = postDTO.getContent();

        if (mediaFiles.size() > 10) {
            throw new IllegalArgumentException("10개 이상의 파일은 업로드 불가능 합니다");
        }

        // 토큰에서 Bearer 부분을 제거
        String actualToken = token.replace("Bearer ", "");

        // 토큰에서 정보 추출
        String userName = jwtUtil.getUsername(actualToken);
        String role = jwtUtil.getRole(actualToken); // 필요할지 ?

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

    //팔로우 한 계정의 게시글 조회
    public List<Posts> getPosts(String token) {
        System.out.println("JWT Token: " + token);

        String actualToken = token.replace("bearer", "").trim();
        String userName = jwtUtil.getUsername(actualToken);

        // Debugging 로그 추가
        System.out.println("Username from token: " + userName);

        //팔로우 중인 멤버들을 가져와서
        List<FollowDto> followingUserDto = followService.getFollowing(userName);

        // Debugging 로그 추가
        System.out.println("Following users: " + followingUserDto);

        // followingUserDto에서 memberUsername 값을 추출하여 List<String>에 저장
        List<String> followingUserList = followingUserDto.stream()
                .map(FollowDto::getMemberUsername)  // FollowDto에서 memberUsername 추출
                .collect(Collectors.toList());

        // Debugging 로그 추가
        System.out.println("Usernames to query: " + followingUserList);

        // 추출된 사용자들의 게시글 조회
        List<Posts> postList = postRepository.findPostsByUserNameIn(followingUserList);

        // Debugging 로그 추가
        System.out.println("Posts found: " + postList);

        return postList;  // 조회된 게시글 리스트 반환
    }
}
