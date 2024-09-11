package com.project.instagramclone.entity.posts;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "postImage")
public class PostImage {
    @Id
    private String id;
    private String postId; // posts 컬렉션의 ID를 참조
    private String mediaUrl; // 파일 URL

}