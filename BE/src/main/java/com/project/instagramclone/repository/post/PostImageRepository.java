package com.project.instagramclone.repository.post;

import com.project.instagramclone.entity.posts.PostImage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PostImageRepository extends MongoRepository<PostImage, String> {
    List<PostImage> findByPostId(String postId); // 특정 게시물에 연결된 이미지 찾기
}
