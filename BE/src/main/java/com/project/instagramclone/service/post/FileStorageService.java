package com.project.instagramclone.service.post;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;


//파일 저장 서비스
@Service
@RequiredArgsConstructor
public class FileStorageService {
    private final AmazonS3 amazonS3;

    // S3 버킷 이름 (application.yml에서 가져옴)
    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucketName;

    // 파일 저장 메서드 (S3에 업로드)
    public String saveFile(MultipartFile file) throws IOException {
        // 원본 파일명 가져오기
        String originalFilename = file.getOriginalFilename();

        // 파일이름에 UUID 추가하여 유니크한 이름으로 변경
        String newFilename = UUID.randomUUID().toString() + "_" + originalFilename;

        // S3에 파일 업로드를 위한 메타데이터 설정
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        // S3에 파일 업로드
        amazonS3.putObject(bucketName, newFilename, file.getInputStream(), metadata);

        // 업로드한 파일의 S3 URL 반환
        return amazonS3.getUrl(bucketName, newFilename).toString();
    }
}
