package com.project.instagramclone.service.post;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;


//파일 저장 서비스
@Service
public class FileStorageService {
    // 파일을 저장할 경로
    private final String UPLOAD_DIR = "C:/Study/Images"; // 실제 경로로 수정 필요


    public FileStorageService() {
        // 애플리케이션 시작 시 디렉터리가 존재하지 않으면 생성
        Path path = Paths.get(UPLOAD_DIR);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                throw new RuntimeException("Could not create upload directory!", e);
            }
        }
    }

    // 파일 저장 메서드
    public String saveFile(MultipartFile file) throws IOException {
        // 원본 파일명 가져오기
        String originalFilename = file.getOriginalFilename();

        // 파일이름에 UUID 추가하여 유니크한 이름으로 변경
        String newFilename = UUID.randomUUID().toString() + "_" + originalFilename;

        // 파일 저장 경로 설정
        Path filePath = Paths.get(UPLOAD_DIR, newFilename);

        // 파일 저장
        Files.copy(file.getInputStream(), filePath);

        // 저장된 파일 경로 반환 (URL 혹은 상대경로)
        return filePath.toString();
    }
}
