package com.project.instagramclone.dto.post;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@Builder

@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {

    private String content; // 글 내용
    private List<MultipartFile> mediaFiles; // 파일명 리스트
    // 게시글 조회 시 사용
    private List<String> mediaUrls; // 저장된 이미지 URL 리스트
    private String writer; //작성자 - nickname
}
