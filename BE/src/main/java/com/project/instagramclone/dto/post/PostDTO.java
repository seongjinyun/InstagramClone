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
}
