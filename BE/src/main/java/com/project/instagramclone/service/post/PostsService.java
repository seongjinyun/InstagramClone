package com.project.instagramclone.service.post;

import com.project.instagramclone.entity.posts.Posts;
import com.project.instagramclone.repository.post.PostImageRepository;
import com.project.instagramclone.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostsService {

    //파일이 저장되는 서버 경로
    private final String UPLOAD_DIR = "uploads/";

    // local, oauth2 service
    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;

    // 게시글 작성
    public String savePost(MultipartFile file) throws IOException {
        if (!Files.exists(Paths.get(UPLOAD_DIR))) {
            Files.createDirectories(Paths.get(UPLOAD_DIR));
        }
        String fileExtension = getFileExtension(file.getOriginalFilename());
        String newFileName = UUID.randomUUID().toString() + fileExtension;
        String filePath = UPLOAD_DIR + newFileName;
        File dest = new File(filePath);
        file.transferTo(dest);

        return filePath;  // 저장된 파일 경로 반환
    }
    private String getFileExtension(String filename) {
        return filename.substring(filename.lastIndexOf("."));
    }
}
