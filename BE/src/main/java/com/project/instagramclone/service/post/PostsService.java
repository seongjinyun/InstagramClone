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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
        String nickname = jwtUtil.getNickname(actualToken);
        String role = jwtUtil.getRole(actualToken); // 필요할지 ?

        Posts posts = new Posts();
        posts.setContent(postDTO.getContent());
        posts.setNickname(nickname);
        posts.setRegdate(System.currentTimeMillis()); // 게시글 등록 시간

        Posts savedPost = postRepository.save(posts);
        if(savedPost == null) {
            throw new IllegalStateException("게시글 저장 실패");
        }

        // 이미지 URL 리스트 생성
        List<String> mediaUrls = new ArrayList<>();

        // 각 파일을 저장하고 그 URL을 리스트에 추가
        for (MultipartFile file : mediaFiles) {
            String fileUrl = fileStorageService.saveFile(file);
            mediaUrls.add(fileUrl);
        }

        // PostImage 객체에 이미지 URL 리스트 저장
        PostImage postImage = new PostImage();
        postImage.setPostId(savedPost.getId());
        postImage.setMediaUrl(mediaUrls); // List<String>로 저장
        postImageRepository.save(postImage);

        return savedPost;
    }

    //팔로우 한 계정의 게시글 조회
    @Transactional
    public List<PostDTO> getPosts(String token) throws IOException {

        String actualToken = token.replace("Bearer", "");
        String nickname = jwtUtil.getNickname(actualToken);

        //팔로우 중인 멤버들을 가져와서
        List<FollowDto> followingUserDto = followService.getFollowing(nickname);

        // followingUserDto에서 memberUsername 값을 추출하여 List<String>에 저장
        List<String> followingUserList = followingUserDto.stream()
                .map(FollowDto::getFollowingNickname)  // FollowDto에서 memberUsername 추출
                .collect(Collectors.toList());

        // 추출된 사용자들의 게시글 조회
        List<Posts> postList = postRepository.findPostsByNicknameIn(followingUserList);

        // 각 게시글에 연결된 이미지 파일을 조회하고 PostDTO에 변환하여 반환
        List<PostDTO> postDTOList = postList.stream().map(post -> {
            // 게시글에 연결된 이미지 조회
            List<PostImage> postImages = postImageRepository.findByPostId(post.getId());

            // 각 PostImage에서 mediaUrl 리스트들을 모두 합침
            List<String> mediaUrls = postImages.stream()
                    .flatMap(postImage -> postImage.getMediaUrl().stream())  // 각 PostImage에서 mediaUrl 리스트 추출 후 flatMap으로 병합
                    .collect(Collectors.toList());

            // PostDTO 생성
            return PostDTO.builder()
                    .postId(post.getId())
                    .content(post.getContent())
                    .mediaUrls(mediaUrls) // 이미지 파일 URL 리스트 추가
                    .writer(post.getNickname()) // 작성자 추가
                    .build();
        }).collect(Collectors.toList());

        return postDTOList;  // 조회된 게시글 리스트 반환
    }

    //게시글 상세 보기
    public PostDTO getPostById(String postId){

        Posts post = postRepository.findById(postId).orElse(null);

        List<PostImage> postImages = postImageRepository.findByPostId(Objects.requireNonNull(post).getId()); //post 가 null이라면 예외처리

        List<String> mediaUrls = postImages.stream()
                .flatMap(postImage -> postImage.getMediaUrl().stream())  // 각 PostImage에서 mediaUrl 리스트 추출 후 flatMap으로 병합
                .collect(Collectors.toList());

        return PostDTO.builder()
                .postId(post.getId())
                .content(post.getContent())
                .mediaUrls(mediaUrls) // 이미지 파일 URL 리스트 추가
                .writer(post.getNickname()) // 작성자 추가
                .build();
    }
}
